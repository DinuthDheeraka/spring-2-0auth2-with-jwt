/**
 * @author :  Dinuth Dheeraka
 * Created : 7/22/2023 11:15 AM
 */
package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = null;
        try {
            userEntity = userRepository.findByUserName(s);
            return new User(userEntity.getUserName(), userEntity.getPassword(), new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User " + s + " was not found in the database");
        }
    }
}
