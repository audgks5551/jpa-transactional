package io.mhan.transactiontest.users.repository;

import io.mhan.transactiontest.users.entity.AutoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoUserRepository extends JpaRepository<AutoUser, Long> {
    Optional<AutoUser> findByUsername(String username);
}
