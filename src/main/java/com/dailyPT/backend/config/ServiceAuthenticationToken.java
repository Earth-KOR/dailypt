package com.dailyPT.backend.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ServiceAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public ServiceAuthenticationToken(Object principal, Object details, Collection<? extends GrantedAuthority> authorities) {
		super(principal, null, authorities);
		setDetails(details);
	}
}
