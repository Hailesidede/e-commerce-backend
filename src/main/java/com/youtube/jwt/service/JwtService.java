package com.youtube.jwt.service;

import com.youtube.jwt.configuration.jwtServiceInterface;
import com.youtube.jwt.dao.UserDao;
import com.youtube.jwt.entity.JwtRequest;
import com.youtube.jwt.entity.JwtResponse;
import com.youtube.jwt.entity.User;
import com.youtube.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService, jwtServiceInterface {

    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception{
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();

        authenticate(userName,userPassword);

        final UserDetails userDetails = loadUserByUsername(userName);

        String newGeneratedJwtToken = jwtUtil.generateToken(userDetails);

        User user = userDao.findById(userName).get();

        return new JwtResponse(user, newGeneratedJwtToken);

    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).get();

        if (user != null){
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthorities(user));

        }else {
            throw new UsernameNotFoundException("Username not found");

        }
    }


    private Set getAuthorities(User user){
        Set authorities = new HashSet();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception{
       try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,userPassword));
       }catch (DisabledException e){
           throw new Exception("User is Disabled");
       }catch (BadCredentialsException e){
           throw new Exception("Bad Credentials From User");
       }

    }
}
