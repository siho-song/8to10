package com.eighttoten.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @Value("${HOSTNAME:unknown}")
    private String podName;

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth(){
        return new ResponseEntity<>(podName, HttpStatus.OK);
    }
}