package com.App.Controller;

import com.App.Entity.User;
import com.App.Payloads.JWTTokenDto;
import com.App.Payloads.LoginDto;
import com.App.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    // http://localhost:8080/api/v1/auth/signup

  private UserService userService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> CreateUser(
            @RequestBody User user
    ){
        return userService.CreateUser(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> LoginUser(
            @RequestBody LoginDto loginDto
            ) {
        String tokens = userService.verifyLogin(loginDto);
        if (tokens!=null) {
            JWTTokenDto tokenDto = new JWTTokenDto();
            tokenDto.setToken(tokens);
            tokenDto.setTokenType("JWT");
            return new ResponseEntity<>(tokenDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("tokenDto i Invalid", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/message")
    public String getMessage(){
        return "hello";
    }
}
