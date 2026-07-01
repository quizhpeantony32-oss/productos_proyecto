package com.productos.productos.security;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class ApiTokenFilter implements Filter {

    private static final String HEADER_NAME = "X-API-TOKEN";
    private static final String TOKEN = "111024";
    private static final Set<String> PROTECTED_METHODS = Set.of("POST", "PUT", "DELETE");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        boolean isApiPath = path.startsWith("/api/");
        boolean needsToken = isApiPath && PROTECTED_METHODS.contains(method);

        if (!needsToken) {
            chain.doFilter(request, response);
            return;
        }

        String incomingToken = httpRequest.getHeader(HEADER_NAME);
        if (TOKEN.equals(incomingToken)) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"message\":\"Token invalido o ausente\"}");
    }
}
