package com.example.Oauth2ResourceServer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.Oauth2ResourceServer.entity.OauthSettings;

public interface OauthSettingsRepository extends CrudRepository<OauthSettings, Long> {
	List<OauthSettings> findAllByType(Byte type);
}
