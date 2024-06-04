package com.example.lucky7postservice.src.auth;

import com.example.lucky7postservice.utils.openfeign.HeaderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service", configuration = HeaderConfig.class)
public interface AuthServiceClient {
    @PostMapping("/auth/token/server-validation")
    ResponseEntity<Long> validateToken();
}