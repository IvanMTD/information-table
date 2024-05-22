package ru.fcpsr.informationtable.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Mono;
import ru.fcpsr.informationtable.enums.Role;
import ru.fcpsr.informationtable.models.ApplicationUser;
import ru.fcpsr.informationtable.repositories.ApplicationUserRepository;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("session-information-table");
        resolver.setCookieMaxAge(Duration.ofHours(24));
        return resolver;
    }

    @Bean
    public CommandLineRunner preSetup(ApplicationUserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            log.info("*****************| environments start |*******************");
            for(String key : System.getenv().keySet()){
                String[] p = key.split("\\.");
                if(p.length > 1) {
                    log.info("\"{}\"=\"{}\"", key, System.getenv().get(key));
                }
            }
            log.info("******************| environments end |********************");

            userRepository.findByUsername(adminUsername).flatMap(user -> {
                log.info("a user with the username {} already exists in the database",adminUsername);
                return Mono.just(user);
            }).switchIfEmpty(Mono.just(new ApplicationUser()).flatMap(user -> {
                user.setUsername(adminUsername);
                user.setPassword(encoder.encode(adminPassword));
                user.setEmail("admin@security.net");
                user.setRole(Role.ADMIN);
                user.setPlacedAt(LocalDate.now());
                return userRepository.save(user).flatMap(saved -> {
                    log.info("a user with the username {} has been added to the database", saved.getUsername());
                    return Mono.just(saved);
                });
            })).subscribe();
        };
    }
}
