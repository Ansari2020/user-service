package org.backend.userservice.Services;

import ch.qos.logback.core.testUtil.RandomUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.backend.userservice.Exceptions.TokenNotFoundException;
import org.backend.userservice.Exceptions.UserAlreadyExists;
import org.backend.userservice.Exceptions.UserNotFoundException;
import org.backend.userservice.Exceptions.WrongPassword;
import org.backend.userservice.Models.Token;
import org.backend.userservice.Models.User;
import org.backend.userservice.Repository.TokenRepository;
import org.backend.userservice.Repository.UserRepository;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;


    UserService(BCryptPasswordEncoder passwordEncoder,
                UserRepository userRepository,
                TokenRepository tokenRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;

    }

    @Transactional
    public User signup(String email,
                       String name,
                       String password) throws UserAlreadyExists{
       User user = new User();
       if(userRepository.findByEmail(email).isPresent()){
           throw new UserAlreadyExists("User with email "+email+" already exists");
       }
       user.setEmail(email);
       user.setName(name);
       user.setHashedPassword(passwordEncoder.encode(password));
       user.setEmailVerified(true);
       return  userRepository.save(user);

    }

    public Token login(String email, String password) throws UserNotFoundException, WrongPassword {
        Optional<User> existUser=userRepository.findByEmail(email);
        if(existUser.isEmpty()){
            throw new UserNotFoundException("User with email "+email+" not found");
        }
        User user = existUser.get();
        if(!passwordEncoder.matches(password,user.getHashedPassword())){
            throw new WrongPassword("Wrong password");
        }

        // Check if the user already has two tokens
        List<Token> userTokens = tokenRepository.findByUserEmail(email);
        if (userTokens.size() >= 2) {
            throw new RuntimeException("User already has two tokens");
        }

        Token token = generateToken(user);
        Token savedToken = tokenRepository.save(token);
        return savedToken;

    }
    private Token generateToken(User user) {

            Token token = new Token();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime thirtyDaysFromNow = now.plusDays(30);
            Date ExpiryAt = Date.from(thirtyDaysFromNow.atZone(ZoneId.systemDefault()).toInstant());
            token.setExpiryAt(ExpiryAt);
            token.setValue(RandomStringUtils.randomAlphanumeric(128));
            token.setUser(user);
            return token;

    }
    public void logout (String token){
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeleted(token,false);
        if(optionalToken.isEmpty()){
            throw new TokenNotFoundException("Token not found");
        }
        Token token1=optionalToken.get();
        token1.setDeleted(true);
        tokenRepository.save(token1);

    }

    public User validateToken (String token){
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token,false, new Date());
        if(optionalToken.isEmpty()){
            throw new TokenNotFoundException("Token not found");
        }
        Token token1=optionalToken.get();
        return token1.getUser();

    }

}
