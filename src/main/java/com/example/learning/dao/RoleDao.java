package com.example.learning.dao;

import com.example.learning.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

    Role findByRoleName(String roleName);

}
