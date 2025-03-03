package com.eighttoten.auth.repository;

import com.eighttoten.auth.AuthEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<AuthEntity, String> {
    Optional<AuthEntity> findByRefreshToken(String refreshToken);
}