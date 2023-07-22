/**
 * @author :  Dinuth Dheeraka
 * Created : 7/22/2023 4:40 PM
 */
package com.example.demo.config;

import com.example.demo.constant.OAuth2Constant;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    private final UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        final Map<String, Object> additionalInfo = new HashMap<>();

        User user = (User) authentication.getPrincipal();

//        UsernamePasswordAuthenticationToken authenticationToken =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (user.getUsername().equals("dinuth@gmail.com")) {
            UserEntity byUserName = userRepository.findByUserName(user.getUsername());
            additionalInfo.put("user", byUserName);
            additionalInfo.put("user_id", byUserName.getId());
        }

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        // set custom claims
        return super.enhance(accessToken, authentication);
    }
}
