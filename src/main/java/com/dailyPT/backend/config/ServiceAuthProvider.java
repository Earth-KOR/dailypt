package com.dailyPT.backend.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
public class ServiceAuthProvider implements AuthenticationProvider {

    private static final String INTERNAL_REST_API_AUTH_KEY = "SERVER_KEY";


    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication.getPrincipal() instanceof ServiceAccountPreAuthenticationPrincipal) {
            return getAccountAuthentication(authentication);
        }

        if (authentication.getPrincipal() instanceof String) {
            return getServiceKeyAuthentication(authentication);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(clazz);
    }

    private Authentication getServiceKeyAuthentication(Authentication authentication) {
        String serviceKey = (String) authentication.getPrincipal();
        if (!StringUtils.hasLength(serviceKey) || !INTERNAL_REST_API_AUTH_KEY.equals(serviceKey)) {
            return null;
        }
        return new ServiceAuthenticationToken(serviceKey, authentication.getDetails(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + serviceKey)));
    }

    private Authentication getAccountAuthentication(Authentication authentication) {
        ServiceAccountPreAuthenticationPrincipal principal = (ServiceAccountPreAuthenticationPrincipal) authentication.getPrincipal();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + principal.getRole()));

        if (principal.isAdmin()) {
            ServiceAuthenticationToken serviceAuthenticationToken = new ServiceAuthenticationToken(principal.getUserId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(serviceAuthenticationToken);
            return serviceAuthenticationToken;
        }
        ServiceAuthenticationToken serviceAuthenticationToken = new ServiceAuthenticationToken(principal.getUserId(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(serviceAuthenticationToken);
        return serviceAuthenticationToken;
    }
}
