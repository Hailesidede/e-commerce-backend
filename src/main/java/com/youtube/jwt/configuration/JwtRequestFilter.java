package com.youtube.jwt.configuration;

import com.youtube.jwt.entity.User;
import com.youtube.jwt.service.JwtService;
import com.youtube.jwt.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // This class is used to check if the token received is valid or not.

    public static String CURRENT_USER = "";

    private final JwtUtil jwtUtil;
    private final jwtServiceInterface jwtService;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, @Lazy JwtService jwtService) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
    }





    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        String jwtToken = null;
        String userName = null;

        if (header !=null && header.startsWith("Bearer ")){
            jwtToken = header.substring(7);

            try {
               userName = jwtUtil.getUserNameFromToken(jwtToken);
               CURRENT_USER = userName;


            }catch (IllegalArgumentException e){
                System.out.println("Unable To get JWT Token");
            }catch (ExpiredJwtException e){
                System.out.println("JWT Token provided is Exipred");
            }
        }else {
            System.out.println("jwt does not Start with bearer");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = jwtService.loadUserByUsername(userName);

            if (jwtUtil.validateToken(jwtToken, userDetails)){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }

        filterChain.doFilter(request, response);

    }
}
