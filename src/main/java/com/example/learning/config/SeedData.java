package com.example.learning.config;

import com.example.learning.dao.RoleDao;
import com.example.learning.dao.UsersDao;
import com.example.learning.models.Role;
import com.example.learning.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UsersDao usersDao;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");

        if(roleDao.findAll().isEmpty()) {
           roleDao.save(adminRole);
           roleDao.save(userRole);
        }

        Users adminUser = new Users();
        Users normalUser = new Users();

        Set<Role> role1 = new HashSet<>();
        Set<Role> role2 = new HashSet<>();

        Role role =  roleDao.findByRoleName("ROLE_USER");
        Role adminrole =  roleDao.findByRoleName("ROLE_ADMIN");

        role1.add(role);
        role2.add(adminrole);

        adminUser.setUserName("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setEnabled(true);
        adminUser.setRoles(role2);

        normalUser.setUserName("user");
        normalUser.setPassword(passwordEncoder.encode("user"));
        normalUser.setEnabled(true);
        normalUser.setRoles(role1);

        if(usersDao.findAll().isEmpty()) {
            usersDao.save(adminUser);
            usersDao.save(normalUser);
        }


    }
}
