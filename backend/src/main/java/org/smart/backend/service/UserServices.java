package org.smart.backend.service;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.smart.backend.entity.Users;
import org.smart.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    UserRepo userRepo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public String verify(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                ));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "fail";
    }
    @PostConstruct
    public void seedUsers() {
        if (userRepo.count() == 0) {
            List<Users> users = List.of(
                    new Users(null, "admin", encoder.encode("admin123"), "ADMIN"),
                    new Users(null, "user", encoder.encode("user123"), "USER")
            );
            userRepo.saveAll(users);
        }
    }
}
