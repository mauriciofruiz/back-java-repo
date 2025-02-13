package com.example.test.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuth implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return
                Mono.just(authentication)
                        .map(auth -> new UsernamePasswordAuthenticationToken(
                                auth.getPrincipal(),
                                auth.getCredentials(),
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        ));
    }
}
