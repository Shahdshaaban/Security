package com.test.demo.Response;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;
    private String refreshToken;

}
