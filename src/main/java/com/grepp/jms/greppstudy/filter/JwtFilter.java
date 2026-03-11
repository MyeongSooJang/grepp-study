package com.grepp.jms.greppstudy.filter;


import com.grepp.jms.greppstudy.member.presentation.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getContextPath().startsWith("/api/member/login") &&
                request.getMethod().equals(HttpMethod.POST.name())) {
        } else {
            String bearerToken = request.getHeader("Authorization");
            String token = bearerToken.substring("Bearer ".length());
            Claims claims = jwtProvider.validateToken(token);  // 변경: Jws<Claims> → Claims
            String id = claims.getSubject();  // 변경: .getPayload() 제거
            //TODO: id를 가지고 role 테이블에서 권한 체크 후 API 호출 되도록 해주세요.
            filterChain.doFilter(request, response);
        }
    }


}
