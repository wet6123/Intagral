package com.a304.intagral.common.auth;

import com.a304.intagral.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
	@Autowired
	User user;
	boolean accountNonExpired;
	boolean accountNonLocked;
	boolean credentialNonExpired;
	boolean enabled = false;
	List<GrantedAuthority> roles = new ArrayList<>();

	public UserDetails(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}
	public Long getId() {
		return this.user.getId();
	}
	public String getEmail() {
		return this.user.getEmail();
	}
	public String getNickname() {
		return this.user.getNickname();
	}
	public String getProfileImgPath() {
		return this.user.getProfileImgPath();
	}
	public String getIntro() {
		return this.user.getIntro();
	}
	public String getAccessToken() {
		return this.user.getAccessToken();
	}
	public String getRefreshToken() {
		return this.user.getRefreshToken();
	}
	public String getAuthToken() {
		return this.user.getAuthToken();
	}
	public String getOauthPlatform() {
		return this.user.getOauthPlatform();
	}
	@Override
	public String getUsername() {
		return this.user.getId().toString();
	}
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialNonExpired;
	}
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {
		return null;
	}

	public void setAuthorities(List<GrantedAuthority> roles) {
		this.roles = roles;
	}
}