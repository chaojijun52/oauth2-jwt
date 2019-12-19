package com.example.Oauth2AuthorizationServer.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.Oauth2AuthorizationServer.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}
