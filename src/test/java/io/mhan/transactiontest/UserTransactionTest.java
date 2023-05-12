package io.mhan.transactiontest;

import io.mhan.transactiontest.users.entity.User;
import io.mhan.transactiontest.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

// 하나씩 수행하여 결과를 보는 것이 정확함
@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTransactionTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("test에 @transactional없이 save 메서드를 호출하여 user 저장")
    void t1() {
        User autoUser = User.create("user1", "pass");
        userRepository.save(autoUser); // 여기서 commit이 일어남!!

        log.info("메서드 끝!!");
    }

    @Test
    @Transactional
    @Rollback(false)
    @DisplayName("test에 @transactional을 추가하고 Rollback false로 설정한 후 save 메서드를 호출하여 user 저장")
    void t2() {
        User autoUser = User.create("user1", "pass");
        userRepository.save(autoUser);

        log.info("메서드 끝!!");
    }
    // 쓰기 지연이 일어남!! 이유 => SQL문 개수 최소화
    // 여기서 commit이 일어남!!

    @Test
    @Transactional
    @Rollback(true)
    @DisplayName("test에 @transactional을 추가하고 Rollback true로 설정한 후 save 메서드를 호출하여 user 저장")
    void t3() {
        User autoUser = User.create("user1", "pass");
        userRepository.save(autoUser);

        log.info("메서드 끝!!");
    }
    // insert문 발생
    // rollback으로 인해 취소됨

    @Test
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t4() {
        User autoUser = User.create("user1", "pass");
        User savedUser = userRepository.save(autoUser);

        assertThat(autoUser).isEqualTo(savedUser);

        User findAutoUser = userRepository.findById(autoUser.getId()).get();
        assertThat(findAutoUser).isNotEqualTo(savedUser);

        log.info("메서드 끝!!");
    }

    @Test
    @Transactional
    @Rollback(true)
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t5() {
        User autoUser = User.create("user1", "pass");
        User savedUser = userRepository.save(autoUser);

        assertThat(autoUser).isEqualTo(savedUser);

        User findAutoUser = userRepository.findById(autoUser.getId()).get();
        assertThat(findAutoUser).isEqualTo(savedUser);

        log.info("메서드 끝!!");
    }
    // test에서는 Transactional 범위에서만 영속성이 살아있음

    @Test
    @Order(1)
    @Transactional
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t6() {
        User autoUser = User.create("user1", "pass");
        User savedUser = userRepository.save(autoUser);

        log.info("메서드 끝!!");
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback(false)
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t7() {
        User autoUser = User.create("user2", "pass");
        User savedUser = userRepository.save(autoUser);

        assertThat(savedUser.getId()).isEqualTo(2);

        log.info("메서드 끝!!");
    }
    // 결론 : rollback 되면 모든 sql문 취소

    @Test
    @DisplayName("save를 두번하기 전에 user password 수정")
    void t8() {
        User autoUser = User.create("user3", "pass");
        User savedUser = userRepository.save(autoUser);

        User savedUser2 = userRepository.save(savedUser);

        assertThat(savedUser).isNotEqualTo(savedUser2);

        log.info("메서드 끝!!");
    }

    @Test
    @DisplayName("save를 두번하기 전에 user password 수정")
    void t9() {
        User autoUser = User.create("user3", "pass");
        User savedUser = userRepository.save(autoUser);

        savedUser.setPassword("pass2");
        User savedUser2 = userRepository.save(savedUser);

        assertThat(savedUser).isNotEqualTo(savedUser2);

        log.info("메서드 끝!!");
    }
    // spring boot jpa는 새로운 객체를 id가 null일 때라고 생각한다. 그래서 persist 메서드가 실행된다.
    // spring boot jpa는 id가 있을 때 영속성 컨테스트에 없으면 select를 하여 찾아와서 만약 필드들이 다르다면 업데이트를 한다.

    @Test
    @DisplayName("테이블에 없는 id를 추가하여 merge를 시키면 어떻게 작동하는지 테스트")
    void t10() {
        User user = User.builder()
                .id(100L)
                .username("user3")
                .password("pass")
                .build();

        User savedUser = userRepository.save(user);

        log.info("메서드 끝!!");
    }
    // merge가 일어나면 select를 하여 해당 레코드가 없다면 insert문 발생

    @Test
    @DisplayName("테이블에 없는 id를 추가하여 merge를 시키면 어떻게 작동하는지 테스트")
    void t11() {
        User autoUser = User.create("user3", "pass");
        User savedUser = userRepository.save(autoUser);

        log.info("메서드 끝!!");
    }
    // merge가 일어나면 select를 하여 해당 레코드가 없다면 insert문 발생
}
