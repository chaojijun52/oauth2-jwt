package com.example.Oauth2AuthorizationServer.config;

import java.sql.Types;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

public class CustomJdbcTokenStore extends JdbcTokenStore {
	private static final Log LOG = LogFactory.getLog(JdbcTokenStore.class);
	private static final String DEFAULT_ACCESS_TOKEN_INSERT_STATEMENT = "insert into oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) values (?, ?, ?, ?, ?, ?, ?)";
	private String insertAccessTokenSql = DEFAULT_ACCESS_TOKEN_INSERT_STATEMENT;
	
	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	private final JdbcTemplate jdbcTemplate;

	public CustomJdbcTokenStore(DataSource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		System.out.println("June testing...readAccessToken");
	    OAuth2AccessToken accessToken = null;

	    try {
	        accessToken = new DefaultOAuth2AccessToken(tokenValue);
	    }
	    catch (EmptyResultDataAccessException e) {
	        if (LOG.isInfoEnabled()) {
	            LOG.info("Failed to find access token for token "+tokenValue);
	        }
	    }
	    catch (IllegalArgumentException e) {
	        LOG.warn("Failed to deserialize access token for " +tokenValue,e);
	        removeAccessToken(tokenValue);
	    }

	    return accessToken;
	}
	
	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		String refreshToken = null;
		if (token.getRefreshToken() != null) {
			refreshToken = token.getRefreshToken().getValue();
		}
		
		if (readAccessToken(token.getValue())!=null) {
			removeAccessToken(token.getValue());
		}

		jdbcTemplate.update(insertAccessTokenSql, new Object[] { "123456",//extractTokenKey(token.getValue()),
				new SqlLobValue(serializeAccessToken(token)), authenticationKeyGenerator.extractKey(authentication),
				authentication.isClientOnly() ? null : authentication.getName(),
				authentication.getOAuth2Request().getClientId(),
				new SqlLobValue(serializeAuthentication(authentication)), extractTokenKey(refreshToken) }, new int[] {
				Types.VARCHAR, Types.BLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.VARCHAR });
	}

}
