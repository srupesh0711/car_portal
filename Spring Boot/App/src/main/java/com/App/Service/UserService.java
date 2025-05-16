package com.App.Service;

import com.App.Entity.User;
import com.App.Payloads.LoginDto;
import com.App.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private JWTService jwtService;


    public UserService(UserRepository userRepository, JWTService jwtService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> CreateUser(User user){
        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("already exists by username", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent()){
            return new ResponseEntity<>("already exists by email",HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        String encodePassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodePassword);
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(hashpw);

        User save = userRepository.save(user);
        return new ResponseEntity<>("created SuccessFully", HttpStatus.CREATED);
    }


    public String verifyLogin(
            LoginDto loginDto
    ){
        Optional<User> opUsername = userRepository.findByUsername(loginDto.getUsername());
        if(opUsername.isPresent()){
            User user = opUsername.get();
            if(BCrypt.checkpw(loginDto.getPassword(),user.getPassword())){
                String token = jwtService.generateToken(user.getUsername());
                return token;
            }else {
                return null;
            }
        }
        return null;
    }
}
