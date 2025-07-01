package br.com.yugitilidades.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final String jwtSecret;

    public JwtAuthenticationFilter(@Value("${yugitilidades.jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();
        log.info("JwtAuthenticationFilter: Processing request for path: {}", path);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("JwtAuthenticationFilter: Found Bearer token");
            try {
                Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
                DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
                String role = jwt.getClaim("role").asString();
                log.info("JwtAuthenticationFilter: Token role claim: {}", role);
                if (path.contains("/admin")) {
                    if (!"ADMIN".equals(role)) {
                        log.warn("JwtAuthenticationFilter: Insufficient role for admin endpoint: {}", role);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Insufficient role for admin endpoint");
                        return;
                    }
                } else if (path.contains("/cards")) {
                    if (!"CARDS".equals(role) && !"ADMIN".equals(role)) {
                        log.warn("JwtAuthenticationFilter: Insufficient role for cards endpoint: {}", role);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Insufficient role for cards endpoint");
                        return;
                    }
                }
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken("jwtUser", null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException e) {
                log.error("JwtAuthenticationFilter: Invalid or expired JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        } else if (path.contains("/admin") || path.contains("/cards")) {
            log.warn("JwtAuthenticationFilter: Missing JWT token for protected endpoint: {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing JWT token");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
