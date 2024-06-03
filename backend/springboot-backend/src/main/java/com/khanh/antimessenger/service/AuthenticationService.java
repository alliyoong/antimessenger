package com.khanh.antimessenger.service;

import com.khanh.antimessenger.dto.RegisterRequestDto;
import com.khanh.antimessenger.model.MessAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthenticationService {
    MessAccount register(RegisterRequestDto data);

    Map<String, Object> login(String username, String password, HttpServletResponse response, HttpServletRequest request);

    void enableAccountByVerifyEmail(String key);
}
