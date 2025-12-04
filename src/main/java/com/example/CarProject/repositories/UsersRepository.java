package com.example.CarProject.repositories;

import com.example.CarProject.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository <Users,Long> {
}
