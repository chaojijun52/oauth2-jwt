package com.example.Oauth2ResourceServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Oauth2ResourceServer.entity.OauthSettings;
import com.example.Oauth2ResourceServer.repository.OauthSettingsRepository;

@Controller
@RequestMapping(path="/app")
public class AppSettings {
	@Autowired
	private OauthSettingsRepository oauthSettingsRepository;
@PostMapping(path="/client/add")
public @ResponseBody String addClient(@RequestBody OauthSettings setting) {
	setting.setType((byte)0);
	setting.setGrantTypes("password,refresh_token,client_credentials");
	setting.setAuthorities("ROLE_CLIENT,ROLE_TRUSTED_CLIENT");
	setting.setScopes("read,write,trust");
	this.oauthSettingsRepository.save(setting);
	return "saved";
}
}
