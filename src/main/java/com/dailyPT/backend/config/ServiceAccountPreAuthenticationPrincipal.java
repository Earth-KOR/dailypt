package com.dailyPT.backend.config;

import lombok.Getter;

@Getter
public class ServiceAccountPreAuthenticationPrincipal {

	private Long userId;

	private String role;

	private boolean isAdmin;

	public ServiceAccountPreAuthenticationPrincipal(Long userId, String roles, boolean isAdmin) {
		this.userId = userId;
		this.role = roles;
		this.isAdmin = isAdmin;
	}

	public ServiceAccountPreAuthenticationPrincipal(Long userId, String role) {
		this(userId, role, false);
	}
}
