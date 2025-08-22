package com.project.moneyManager.MoneyManager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class Jwtutils {
    private static final Key key= Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long expirationTime=60*60*1000;
    public  String generatetoken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();

    }

    public  boolean validateToken(String token){
      try{
          Jwts.parserBuilder()
                  .setSigningKey(key)
                  .build()
                  .parseClaimsJws(token);
          return  true;
      }catch (Exception e){
return false;
      }
    }
public   String getEmailFromToken(String token){
     Claims claims= Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

     return claims.getSubject();
}

}
