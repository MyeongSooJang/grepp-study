package com.grepp.jms.greppstudy.filter;


import com.grepp.jms.greppstudy.member.presentation.util.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final List<String> EXCLUDE_PATTERNS = List.of(
            "/api/member/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/**",
            "/actuator/**",
            "/api/authorizations/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        // Bearer 토큰이 없으면 다음 필터로 (로그인 페이지 등)
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증
        String token = bearerToken.substring(7);  // "Bearer " 제거
        Claims claims = jwtProvider.validateToken(token);
        String id = claims.getSubject();

        // Spring Security에 인증 정보 등록 (이게 없으면 401 발생!)
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                id, null, Collections.emptyList());  // TODO: role 기반 권한 추가
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return EXCLUDE_PATTERNS.stream().anyMatch(pattern ->pattern.matches(requestURI));
    }
}
