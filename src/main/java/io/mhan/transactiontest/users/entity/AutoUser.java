package io.mhan.transactiontest.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class AutoUser {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime created;

    private LocalDateTime updated;

    public static AutoUser create(String username, String password) {
        AutoUser user = AutoUser.builder()
                .username(username)
                .password(password)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        return user;
    }
}
