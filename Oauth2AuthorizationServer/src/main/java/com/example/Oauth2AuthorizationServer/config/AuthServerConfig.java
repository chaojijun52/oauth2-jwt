package com.example.Oauth2AuthorizationServer.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder.ClientBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	private static String REALM = "CRM_REALM";

	private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
    private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

//	    clients.inMemory()
//	    .withClient("exampleClient")
//            .secret("{noop}exampleSecret")
//            .authorizedGrantTypes("password", "refresh_token","client_credentials")
//            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//            .scopes("read", "write", "trust")
//            .accessTokenValiditySeconds(300)
//            .refreshTokenValiditySeconds(THIRTY_DAYS);
		clients.jdbc(this.jdbcTemplate.getDataSource());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
				.authenticationManager(authenticationManager).reuseRefreshTokens(false).accessTokenConverter(accessTokenConverter);

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.realm(REALM);
//		oauthServer.checkTokenAccess("permitAll()");
		oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
	}
}
