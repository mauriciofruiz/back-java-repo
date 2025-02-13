package com.example.test.security.repository;

import com.example.test.security.config.CustomAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final CustomAuth customAuth;
    /**
     * Metodo que se encarga de guardar el SecurityContext
     * @param exchange the exchange to associate to the SecurityContext
     * @param context the SecurityContext to save
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return customAuth.authenticate(new UsernamePasswordAuthenticationToken("defaultUser", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))))
                .map(SecurityContextImpl::new);
    }
}
