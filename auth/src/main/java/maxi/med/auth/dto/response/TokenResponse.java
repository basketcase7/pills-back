package maxi.med.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(String token) {}
