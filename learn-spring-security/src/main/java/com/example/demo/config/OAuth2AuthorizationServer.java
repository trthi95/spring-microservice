package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

	private String clientid = "client";
	private String clientSecret = "secret";
	private String privateKey = "private key";
	private String publicKey = "public key";

	@Autowired
    private DataSource dataSource;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

//	@Bean
//	public JwtAccessTokenConverter tokenEnhancer() {
//		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		converter.setSigningKey(privateKey);
//		converter.setVerifierKey(publicKey);
//		return converter;
//	}
//
//	@Bean
//	public JwtTokenStore tokenStore() {
//		return new JwtTokenStore(tokenEnhancer());
//	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.jdbc(dataSource).withClient(clientid).secret(passwordEncoder.encode(clientSecret))
//				.authorizedGrantTypes("password", "authorization_code", "refresh_token").authorities("role_user")
//				.scopes("read", "write")
//				.accessTokenValiditySeconds(300).refreshTokenValiditySeconds(240000);
		
		clients.inMemory().withClient("client")
		.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
		.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "USER").scopes("read", "write").autoApprove(true)
		.secret(passwordEncoder.encode("secret"));
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore());
	}
	
	
	@Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
