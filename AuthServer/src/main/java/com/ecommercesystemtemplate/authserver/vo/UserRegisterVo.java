package com.ecommercesystemtemplate.authserver.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author lhjls
 */
@Data
public class UserRegisterVo {

    @NotEmpty(message = "Username cannot be empty")
    @Length(min = 6, max = 18, message = "Username must be between 6 and 18 characters")
    private String userName;

    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, max = 18, message = "Password must be between 6 and 18 characters")
    private String password;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Incorrect phone number format")
    @NotEmpty(message = "Phone number cannot be empty")
    private String phone;

    @NotEmpty(message = "Verification code cannot be empty")
    private String code;
}
