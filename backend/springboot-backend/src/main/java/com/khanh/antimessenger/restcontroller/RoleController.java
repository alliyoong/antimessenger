package com.khanh.antimessenger.restcontroller;

import com.khanh.antimessenger.exception.AppExceptionHandler;
import com.khanh.antimessenger.model.HttpResponse;
import com.khanh.antimessenger.model.Role;
import com.khanh.antimessenger.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController extends AppExceptionHandler {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<HttpResponse> getListRole() {
        List<Role> data = roleService.getAllRole().stream().toList();
        return ResponseEntity.created(null).body(HttpResponse.builder()
                .httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("Successfully get list role")
                .data(Map.of("listRole", data))
                .build());
    }
}
