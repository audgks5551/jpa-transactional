package io.mhan.transactiontest.init;

import io.mhan.transactiontest.users.entity.AutoUser;
import io.mhan.transactiontest.users.entity.User;
import io.mhan.transactiontest.users.repository.AutoUserRepository;
import io.mhan.transactiontest.users.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class InitData {

    @Bean
    @Profile("default")
    public ApplicationRunner defaultInit(AutoUserRepository autoUserRepository) {
        return args -> {
            AutoUser user = AutoUser.create("user1", "pass");
            autoUserRepository.save(user);
        };
    }
}
