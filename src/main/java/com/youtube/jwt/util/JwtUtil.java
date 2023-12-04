package com.youtube.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {


    private static final String  SECRET_KEY = "learn_programming_yourself";
    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);

    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver ){
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }


    public boolean validateToken (String token, UserDetails userDetails){
        String userName  = getUserNameFromToken(token);
        return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));

    }


    private boolean isTokenExpired(String token){
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());

    }


    private Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
