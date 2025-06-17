package com.bookstorebe.bookstorebe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookstorebe.bookstorebe.entity.MyAppUser;
import com.bookstorebe.bookstorebe.repository.MyAppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor 
public class MyAppUserService implements UserDetailsService{
    
    @Autowired
    private MyAppUserRepository repository;
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyAppUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            MyAppUser userObj = user.get();
        
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
    
}