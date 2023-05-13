package io.mhan.transactiontest.users.service;

import io.mhan.transactiontest.users.entity.User;
import io.mhan.transactiontest.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public String findByUsernameNotTransaction(String username) {
        User first = userRepository.findByUsername(username).get();

        User second = userRepository.findByUsername(username).get();

        return String.valueOf(first.equals(second));
        // open-in-view: false일 때 트랜잭션 걸지 않기
        // true 반환 -> 영속성 o
        // false 반환 -> 영속성 x
    }

    @Transactional
    public String findByUsernameTransaction(String username) {
        User first = userRepository.findByUsername(username).get();

        User second = userRepository.findByUsername(username).get();

        return String.valueOf(first.equals(second));
        // open-in-view: false일 때 트랜잭션 걸기
        // true 반환 -> 영속성 o
        // false 반환 -> 영속성 x
    }
    // 결론 : open-in-view: false일 때 transactional 범위 내에서만 영속성 유지

    @Transactional
    public String saveRollback(String username) {
        User autoUser = userRepository.save(User.create("user2", "pass"));

        if (1==1) throw new RuntimeException();

        return "ok";
    }
}
