package com.example.learning.security;

import com.example.learning.dao.UsersDao;
import com.example.learning.models.Role;
import com.example.learning.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MyUserDetailsService  implements UserDetailsService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Users> userRes = usersDao.findByUserName(userName);
        if(!userRes.isPresent()) {
            throw new UsernameNotFoundException("Could not findUser with email = " + userName);
        }
        Users user = userRes.get();
        return new User(user.getUserName(), user.getPassword(), mapRoleAuth(user.getRoles()) );
    }

    public  Set<GrantedAuthority> mapRoleAuth(Set<Role> roleSet) {
      return   roleSet.stream().map(  role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
    }
}
