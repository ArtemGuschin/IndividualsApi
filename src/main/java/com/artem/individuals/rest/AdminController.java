package com.artem.individuals.rest;

import com.artem.individuals.dto.response.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @GetMapping("/users")
    public Flux<UserResponse> getAllUsers() {
        return Flux.just(
                new UserResponse("1", "admin@example.com", List.of("ADMIN"), "2023-01-01T00:00:00Z"),
                new UserResponse("2", "user@example.com", List.of("USER"), "2023-01-02T00:00:00Z")
        );
    }
}