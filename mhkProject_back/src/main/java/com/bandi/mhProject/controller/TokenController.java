package com.bandi.mhProject.controller;

import com.bandi.mhProject.util.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.Optional.ofNullable;
//@Controller
public class TokenController {

    private final TokenStore tokenStore;

    @Autowired
    public TokenController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PostMapping("/revokeToken")
//    public void revokeToken(Authentication authentication) {
//        ofNullable(authentication).ifPresent(auth -> {
//            OAuth2AccessToken accessToken = tokenStore.getAccessToken((OAuth2Authentication) auth);
//
//            ofNullable(accessToken).ifPresent(oAuth2AccessToken -> {
//                ofNullable(oAuth2AccessToken.getRefreshToken()).ifPresent(tokenStore::removeRefreshToken);
//                tokenStore.removeAccessToken(accessToken);
//            });
//        });
//    }

}