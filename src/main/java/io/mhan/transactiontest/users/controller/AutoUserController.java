package io.mhan.transactiontest.users.controller;

import io.mhan.transactiontest.users.entity.AutoUser;
import io.mhan.transactiontest.users.service.AutoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutoUserController {

    private final AutoUserService autoUserService;

    @GetMapping("/autoUser/check")
    public String permanenceCheck() {

        AutoUser first = autoUserService.findByUsername("user1");
        AutoUser second = autoUserService.findByUsername("user1");

        return String.valueOf(first.equals(second));
        // true 반환 -> 영속성 o
        // false 반환 -> 영속성 x
        // open-in-view: true -> 영속성 o
        // open-in-view: false -> 영속성 x
    }

    @GetMapping("/autoUser/check2")
    public String permanenceCheck2() {

        String check = autoUserService.findByUsernameNotTransaction("user1");

        return check;
    }

    @GetMapping("/autoUser/check3")
    public String permanenceCheck3() {

        String check = autoUserService.findByUsernameTransaction("user1");

        return check;
    }

    @GetMapping("/autoUser/check4")
    public String rollbackCheck4() {

        String check = autoUserService.saveRollback("user2");

        return check;
    }
}
