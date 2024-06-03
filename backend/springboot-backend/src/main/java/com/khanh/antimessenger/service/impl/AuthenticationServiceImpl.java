package com.khanh.antimessenger.service.impl;

import com.khanh.antimessenger.constant.SecurityConstant;
import com.khanh.antimessenger.constant.VerificationType;
import com.khanh.antimessenger.dto.RegisterRequestDto;
import com.khanh.antimessenger.dto.dtomapper.DtoMapper;
import com.khanh.antimessenger.exception.AccountAlreadyActivated;
import com.khanh.antimessenger.exception.InvalidPasswordException;
import com.khanh.antimessenger.exception.ResourceAlreadyInUseException;
import com.khanh.antimessenger.exception.ResourceNotFoundException;
import com.khanh.antimessenger.model.*;
import com.khanh.antimessenger.repository.AccountVerificationRepository;
import com.khanh.antimessenger.repository.MessAccountRepository;
import com.khanh.antimessenger.repository.RoleRepository;
import com.khanh.antimessenger.service.AuthenticationService;
import com.khanh.antimessenger.utilities.EmailService;
import com.khanh.antimessenger.utilities.AccessTokenService;
import com.khanh.antimessenger.service.MessAccountService;
import com.khanh.antimessenger.utilities.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.khanh.antimessenger.constant.RoleType.ROLE_USER;
import static com.khanh.antimessenger.constant.SecurityConstant.PASSWORD_PATTERN;
import static com.khanh.antimessenger.constant.SecurityConstant.VERIFICATION_URL_EXPIRATION_TIME;
import static com.khanh.antimessenger.constant.VerificationType.ACCOUNT;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final MessAccountRepository messAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AccountVerificationRepository accountVerificationRepository;
    private final EmailService emailService;
    private final AuthenticationManager customAuthenticationManager;
    private final MessAccountService messAccountService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    @Value("${application.front-end.url}")
    private String frontEndUrl;

    @Override
    public MessAccount register(RegisterRequestDto data) {
        if(!isEmailUnique(data.getEmail())) throw new ResourceAlreadyInUseException("account", "email", data.getEmail());
        if (!isUserNameUnique(data.getUsername()))
            throw new ResourceAlreadyInUseException("account", "username", data.getUsername());
        if (!isPasswordValid(data.getPassword())) throw new InvalidPasswordException();
        // copy data properties to a mess account
        DtoMapper<RegisterRequestDto, MessAccount> dtoMapper = new DtoMapper<>(RegisterRequestDto::new, MessAccount::new);
        MessAccount toAdd = dtoMapper.toEntity(data);
        try {
            Role roleUser = roleRepository.findRoleByRoleName(ROLE_USER.name())
                    .orElseThrow(() -> new ResourceNotFoundException("role", "roleName", ROLE_USER.name()));

            toAdd.setRole(roleUser);
            toAdd.setPassword(encoder.encode(toAdd.getPassword()));
            messAccountRepository.save(toAdd);

            String verificationUrl = generateVerificationUrl(ACCOUNT);
            AccountVerification accountVerification = AccountVerification.builder()
                    .messAccount(toAdd)
                    .expirationDate(LocalDateTime.now().plusSeconds(VERIFICATION_URL_EXPIRATION_TIME))
                    .url(verificationUrl)
                    .build();
            accountVerificationRepository.save(accountVerification);
            emailService.sendEmail(toAdd.getEmail(), toAdd.getFirstName(), verificationUrl, ACCOUNT);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred. Please try again");
        }
        return toAdd;
    }

    @Override
    public Map<String, Object> login(String username, String password, HttpServletResponse response, HttpServletRequest request) {
        customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        MessAccount loginAccount = messAccountService.getAccountByUserName(username);
        UserPrincipal userPrincipal = new UserPrincipal(loginAccount);
        HttpHeaders jwtHeader = generateJwtHeader(userPrincipal);
        addRefreshTokenCookie(userPrincipal, response, request);
        return Map.of("jwtHeader", jwtHeader, "account", loginAccount);
    }

    private void addRefreshTokenCookie(UserPrincipal userPrincipal, HttpServletResponse response, HttpServletRequest request) {
        String token = refreshTokenService.generateToken(userPrincipal, request);
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @Override
    public void enableAccountByVerifyEmail(String key) {
        String verifyUrl = String.format("%s/verify/account/%s", frontEndUrl, key);
        AccountVerification verification = accountVerificationRepository.findByUrl(verifyUrl)
                .orElseThrow(() -> new ResourceNotFoundException("verification url", "url", verifyUrl));
        MessAccount account = verification.getMessAccount();
        if (account.getIsEnabled() == 1) {
            throw new AccountAlreadyActivated();
        }
        account.setIsEnabled(1);
        messAccountRepository.save(account);

    }

    private HttpHeaders generateJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.TOKEN_HEADER, accessTokenService.generateToken(user));
        headers.add("Access-Control-Expose-Headers", "Jwt-Token");
        return headers;
    }

    private String generateVerificationUrl(VerificationType type) {
        String key = UUID.randomUUID().toString();
        return String.format("%s/verify/%s/%s", frontEndUrl, type.getType(), key);
//        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/verify/" + type.getType() + "/" + key).toUriString();
    }

    private boolean isEmailUnique(String email) {
        return StringUtils.isNotBlank(email.trim()) && messAccountRepository.findMessAccountByEmail(email).isEmpty();
    }

    private boolean isUserNameUnique(String username) {
        return StringUtils.isNotBlank(username.trim()) && messAccountRepository.findMessAccountByUsername(username).isEmpty();
    }

    private boolean isPasswordValid(String password) {
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        return passwordPattern.matcher(Objects.requireNonNull(password)).matches();
    }
}
