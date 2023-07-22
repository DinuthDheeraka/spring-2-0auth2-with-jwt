/**
 * @author :  Dinuth Dheeraka
 * Created : 7/22/2023 11:27 AM
 */
package com.example.demo.config;

import com.example.demo.constant.OAuth2Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig implements AuthorizationServerConfigurer {

    private final AuthenticationManager authenticationManager;
    private final CustomTokenEnhancer customTokenEnhancer;

    // This method is used to configure the security settings of the authorization server,
    // such as which endpoints are public and which require authentication.
    @Override
    public void configure(AuthorizationServerSecurityConfigurer securityConfigurer) throws Exception {
        securityConfigurer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    //This method is used to define the client details service, which is responsible
    // for managing information about registered OAuth2 clients
    // (e.g., client id, client secret, grant types, scopes).
    @Override
    public void configure(ClientDetailsServiceConfigurer clientConfigurer) throws Exception {
        clientConfigurer
                .inMemory()
                .withClient(OAuth2Constant.USER_CLIENT_ID)
                .secret(OAuth2Constant.CLIENT_SECRET)
                .scopes(OAuth2Constant.SCOPE_READ, OAuth2Constant.SCOPE_WRITE)
                .authorizedGrantTypes(OAuth2Constant.GRANT_TYPE_PASSWORD, OAuth2Constant.REFRESH_TOKEN)
                .accessTokenValiditySeconds(OAuth2Constant.ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(OAuth2Constant.REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    //This method allows you to configure various endpoints provided by the authorization server,
    // such as the token endpoint, authorization endpoint, user-info endpoint, and more.
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception {

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(enhancer(), tokenEnhancer()));

        endpointsConfigurer
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenEnhancer(enhancerChain)
                .accessTokenConverter(tokenEnhancer());
    }

    //JwtAccessTokenConverter is a class provided by Spring Security that performs the conversion
    // between JWT format and OAuth2 tokens. It handles the encoding and decoding of JWT tokens.
    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {

        //By creating a JwtAccessTokenConverter bean and configuring it with the signing and
        // verifier keys,
        // you enable your authorization server to issue access tokens in JWT format.

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(OAuth2Constant.TOKEN_SIGN_IN_KEY);
        converter.setVerifierKey(OAuth2Constant.TOKEN_SIGN_IN_KEY);
        return converter;
    }

    //By creating the JwtTokenStore bean, you enable your
    // authorization server to store and manage access tokens in JWT format.

    // When a client requests an access token, the authorization server issues a
    // JWT-based token using the JwtTokenStore. The client can then use this token to
    // access protected resources directly, and the resource server (protected resource)
    // can verify the token's validity using the same JwtTokenStore
    // and the JwtAccessTokenConverter with the appropriate signing and verifier keys.
    @Bean
    public JwtTokenStore tokenStore() {

        //The JwtTokenStore relies on the JwtAccessTokenConverter
        // (obtained from the tokenEnhancer() method) to decode and verify the JWT tokens.
        return new JwtTokenStore(tokenEnhancer());
    }

    @Bean
    public TokenEnhancer enhancer(){
        return customTokenEnhancer;
    }
}
