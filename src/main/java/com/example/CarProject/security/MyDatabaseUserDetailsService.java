package com.example.CarProject.security;

import com.example.CarProject.entities.MyUser;
import com.example.CarProject.repositories.MyUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class MyDatabaseUserDetailsService implements UserDetailsService {
    private final MyUserRepository myUserRepository;

    public MyDatabaseUserDetailsService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        MyUser user = myUserRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);

    }

}
