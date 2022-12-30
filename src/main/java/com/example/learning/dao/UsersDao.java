package com.example.learning.dao;

import com.example.learning.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsersDao  extends JpaRepository<Users, Integer> {

    public Optional<Users> findByUserName(String username);
    Boolean existsByUserName(String username);
}
