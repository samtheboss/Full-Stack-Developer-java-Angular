package org.smart.backend.controller;


import java.util.Map;

import org.smart.backend.service.UserServices;
import org.smart.backend.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    UserServices userServices;
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Users users) {
    System.out.println(users.getUsername()+" "+users.getPassword());
        String token = userServices.verify(users);
        return Map.of("jwt_token", token);
    }
}
