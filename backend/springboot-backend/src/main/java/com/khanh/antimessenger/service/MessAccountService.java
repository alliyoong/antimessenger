package com.khanh.antimessenger.service;

import com.khanh.antimessenger.dto.CreateAccountRequestDto;
import com.khanh.antimessenger.model.MessAccount;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface MessAccountService {
    MessAccount createAccount(CreateAccountRequestDto data, MultipartFile file);

    Collection<MessAccount> getListAccount(int page, int pageSize);

    Collection<MessAccount> getAllAccount();

    MessAccount getAccountByUserName(String username);

    MessAccount getAccountByAccountId(Long id);

    MessAccount updateAccount(Long id, CreateAccountRequestDto account, MultipartFile file);

    void deleteAccount(Long id);

    String resetPassword(Long id);

    MessAccount updateProfileImage(String username, MultipartFile file);
}
