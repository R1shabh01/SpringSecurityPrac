package com.example.Practice.Service;

import com.example.Practice.Model.Users;
import com.example.Practice.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder encoder ;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JWTService jwtService;

    public void register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);

    }
    public String verify(Users users){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(users.getUsername());
        }else {
            return "fail";
        }
    }
}
