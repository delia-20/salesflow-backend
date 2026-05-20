package com.portafolio.zomtg.salesflow.security.controller;

import com.portafolio.zomtg.salesflow.users.enums.Role;
import com.portafolio.zomtg.salesflow.security.service.JWTService;
import com.portafolio.zomtg.salesflow.security.service.UserServiceDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserServiceDetails userServiceDetails;

    public JwtFilter(JWTService jwtService, UserServiceDetails userServiceDetails) {
        this.jwtService = jwtService;
        this.userServiceDetails = userServiceDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token =authHeader.substring(7);
        String username =jwtService.extractUsername(token);
        String role= jwtService.extractRole(token);
        if(username !=null
            && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails;
            if(role.equals(Role.CUSTOMER.name())){
                userDetails = userServiceDetails.loadUserByUsernameClients(username);
            }else if (role.equals(Role.OWNER.name()) || role.equals(Role.EMPLOYEE.name())){
                userDetails = userServiceDetails.loadUserByUsername(username);
            }else {
                userDetails = userServiceDetails.loadUserByUsername(username);
            }

            // userDetails = userServiceDetails.loadUserByUsername(username);
            if(jwtService.isValidToken(token,userDetails)){
                UsernamePasswordAuthenticationToken auth=
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }

}
