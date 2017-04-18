package com.soprasteria.initiatives.auth.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author cegiraud
 */
@FeignClient("user-service")
@FunctionalInterface
public interface UserClient {

    @PutMapping("/api/users/activate")
    void activate(@RequestHeader("authorization") String authorization, @RequestParam("uuid") String uuid);

}
