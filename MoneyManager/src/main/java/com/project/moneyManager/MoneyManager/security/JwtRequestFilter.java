package com.project.moneyManager.MoneyManager.security;

import com.project.moneyManager.MoneyManager.service.AppUserDetailsService;
import com.project.moneyManager.MoneyManager.util.Jwtutils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AppUserDetailsService appUserDetailsService;
    private final Jwtutils jwtutils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        String email=null,jwt=null;
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            jwt=authHeader.substring(7);
            email=jwtutils.getEmailFromToken(jwt);
        }
        if (email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= this.appUserDetailsService.loadUserByUsername(email);

            if (jwtutils.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authtoken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
