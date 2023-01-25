package com.sergio.auth.backend.resources.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ResourceController {

    private static final GrantedAuthority GOLD_CUSTOMER = new SimpleGrantedAuthority("gold");


    @GetMapping("/messages")
    public String getMessages(JwtAuthenticationToken auth ){
        log.info("authorities is ", auth.getAuthorities());
        if ( auth.getAuthorities().contains(GOLD_CUSTOMER)) {
            // q.setPrice(10.0);
        }
        return "the protected messages";
    }
}
