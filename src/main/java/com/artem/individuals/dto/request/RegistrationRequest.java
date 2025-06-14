package com.artem.individuals.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest extends AuthRequest {
    @JsonProperty("confirm_password")
    private String confirmPassword;
}