package org.smart.backend.repository;


import java.util.List;

import org.smart.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);
    List<Users> findAll();
}
