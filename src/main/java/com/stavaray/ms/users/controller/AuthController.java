package com.stavaray.ms.users.controller;

import com.stavaray.ms.users.dto.request.SigninRequest;
import com.stavaray.ms.users.dto.response.ApiResponse;
import com.stavaray.ms.users.dto.response.JwtAuthenticationResponse;
import com.stavaray.ms.users.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(@RequestBody SigninRequest request) {
        Optional<JwtAuthenticationResponse> userLogin = authService.login(request);
        return userLogin.map(userResponse ->
                        ResponseEntity.ok(new ApiResponse<>(200, "Obtencion de Token", userResponse)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(403, "Email y/o clave incorrecta", null)));

    }

}
