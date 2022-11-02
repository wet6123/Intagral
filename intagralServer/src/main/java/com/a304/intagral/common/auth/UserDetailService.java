package com.a304.intagral.common.auth;

import com.a304.intagral.db.entity.User;
import com.a304.intagral.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailService implements UserDetailsService{
	@Autowired
	UserService userService;

	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByUserName(username);
		if(user != null) {
			UserDetails userDetails = new UserDetails(user);
			return userDetails;
		}
		return null;
	}
}