package com.example.Oauth2ResourceServer.config;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {
	@Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        DefaultOAuth2AccessToken fooAccessToken = new DefaultOAuth2AccessToken(accessToken);

        Map<String,Object> valueMap = new HashMap<>();
        valueMap.put("timestamp",new Timestamp(System.currentTimeMillis()));

        String name = authentication.getName();

        fooAccessToken.setAdditionalInformation(valueMap);

        return super.enhance(fooAccessToken,authentication);
    }
}
