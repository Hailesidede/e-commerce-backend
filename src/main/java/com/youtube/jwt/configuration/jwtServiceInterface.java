package com.youtube.jwt.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface jwtServiceInterface  {
    UserDetails loadUserByUsername(String username);

}
 