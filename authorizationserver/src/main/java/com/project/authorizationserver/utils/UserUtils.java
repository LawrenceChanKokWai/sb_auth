package com.project.authorizationserver.utils;

import com.project.authorizationserver.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;

public class UserUtils {

    public static User getUser(Authentication authentication) {
        if(authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken) {
            var usernamePasswordAuthentication = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
            return (User) usernamePasswordAuthentication.getPrincipal();
        }
        return (User) authentication.getPrincipal();
    }

}
