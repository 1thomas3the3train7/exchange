package com.udsu.auth.utils;


import com.google.gson.Gson;
import com.udsu.auth.exception.IncorrectJwtTokenException;
import com.udsu.auth.exception.NotValidRequestException;
import com.udsu.auth.model.auth.JwtCredentials;
import com.udsu.auth.model.auth.Role;
import com.udsu.auth.model.auth.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtUtils {
    private final SecretKey jwtAccessToken;
    private final SecretKey jwtRefreshToken;
    private final Gson gson = new Gson();

    public JwtUtils(@Value("${jwt.accessToken}")String jwtAccessToken, @Value("${jwt.refreshToken}") String jwtRefreshToken) {
        this.jwtAccessToken = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessToken));
        this.jwtRefreshToken = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshToken));
    }
    public String generateAccessToken(final User userDTO){
        final LocalDateTime time = LocalDateTime.now();
        final Instant accessInst = time.plusMinutes(100).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessTime = Date.from(accessInst);
        return Jwts.builder()
                .setSubject(userDTO.getEmail())
                .setExpiration(accessTime)
                .claim("roles",gson.toJson(userDTO.getRoles()))
                .claim("type","access")
                .signWith(jwtAccessToken)
                .compact();
    }
    public String generateRefreshToken(final User userDTO){
        final LocalDateTime time = LocalDateTime.now();
        final Instant refreshInst = time.plusDays(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshTime = Date.from(refreshInst);
        return Jwts.builder()
                .setSubject(userDTO.getEmail())
                .setExpiration(refreshTime)
                .claim("roles",gson.toJson(userDTO.getRoles()))
                .claim("type","refresh")
                .signWith(jwtRefreshToken)
                .compact();
    }
    public boolean validateAccessToken(final String accessToken){
        try {
            final Claims claims = getAccessClaims(accessToken);
            if(claims == null){
                return false;
            }
            if(!((String) claims.get("type")).equals("access")){
                return false;
            }
            return validateToken(accessToken,jwtAccessToken);
        } catch (NullPointerException n){
            System.out.println(n.getMessage());
            return false;
        } catch (ExpiredJwtException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean validateRefreshToken(final String refreshToken){
        final Claims claims = getAccessClaims(refreshToken);
        if(!((String) claims.get("type")).equals("refresh")){
            throw new IncorrectJwtTokenException("Incorrect token type");
        }
        return validateToken(refreshToken,jwtRefreshToken);
    }
    private boolean validateToken(final String typeToken,final Key key){
        try {
            if(typeToken == null || typeToken.equals("")){
                System.out.println("falsee");
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(typeToken);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.out.println("token expired");
        } catch (UnsupportedJwtException unsEx) {
            System.out.println(unsEx.getMessage());
        } catch (MalformedJwtException mjEx) {
            System.out.println(mjEx.getMessage());
        } /*catch (SignatureException sEx) {
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
        return false;
    }
    public Claims getAccessClaims(final String token) {
        return getClaims(token, jwtAccessToken);
    }

    public Claims getRefreshClaims(final String token) {
        return getClaims(token, jwtRefreshToken);
    }

    private Claims getClaims(final String token, final Key secret) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException m){
            System.out.println(m.getMessage());
            return null;
        }
    }
    public JwtCredentials parseClaims(final Claims claims){
        final JwtCredentials jwtCredentials = new JwtCredentials();
        jwtCredentials.setRoles(getRoles(claims));
        jwtCredentials.setEmail(claims.getSubject());
        return jwtCredentials;
    }
    private Role[] getRoles(final Claims claims){
        try {
            final Role[] roles = gson.fromJson((String) claims.get("roles"), Role[].class);
            return roles;
        } catch (NullPointerException n){
            throw new NotValidRequestException("Not valid request (null roles)");
        }
    }
    public User parseRefreshToken(final String refresh){
        final Claims claims = getRefreshClaims(refresh);
        return new User(claims.getSubject(), getRoles(claims));
    }
}
