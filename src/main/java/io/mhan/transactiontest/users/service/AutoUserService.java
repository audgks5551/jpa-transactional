package io.mhan.transactiontest.users.service;

import io.mhan.transactiontest.users.entity.AutoUser;
import io.mhan.transactiontest.users.repository.AutoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AutoUserService {

    private final AutoUserRepository autoUserRepository;

    public AutoUser findByUsername(String username) {
        return autoUserRepository.findByUsername(username).get();
    }

    public String findByUsernameNotTransaction(String username) {
        AutoUser first = autoUserRepository.findByUsername(username).get();

        AutoUser second = autoUserRepository.findByUsername(username).get();

        return String.valueOf(first.equals(second));
        // open-in-view: false일 때 트랜잭션 걸지 않기
        // true 반환 -> 영속성 o
        // false 반환 -> 영속성 x
    }

    @Transactional
    public String findByUsernameTransaction(String username) {
        AutoUser first = autoUserRepository.findByUsername(username).get();

        AutoUser second = autoUserRepository.findByUsername(username).get();

        return String.valueOf(first.equals(second));
        // open-in-view: false일 때 트랜잭션 걸기
        // true 반환 -> 영속성 o
        // false 반환 -> 영속성 x
    }
    // 결론 : open-in-view: false일 때 transactional 범위 내에서만 영속성 유지

    @Transactional
    public String saveRollback(String username) {
        AutoUser autoUser = autoUserRepository.save(AutoUser.create("user2", "pass"));

        if (1==1) throw new RuntimeException();

        return "ok";
    }
}
