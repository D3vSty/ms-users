package com.stavaray.ms.users.controller;

import com.stavaray.ms.users.dto.request.SigninRequest;
import com.stavaray.ms.users.dto.response.ApiResponse;
import com.stavaray.ms.users.dto.response.JwtAuthenticationResponse;
import com.stavaray.ms.users.service.AuthService;
import com.stavaray.ms.users.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        SigninRequest request = Util.signinRequest();

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken("dummyToken");

        when(authService.login(any())).thenReturn(Optional.of(jwtAuthenticationResponse));

        ResponseEntity<ApiResponse<JwtAuthenticationResponse>> responseEntity = authController.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        assertEquals("Obtencion de Token", responseEntity.getBody().getMessage());
        assertEquals("dummyToken", responseEntity.getBody().getData().getToken());
    }

    @Test
    public void testLogin_Failure() {
        SigninRequest request = new SigninRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(authService.login(any())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<JwtAuthenticationResponse>> responseEntity = authController.login(request);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals(403, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        assertEquals("Email y/o clave incorrecta", responseEntity.getBody().getMessage());
    }

}
