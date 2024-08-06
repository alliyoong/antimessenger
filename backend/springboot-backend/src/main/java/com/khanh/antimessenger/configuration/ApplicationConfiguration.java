package com.khanh.antimessenger.configuration;

import com.khanh.antimessenger.constant.SecurityConstant;
import com.khanh.antimessenger.model.MessAccount;
import com.khanh.antimessenger.model.UserPrincipal;
import com.khanh.antimessenger.repository.MessAccountRepository;
import com.khanh.antimessenger.utilities.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final MessAccountRepository repository;
    private final LoginAttemptService loginAttemptService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstant.BCRYPT_STRENGTH);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (username) -> {
            MessAccount messAccount = repository.findMessAccountByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User cannot be found"));
            validateLoginAttempt(messAccount);
            messAccount.setLastLoginDateDisplay(messAccount.getLastLoginDate());
            messAccount.setLastLoginDate(LocalDateTime.now());
            repository.save(messAccount);
            return (UserDetails) new UserPrincipal(messAccount);
        };
    }

    private void validateLoginAttempt(MessAccount account){
        if (account.getIsNonLocked() == 1) {
            if (loginAttemptService.hasExceededMaxAttempt(account.getUsername()))
                account.setIsNonLocked(0);
            else
                account.setIsNonLocked(1);
        } else {
            loginAttemptService.removeUserFromLoginAttempCache(account.getUsername());
        }
    }

    @Bean
    public AuthenticationProvider customProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
