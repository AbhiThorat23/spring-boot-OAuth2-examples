package com.codelabs.authserver.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codelabs.authserver.models.CustomUserDetails;
import com.codelabs.authserver.models.User;
import com.codelabs.authserver.repository.CustomUserDetailsRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomUserDetailsRepository userDetailsRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userDetailsRepository.findByUsername(username);
		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
		
		//Below line using java 8 -> usersOptional.map(CustomUserDetails::new).get();
		UserDetails userDetails = new CustomUserDetails(optionalUser.get());
		
		//Check User's account status (isAccountNonLocked, isEnabled, isCredentialsNonExpired etc)
		new AccountStatusUserDetailsChecker().check(userDetails);
		
		return userDetails;

	}

}
