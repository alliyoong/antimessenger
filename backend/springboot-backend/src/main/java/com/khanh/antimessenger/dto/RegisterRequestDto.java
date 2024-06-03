package com.khanh.antimessenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.khanh.antimessenger.constant.ExceptionConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    @JsonProperty("firstName")
    @NotEmpty(message = EMPTY_FIRSTNAME_ERROR_MSG)
    private String firstName;

    @JsonProperty("lastName")
    @NotEmpty(message = EMPTY_LASTNAME_ERROR_MSG)
    private String lastName;

    @JsonProperty("username")
    @NotEmpty(message = EMPTY_USERNAME_ERROR_MSG)
    private String username;

    @JsonProperty("email")
    @NotEmpty(message = EMPTY_EMAIL_ERROR_MSG)
    @Email(message = INVALID_EMAIL_ERROR_MSG)
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = EMPTY_PASSWORD_ERROR_MSG)
    private String password;
}
