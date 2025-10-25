package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.Ecom.EcomEngineAPI.entity.User u = userRepository.findByEmail(email);
                //.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Spring Security wants ROLE_ prefix on authorities
        String roleName = "ROLE_" + (u.getRole() != null ? u.getRole().name() : "USER");

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPassword(),
                u.isActive(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(roleName))
        );
    }
}
