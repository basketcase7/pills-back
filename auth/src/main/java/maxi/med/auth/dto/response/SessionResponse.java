package maxi.med.auth.dto.response;

import java.util.Date;

public record SessionResponse(Long id, Long userId, String token, Date expirationTime) {}
