package com.example.Oauth2ResourceServer.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.nimbusds.jose.util.IOUtils;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {
//	@Value("${security.signing-key}")
//    private String signingKey;

	@Value("${security.encoding-strength}")
	private Integer encodingStrength;

	@Value("${security.security-realm}")
	private String securityRealm;

//	@Autowired
//    private ResourceServerTokenServices tokenServices;
//
//    @Autowired
//    private JwtAccessTokenConverter accessTokenConverter;
//    
	@Override
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()
				.authorizeRequests().anyRequest().authenticated().and().httpBasic().realmName("CRM_REALM");
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// do not require a resource id in AccessToken.
		resources.resourceId(null);
		resources.tokenServices(tokenServices());
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();// new JwtAccessTokenConverter();
		Resource resource = new ClassPathResource("public.txt");
		String publicKey = null;
		try {
			InputStream inputStream=resource.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			publicKey = reader.lines().collect(Collectors.joining());//resource.getInputStream().toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
//        converter.setSigningKey(signingKey);
		System.out.println(
				"\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n===============================Public key========================================                "
						+ publicKey+"\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
//		publicKey = "-----BEGIN PUBLIC KEY-----"
//				+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlf/N+8VkgpZt7p+SgkMA"
//				+ "w7XDuB83AW9yeuqrDEbc+eai6L9AYAuxdYLRwLSMAzl5x9lDc0vNkEfGSuv8j3z/"
//				+ "b4ZcppnnmO9MS+QFUfEAR8/RtyCdAHJRwua9rAkqFeWr1zPyj2o56Fz83/4ivW4E"
//				+ "fAwXScVxaXBc1ccvTTKaeJcTa56b/8XqOFUNZKiI1ovOvm1J4avs+9zviUo1DD9E"
//				+ "D9ao9aOMCvs5OsH28M26FJqUgJaF4sTNf0so0kB944mhVr9HnAAs4VJoJgJsJTkR"
//				+ "jBl7Y2Jr7IrNJ7meTOSsQOFlSEAciiej2DtZxtyT395BN5L2FeJE4oDal+dnY17P"
//				+ "pwIDAQAB"
//				+ "-----END PUBLIC KEY-----";
		converter.setVerifierKey(publicKey);
		return converter;
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}
}
