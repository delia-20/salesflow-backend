package com.portafolio.zomtg.salesflow.security.service;

import com.portafolio.zomtg.salesflow.model.entities.Client;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {
    private  static final  String SECRET_KEY = "zomtg_secret_key_salesflow_256_bits_minimum_secure_123456_no_bug_no_cry";

    public JWTService() {
//        try {
//
//
//            KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = kg.generateKey();
//            SECRET_KEY = Base64.getEncoder().encodeToString(sk.getEncoded());
////            String secret = Base64.getEncoder().encodeToString(sk.getEncoded());
////            SECRET_KEY = "zomtg=secret=mind=no=bug=no=cry"+secret;
//
//        }catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }
    public String getToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",user.getRole().name());
        claims.put("ownerId",user.getOwnerId());
        claims.put("storeId",user.getStoreId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))//1 minuto
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(
                token, Claims::getExpiration
        ).before(new Date());
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        return claimsResolver.apply(claims);
    }

    public String getTokenClient(Client client) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",client.getRole().name());
        return
                Jwts.builder()
                        .setClaims(claims)
                        .setSubject(client.getUsername())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))//1 minuto
                        .signWith(getKey(),SignatureAlgorithm.HS256)
                        .compact();

        /*
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",user.getRole().name());
        claims.put("ownerId",user.getOwnerId());
        claims.put("storeId",user.getStoreId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))//1 minuto
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
         */
    }

    public String extractRole(String token) {
        return  extractClaims(token,claims -> claims.get("role",String.class));

    }
}
