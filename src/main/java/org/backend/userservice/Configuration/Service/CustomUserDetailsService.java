package org.backend.userservice.Configuration.Service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.backend.userservice.Configuration.Models.CustomUserDetails;
import org.backend.userservice.Models.User;
import org.backend.userservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@JsonDeserialize
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;
    public CustomUserDetailsService(){

    }

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User not found with email "+username);
        }
        User user = optionalUser.get();
        return new CustomUserDetails(user);

    }
}
