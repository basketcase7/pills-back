package maxi.med.auth.dto.response;

import java.util.Date;

public record CurrentSessionResponse(Long id, Long userId, Date expirationTime, Boolean active) {}
