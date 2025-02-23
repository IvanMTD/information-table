package ru.fcpsr.informationtable.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import ru.fcpsr.informationtable.repositories.ApplicationUserRepository;
import ru.fcpsr.informationtable.services.ApplicationUserService;
import ru.fcpsr.informationtable.utils.*;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final ApplicationUserRepository userRepository;
    private final JwtUtil jwt;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http){
        ServerCsrfTokenRequestAttributeHandler requestHandler = new ServerCsrfTokenRequestAttributeHandler();
        requestHandler.setTokenFromMultipartDataEnabled(true);

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter());

        return http
                .csrf(csrf -> csrf.csrfTokenRequestHandler(requestHandler))
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth/login","/img/**","/favicon.ico").permitAll()
                        .anyExchange().permitAll())
                .formLogin(loginSpec -> loginSpec.loginPage("/auth/login").authenticationSuccessHandler(authenticationSuccessHandler()))
                .logout(logoutSpec -> logoutSpec.logoutSuccessHandler(logoutSuccessHandler()))
                .requestCache(requestCacheSpec -> requestCacheSpec.requestCache(serverRequestCache()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ServerRequestCache serverRequestCache() {
        return new WebSessionServerRequestCache();
    }

    @Bean
    public JwtAuthenticationManager authenticationManager() {
        return new JwtAuthenticationManager(jwt, applicationUserService(), passwordEncoder());
    }

    @Bean
    public JwtAuthenticationSuccessHandler authenticationSuccessHandler(){
        return new JwtAuthenticationSuccessHandler(jwt,serverRequestCache());
    }

    @Bean
    public JwtLogoutSuccessHandler logoutSuccessHandler(){
        JwtLogoutSuccessHandler logoutSuccessHandler = new JwtLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/"));
        return logoutSuccessHandler;
    }

    @Bean
    public JwtAuthenticationConverter authenticationConverter(){
        return new JwtAuthenticationConverter();
    }

    @Bean
    public ApplicationUserService applicationUserService(){
        return new ApplicationUserService(userRepository,passwordEncoder());
    }
}
