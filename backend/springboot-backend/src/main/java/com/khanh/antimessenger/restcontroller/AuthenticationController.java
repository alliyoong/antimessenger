package com.khanh.antimessenger.restcontroller;

import com.khanh.antimessenger.dto.AccountViewDto;
import com.khanh.antimessenger.dto.RegisterRequestDto;
import com.khanh.antimessenger.dto.dtomapper.DtoMapper;
import com.khanh.antimessenger.exception.AppExceptionHandler;
import com.khanh.antimessenger.model.HttpResponse;
import com.khanh.antimessenger.model.MessAccount;
import com.khanh.antimessenger.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {"/"})
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController extends AppExceptionHandler {
    private final AuthenticationService authenticationService;
    private final Validator validator;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestParam("firstName") String firstName,
                                                 @RequestParam("lastName") String lastName,
                                                 @RequestParam("username") String username,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("email") String email) throws NoSuchMethodException, MethodArgumentNotValidException {
        RegisterRequestDto data = RegisterRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .email(email)
                .build();
        DataBinder binder = new DataBinder(data);
        binder.setValidator(validator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            Method method = this.getClass().getMethod("register", String.class, String.class, String.class, String.class, String.class);
            MethodParameter parameter = new MethodParameter(method, 0);
            throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
        }
        MessAccount messAccount = authenticationService.register(data);
        DtoMapper<AccountViewDto, MessAccount> dtoMapper = new DtoMapper<>(AccountViewDto::new, MessAccount::new);
        AccountViewDto dto = dtoMapper.fromEntity(messAccount);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .data(Map.of("account",dto))
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .message("You have registered successfully")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @NonNull HttpServletResponse response,
                                              @NonNull HttpServletRequest request) {
        Map<String, Object> result = authenticationService.login((username), password, response, request);
        HttpHeaders jwtHeader = (HttpHeaders) result.get("jwtHeader");
        MessAccount account = (MessAccount) result.get("account");
        DtoMapper<AccountViewDto, MessAccount> dtoMapper = new DtoMapper<>(AccountViewDto::new, MessAccount::new);
        AccountViewDto dto = dtoMapper.fromEntity(account);
        return ResponseEntity.created(null).headers(jwtHeader).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("account",dto))
                        .message("Login successfully")
                        .build()
        );
    }

    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable @NotEmpty String key) {
        authenticationService.enableAccountByVerifyEmail(key);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .message("Account was successfully activated")
                        .build()
        );
    }

    @PostMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
//        Cookie cookie = request.getCookies();
    }

}
