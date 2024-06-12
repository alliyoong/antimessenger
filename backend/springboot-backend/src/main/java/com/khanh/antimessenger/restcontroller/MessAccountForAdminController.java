package com.khanh.antimessenger.restcontroller;

import com.khanh.antimessenger.dto.AccountViewDto;
import com.khanh.antimessenger.dto.CreateAccountRequestDto;
import com.khanh.antimessenger.dto.CreateAccountRequestDto.OnCreate;
import com.khanh.antimessenger.dto.CreateAccountRequestDto.OnUpdate;
import com.khanh.antimessenger.dto.dtomapper.DtoMapper;
import com.khanh.antimessenger.exception.AppExceptionHandler;
import com.khanh.antimessenger.model.HttpResponse;
import com.khanh.antimessenger.model.MessAccount;
import com.khanh.antimessenger.service.MessAccountService;
import com.khanh.antimessenger.utilities.ValidUploadFile;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
@Validated
public class MessAccountForAdminController extends AppExceptionHandler {
    private final MessAccountService messAccountService;

    @GetMapping("/{username}")
    public ResponseEntity<HttpResponse> getAccount(@PathVariable("username") String username) {
        MessAccount account = messAccountService.getAccountByUserName(username);
        DtoMapper<AccountViewDto, MessAccount> dtoMapper = new DtoMapper<>(AccountViewDto::new, MessAccount::new);
        AccountViewDto accountDto = dtoMapper.fromEntity(account);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .data(Map.of("user", accountDto))
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<HttpResponse> getListAccount() {
        DtoMapper<AccountViewDto, MessAccount> dtoMapper = new DtoMapper<>(AccountViewDto::new, MessAccount::new);
        List<AccountViewDto> listAccountDto =  messAccountService.getAllAccount().stream()
                .map(account -> (AccountViewDto) dtoMapper.fromEntity(account))
                .collect(Collectors.toList());
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("listAccount", listAccountDto))
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> addAccount(@Validated({OnCreate.class, OnUpdate.class}) @RequestPart(name = "account") CreateAccountRequestDto data,
                                                   @ValidUploadFile @RequestPart(name = "profileImage", required = false) MultipartFile file) {
        MessAccount toCreate = messAccountService.createAccount(data, file);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(CREATED.value())
                        .httpStatus(CREATED)
                        .message("Account has been added successfully")
                        .data(Map.of("account", toCreate))
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> updateAccount(@PathVariable Long id,
                                                      @Validated(OnUpdate.class) @RequestPart(name = "account") CreateAccountRequestDto data,
                                                      @ValidUploadFile @RequestPart(name = "profileImage", required = false) MultipartFile file) {
        MessAccount updated = messAccountService.updateAccount(id, data, file);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(CREATED.value())
                        .httpStatus(CREATED)
                        .message("Account has been updated successfully")
                        .data(Map.of("account", updated))
                        .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping ("/{id}")
    public ResponseEntity<HttpResponse> deleteAccount(@PathVariable long id) {
        messAccountService.deleteAccount(id);

        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(NO_CONTENT.value())
                        .httpStatus(NO_CONTENT)
                        .message("Account successfully deleted.")
                        .build()
        );
    }

    @GetMapping("/reset-password/{id}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable long id) {
        messAccountService.resetPassword(id);
        return ResponseEntity.created(null).body(
            HttpResponse.builder()
                    .httpStatusCode(OK.value())
                    .httpStatus(OK)
                    .message("New password has been sent to users email")
                    .build()
        );
    }
}
