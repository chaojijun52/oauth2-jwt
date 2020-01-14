package com.example.Oauth2AuthorizationServer.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Oauth2AuthorizationServer.entity.User;
import com.example.Oauth2AuthorizationServer.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=this.userRepository.findByUsername(username);
		
		if(user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", username));
        }
		List<GrantedAuthority> authorities = new ArrayList<>();
//        for(String role : user.getRoles().split(",")) {
//            authorities.add(new SimpleGrantedAuthority(role));
//        };
		authorities.add(new SimpleGrantedAuthority("USER"));

//        System.out.println("username: "+user.getUsername()+", password: "+user.getPassword()+", roles: "+user.getRoles());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), authorities);
		return userDetails;
	}

}
