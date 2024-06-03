package com.khanh.antimessenger.listener;

import com.khanh.antimessenger.model.UserPrincipal;
import com.khanh.antimessenger.utilities.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationSuccessListener {
    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal account = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.removeUserFromLoginAttempCache(account.getUsername());
        }
    }
}
