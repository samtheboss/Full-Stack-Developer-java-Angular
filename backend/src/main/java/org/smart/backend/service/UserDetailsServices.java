package org.smart.backend.service;

import org.smart.backend.entity.UserPrincipal;
import org.smart.backend.entity.Users;
import org.smart.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServices implements UserDetailsService {
    @Autowired
    UserRepo repo ;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);
        if (user ==null){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
    return new UserPrincipal(user);
    }
}
