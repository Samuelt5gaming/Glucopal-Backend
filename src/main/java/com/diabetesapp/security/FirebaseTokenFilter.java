package com.diabetesapp.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/** Verifies "Authorization: Bearer <Firebase ID token>" and populates the security context. */
@Component
@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(FirebaseTokenFilter.class);

    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                FirebaseToken decoded = firebaseAuth.verifyIdToken(token);
                FirebasePrincipal principal = new FirebasePrincipal(
                        decoded.getUid(),
                        decoded.getEmail(),
                        decoded.getName(),
                        decoded.isEmailVerified(),
                        decoded.getPicture());
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, List.of()));
            } catch (FirebaseAuthException | IllegalArgumentException e) {
                log.debug("Rejected Firebase token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
