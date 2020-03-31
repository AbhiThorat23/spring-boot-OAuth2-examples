package com.codelabs.authserver.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class AuthorizationServerConfiguration implements AuthorizationServerConfigurer {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * Spring Security OAuth exposes two endpoints for checking tokens
	 * (/oauth/check_token and /oauth/token_key). Those endpoints are not exposed by
	 * default (have access "denyAll()").
	 * 
	 * Here we are configuring "/oauth/check_token" -> to be Authenticated.
	 * and "/oauth/token_key" -> Access to this url is permitted to all.
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");
	}

	/**
	 *Client(Client means OAuth client app) details configuration is stored in database which is encoded as well.
	 *If we want to store inMemory clientId and there secrete then we can do that here as well.
	 *But best way is to store all this secrete and clientId in database in encrypted format.
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource).passwordEncoder(passwordEncoder);

	}

	/**
	 *If we do not write "endpoints.tokenStore(jdbcTokenStore());" this code
	 *then after restarting the service we can not used old AccessToken for calling resource server.
	 *
	 *So we have to store that token so that after restart of the service as well we can use the same.
	 *That is why we have used JdbcTokenStore() here provided by spring boot OAuth
	 *
	 *NOTE: If we are using in memory database like H2 in this case, then this tokenStore() to store token in database 
	 *will not be useful because after restarting the service new schema will get created and everything will be vanished.
	 *So, This is useful in real time environment/database like MySql but not with h2 database.
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(jdbcTokenStore());
		endpoints.authenticationManager(authenticationManager);

	}

	@Bean
	TokenStore jdbcTokenStore() {
		return new JdbcTokenStore(dataSource);
	}

}
