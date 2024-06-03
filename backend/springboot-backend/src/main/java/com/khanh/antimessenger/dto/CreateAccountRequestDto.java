package com.khanh.antimessenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.khanh.antimessenger.constant.ExceptionConstant.*;
import static com.khanh.antimessenger.constant.ExceptionConstant.EMPTY_PASSWORD_ERROR_MSG;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateAccountRequestDto {
    @JsonProperty("firstName")
    @NotEmpty(message = EMPTY_FIRSTNAME_ERROR_MSG, groups = OnUpdate.class)
    private String firstName;

    @JsonProperty("lastName")
    @NotEmpty(message = EMPTY_LASTNAME_ERROR_MSG, groups = OnUpdate.class)
    private String lastName;

    @JsonProperty("username")
    @NotEmpty(message = EMPTY_USERNAME_ERROR_MSG, groups = OnUpdate.class)
    private String username;

    @JsonProperty("email")
    @NotEmpty(message = EMPTY_EMAIL_ERROR_MSG, groups = OnUpdate.class)
    @Email(message = INVALID_EMAIL_ERROR_MSG, groups = OnUpdate.class)
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = EMPTY_PASSWORD_ERROR_MSG, groups = OnCreate.class)
    private String password;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("address")
    private String address;

    @JsonProperty("role")
    @NotEmpty(message = EMPTY_ROLE_ERROR_MSG, groups = OnUpdate.class)
    private String role;

    @JsonProperty("isEnabled")
    private Integer isEnabled;

    @JsonProperty("isNonLocked")
    private Integer isNonLocked;

    @JsonProperty("isUsingMfa")
    private Integer isUsingMfa;

    public interface OnCreate{}
    public interface OnUpdate{}
}
