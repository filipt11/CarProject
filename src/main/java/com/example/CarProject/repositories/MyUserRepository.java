package com.example.CarProject.repositories;

import com.example.CarProject.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository <MyUser,Long> {
    Optional<MyUser>findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
