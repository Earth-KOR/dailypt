package com.dailyPT.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile({"local", "dev"})
public class DevelopBypassPreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

	public static final String DEVELOP_BYPASS_ACCOUNT_ID_ROLES = "X-BYPASS-ACCOUNT-ID-ROLES";

	public DevelopBypassPreAuthFilter(AuthenticationProvider authenticationProvider) {
		setCheckForPrincipalChanges(true);
		setAuthenticationManager(new ProviderManager(Collections.singletonList(authenticationProvider)));
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
		String bypassHeader = httpServletRequest.getHeader(DEVELOP_BYPASS_ACCOUNT_ID_ROLES);

		if (StringUtils.isBlank(bypassHeader)) {
			return null;
		} else {
			String[] token = bypassHeader.split("/");
			String role = token[1];
			return new ServiceAccountPreAuthenticationPrincipal(Long.parseLong(token[0]), role);
		}
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
