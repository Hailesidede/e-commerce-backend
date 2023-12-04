package com.youtube.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.function.Function;

public class JwtUtilClone {

    private final String SECRETE_KEY = "Haile_Sidede";

    private String getUserNameFromToken(String token){

    }

    private <T>T getClaimsFromToken(String token, Function<Claims,T> claimResolver){
        Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);

    }


    private Claims getAllClaimsFromToken(String token){
       return Jwts.parser().setSigningKey(SECRETE_KEY).parseClaimsJws(token).getBody();
    }


    private boolean isTokenExpired(String token){

    }


    private boolean validateToken(String token){

    }


    private Date getExpirationDateFromToken(String token){
        return

    }
}
