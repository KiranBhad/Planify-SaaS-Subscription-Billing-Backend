package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class SubscriptionValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // ✅ Skip filter for public and subscription-related endpoints
        if (
                path.contains("/api/auth") ||
                        path.contains("/api/plans/all") ||
                        path.contains("/api/auth/verify") ||
                        path.contains("/api/plans/subscribe") ||
                        path.contains("/swagger") || path.contains("/v3/api-docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            if (!"ADMIN".equals(user.getRole())) {
                Date expiry = user.getSubscriptionExpiry();
                if (expiry == null || expiry.before(new Date())) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Subscription Expired");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
