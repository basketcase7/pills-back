package maxi.med.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterBody(
        @Size(min = 1, max = 50, message = "First name must be at least 1 character and no more than 50")
                @NotBlank(message = "First name cannot be empty")
                String firstName,
        @Size(min = 1, max = 50, message = "Last name must be at least 1 character and no more than 50")
                @NotBlank(message = "Last name cannot be empty")
                String lastName,
        @Size(min = 5, max = 100, message = "Email must be at least 5 character and no more than 100") String email,
        @NotBlank(message = "Password cannot be empty") String password) {}
