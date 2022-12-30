package com.example.learning.controllers;

import com.example.learning.dao.RoleDao;
import com.example.learning.dao.UsersDao;
import com.example.learning.dto.LoginCredentials;
import com.example.learning.dto.SignUpDTO;
import com.example.learning.models.Role;
import com.example.learning.models.Users;
import com.example.learning.security.JWTUtil;
import com.example.learning.security.MyUserDetailsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersDao usersDao;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;


    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<String> registerHandler(@RequestBody SignUpDTO signUpDTO){
        String encodedPass = passwordEncoder.encode(signUpDTO.getPassword());

        Set<Role> roleset = new HashSet<Role>();
        Role role =  roleDao.findByRoleName("ROLE_USER");
        roleset.add(role);
        Users user = new Users();
        BeanUtils.copyProperties(signUpDTO, user);

        user.setRoles(roleset);
        user.setPassword(encodedPass);
        user = usersDao.save(user);

        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUserName(), body.getPassword());

            authManager.authenticate(authInputToken);

          UserDetails userDetails =  myUserDetailsService.loadUserByUsername(body.getUserName());

            String token = jwtUtil.generateToken(userDetails);

            return Collections.singletonMap("jwt-token", token);
        }catch (AuthenticationException authExc){
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> sayHello(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }
}
