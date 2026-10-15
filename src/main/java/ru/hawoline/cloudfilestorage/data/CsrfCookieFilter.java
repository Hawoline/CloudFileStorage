package ru.hawoline.cloudfilestorage.data;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Извлекаем токен (это заставляет Spring Security сгенерировать Cookie)
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            csrfToken.getHeaderName();// Опционально: можно явно продублировать его в заголовок ответа
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        
        filterChain.doFilter(request, response);
    }
}
