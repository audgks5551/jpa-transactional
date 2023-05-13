package io.mhan.transactiontest;

import io.mhan.transactiontest.users.entity.AutoUser;
import io.mhan.transactiontest.users.repository.AutoUserRepository;
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
public class AutoUserTransactionTest {

    @Autowired
    private AutoUserRepository autoUserRepository;

    // mariadb에서 auto는 sequence 전략
    @Test
    @DisplayName("test에 @transactional없이 save 메서드를 호출하여 auto user 저장")
    void t1() {
        AutoUser autoUser = AutoUser.create("user1", "pass");
        autoUserRepository.save(autoUser); // 여기서 commit이 일어남!!

        log.info("메서드 끝!!");
    }

    @Test
    @Transactional
    @Rollback(false)
    @DisplayName("test에 @transactional을 추가하고 Rollback false로 설정한 후 save 메서드를 호출하여 auto user 저장")
    void t2() {
        AutoUser autoUser = AutoUser.create("user1", "pass");
        autoUserRepository.save(autoUser);

        log.info("메서드 끝!!");
    }
    // insert 지연이 일어남!! 이유 => SQL문 개수 최소화
    // 여기서 commit이 일어남!!

    @Test
    @Transactional
    @Rollback(true)
    @DisplayName("test에 @transactional을 추가하고 Rollback true로 설정한 후 save 메서드를 호출하여 auto user 저장")
    void t3() {
        AutoUser autoUser = AutoUser.create("user1", "pass");
        autoUserRepository.save(autoUser);

        log.info("메서드 끝!!");
    }
    // rollback으로 인해 insert문 발생하지 않음
    // 결국 쓰기 지연으로 인해 query 문들을 모아뒀지만 rollback으로 인해 실제 db에 반영되지 않음

    @Test
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t4() {
        AutoUser autoUser = AutoUser.create("user1", "pass");
        AutoUser savedUser = autoUserRepository.save(autoUser);

        assertThat(autoUser).isEqualTo(savedUser);

        AutoUser findAutoUser = autoUserRepository.findById(autoUser.getId()).get();
        assertThat(findAutoUser).isNotEqualTo(savedUser);

        log.info("메서드 끝!!");
    }

    @Test
    @Transactional
    @Rollback(false)
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t5() {
        AutoUser autoUser = AutoUser.create("user1", "pass");
        AutoUser savedUser = autoUserRepository.save(autoUser);

        assertThat(autoUser).isEqualTo(savedUser);

        AutoUser findAutoUser = autoUserRepository.findById(autoUser.getId()).get();
        assertThat(findAutoUser).isEqualTo(savedUser);

        log.info("메서드 끝!!");
    }
    // test에서는 Transactional 범위에서만 영속성이 살아있음

    @Test
    @Order(1)
    @Transactional
    @Rollback(true)
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t6() {
        AutoUser autoUser = AutoUser.create("user1", "pass");
        AutoUser savedUser = autoUserRepository.save(autoUser);

        log.info("메서드 끝!!");
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback(false)
    @DisplayName("autoUser와 savedUser가 같은 주소값을 가지는지 확인")
    void t7() {
        AutoUser autoUser = AutoUser.create("user2", "pass");
        AutoUser savedUser = autoUserRepository.save(autoUser);

        assertThat(savedUser.getId()).isEqualTo(2);

        log.info("메서드 끝!!");
    }
    // 결론 : rollback 되면 모든 sql문 취소
}
