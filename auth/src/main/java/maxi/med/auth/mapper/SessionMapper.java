package maxi.med.auth.mapper;

import maxi.med.auth.dto.response.CurrentSessionResponse;
import maxi.med.auth.dto.response.SessionResponse;
import maxi.med.auth.entity.Session;

public class SessionMapper {
    public static SessionResponse mapSessionResponse(Session session) {
        return new SessionResponse(session.id(), session.user().id(), session.token(), session.expirationTime());
    }

    public static CurrentSessionResponse mapCurrentSessionResponse(Session session, Boolean active) {
        return new CurrentSessionResponse(session.id(), session.user().id(), session.expirationTime(), active);
    }
}
