package com.example.springwebtask.form;

import jakarta.validation.constraints.NotBlank;
import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank(message = "IDは必須です。")
    private String loginId;

    @NotBlank(message = "パスワードは必須です。")
    private String password;
}
