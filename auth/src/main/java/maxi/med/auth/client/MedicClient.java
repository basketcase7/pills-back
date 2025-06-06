package maxi.med.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "medic-service", url = "http://localhost:8081")
public interface MedicClient {

    @PostMapping("api/user/{id}")
    void createUser(@PathVariable Long id);
}
