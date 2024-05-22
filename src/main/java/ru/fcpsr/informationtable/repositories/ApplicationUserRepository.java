package ru.fcpsr.informationtable.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import ru.fcpsr.informationtable.models.ApplicationUser;

public interface ApplicationUserRepository extends ReactiveCrudRepository<ApplicationUser,Long> {
    Mono<UserDetails> findByUsername(String username);
}
