package com.kaws26.inventoryManagementSystem.security;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token=getUsernameFromToken(request);

        if(token!=null){
            String email=jwtUtils.getUsernameFromToken(token);
            UserDetails userDetails=customUserDetailsService.loadUserByUsername(email);

            if(StringUtils.hasText(email) && jwtUtils.isTokenValid(token,userDetails)){
                log.info("Valid Token, {}",email);

                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            }
        }

        try{
            filterChain.doFilter(request,response);
        }catch (Exception e) {
            log.error("Exception occurred in AuthFilter: " + e.getMessage());
        }
    }

    private String getUsernameFromToken(HttpServletRequest request){
        String token=request.getHeader("Authorization");
        if(token!=null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;

    }
}

