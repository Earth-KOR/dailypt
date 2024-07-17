package com.dailyPT.backend.config;

import com.dailyPT.backend.common.exception.GradeChangeException;
import com.dailyPT.backend.common.exception.JwtTokenNotValidateException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Order(2)
@Component
@ConditionalOnBean(ServiceAuthProvider.class)
public class ServiceAccountPreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

	private static final String JWT_SCHEME_FORMAT = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;

	public ServiceAccountPreAuthFilter(ServiceAuthProvider authenticationProvider, JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
		setCheckForPrincipalChanges(true);
		setAuthenticationManager(new ProviderManager(Collections.singletonList(authenticationProvider)));
	}

	private boolean isValidTokenFormat(String token) {
		return token != null && token.startsWith(JWT_SCHEME_FORMAT);
	}

	private String resolveToken(HttpServletRequest req) {
		return req.getHeader(Headers.AUTHORIZATION.getKey());
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {

		String token = resolveToken(httpServletRequest);
		if(!isValidTokenFormat(token)) { return null; }
		token = token.replace(JWT_SCHEME_FORMAT, "");
		if (!jwtTokenProvider.validateToken(token)) {
			throw new JwtTokenNotValidateException();
		}
		if (!jwtTokenProvider.validateRoleOfUser(token)) {
			throw new GradeChangeException();
		}
		String userId = jwtTokenProvider.getUserId(token);
		String role = jwtTokenProvider.getUserRoles(token);
		return new ServiceAccountPreAuthenticationPrincipal(Long.parseLong(userId), role);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		return StringUtils.EMPTY;
	}

	@Override
	protected boolean principalChanged(HttpServletRequest request, Authentication currentAuthentication) {
		if (currentAuthentication instanceof ServiceAuthenticationToken) {
			return false;
		}

		return super.principalChanged(request, currentAuthentication);
	}
}
