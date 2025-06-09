package com.artem.individuals.rest;

import com.artem.individuals.dto.request.AuthRequest;
import com.artem.individuals.dto.request.RegistrationRequest;
import com.artem.individuals.dto.response.TokenResponse;
import com.artem.individuals.dto.response.UserResponse;
import com.artem.individuals.service.KeycloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakService keycloakService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TokenResponse> register(@Valid @RequestBody RegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password confirmation does not match!!!"));
        }

        return keycloakService.userExists(request.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "User already exists"));
                    }
                    return keycloakService.registerUser(request.getEmail(), request.getPassword());
                });
    }

    @PostMapping("/login")
    public Mono<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return keycloakService.loginUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh-token")
    public Mono<TokenResponse> refreshToken(@Valid @RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        if (refreshToken == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token required"));
        }
        return keycloakService.refreshToken(refreshToken);
    }

    @GetMapping("/me")
    public Mono<UserResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return keycloakService.getUserById(jwt.getSubject())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }
}