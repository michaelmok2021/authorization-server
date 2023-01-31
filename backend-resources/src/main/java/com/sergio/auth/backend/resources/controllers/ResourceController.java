package com.sergio.auth.backend.resources.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

@RestController
@Slf4j
public class ResourceController {

    private static final GrantedAuthority GOLD_CUSTOMER = new SimpleGrantedAuthority("gold");


    @GetMapping("/messages")
//    public String getMessages(JwtAuthenticationToken auth, @AuthenticationPrincipal Jwt princial){
    public String getMessages(@AuthenticationPrincipal Jwt principal){
        log.info("Claims is ", principal.getClaims());
//        if ( auth.getAuthorities().contains(GOLD_CUSTOMER)) {
//            // q.setPrice(10.0);
//        }
        return "the protected messages";
    }

    @GetMapping("/user/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal) {
        log.info("getUserInfo called for user " + principal.getClaimAsString("preferred_username"));
        Map<String, String> map = new Hashtable<String, String>();
        map.put("user_name", principal.getClaimAsString("preferred_username"));
        map.put("organization", principal.getClaimAsString("organization"));
        return Collections.unmodifiableMap(map);
    }

    @PostMapping("/post/message")
    public String postMessage(@RequestBody String message) {

        return "Posted: " + message;
    }
    @GetMapping(path= "/rose")
    public String rose(@AuthenticationPrincipal Jwt principal){
        return "--------{---(@";
    }

}
