package com.stavaray.ms.users.service;

import com.stavaray.ms.users.dto.request.SigninRequest;
import com.stavaray.ms.users.dto.response.JwtAuthenticationResponse;

import java.util.Optional;

public interface AuthService {
    Optional<JwtAuthenticationResponse> login(SigninRequest request);

}
