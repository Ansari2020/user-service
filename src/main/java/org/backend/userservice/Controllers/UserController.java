package org.backend.userservice.Controllers;

import lombok.Getter;
import org.backend.userservice.Exceptions.TokenNotFoundException;
import org.backend.userservice.Exceptions.UserAlreadyExists;
import org.backend.userservice.Exceptions.UserNotFoundException;
import org.backend.userservice.Exceptions.WrongPassword;
import org.backend.userservice.Models.Token;
import org.backend.userservice.Models.User;
import org.backend.userservice.Services.UserService;
import org.backend.userservice.dtos.LogInRequestDto;
import org.backend.userservice.dtos.LogoutRequestDto;
import org.backend.userservice.dtos.SignUpRequestDto;
import org.backend.userservice.dtos.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signUpRequestDto) {

        try {
            User user=userService.signup(
                    signUpRequestDto.getEmail(),
                    signUpRequestDto.getName(),
                    signUpRequestDto.getPassword()
            );
            return new ResponseEntity<>(UserDto.from(user), HttpStatus.CREATED);
        }
        catch (UserAlreadyExists e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInRequestDto loginRequestDto) {
        try {
            Token token = userService.login(
                    loginRequestDto.getEmail(),
                    loginRequestDto.getPassword()
            );
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        }
        catch (WrongPassword e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Unexpected Error occurred", HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequestDto logoutRequestDto) {

        try{
            userService.logout(logoutRequestDto.getToken());
            return new ResponseEntity<>("User deleted ",HttpStatus.OK);
        }
        catch (TokenNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>("Unexpected Error occurred",HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {

        try {
            User user = userService.validateToken(token);
            return new ResponseEntity<>(UserDto.from(user), HttpStatus.OK);
        }
        catch (TokenNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

}
