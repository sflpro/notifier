package com.sflpro.notifier.api.facade.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/27/19
 * Time: 11:10 AM
 */
public abstract class AutoAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final Authentication authentication = tokenExtractor().extract(request);
        if (authentication != null) {
            authentication.setAuthenticated(true);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    protected abstract TokenExtractor tokenExtractor();


}
