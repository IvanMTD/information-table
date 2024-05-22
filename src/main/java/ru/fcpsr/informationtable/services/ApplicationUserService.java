package ru.fcpsr.informationtable.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fcpsr.informationtable.repositories.ApplicationUserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationUserService implements ReactiveUserDetailsService {
    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
