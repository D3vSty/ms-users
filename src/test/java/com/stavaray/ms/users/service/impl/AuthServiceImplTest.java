package com.stavaray.ms.users.service.impl;

import com.stavaray.ms.users.dto.request.SigninRequest;
import com.stavaray.ms.users.dto.response.JwtAuthenticationResponse;
import com.stavaray.ms.users.entity.User;
import com.stavaray.ms.users.jwt.JwtService;
import com.stavaray.ms.users.repository.UserRepository;
import com.stavaray.ms.users.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testLogin_Success() {
        SigninRequest request = Util.signinRequest();
        User user = Util.user();
        String token = "dummyToken";

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn(token);

        JwtAuthenticationResponse expectedResponse = JwtAuthenticationResponse.builder().token(token).build();
        Optional<JwtAuthenticationResponse> expectedOptional = Optional.of(expectedResponse);

        Optional<JwtAuthenticationResponse> actualResponse = authService.login(request);

        assertEquals(expectedOptional, actualResponse);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin_AuthenticationFails_ReturnsEmptyOptional() {

        SigninRequest request = Util.signinRequest();

        doThrow(new RuntimeException("Authentication failed"))
                .when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Optional<JwtAuthenticationResponse> actualResult = authService.login(request);

        assertTrue(actualResult.isEmpty(), "Expected an empty Optional but was not");

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        verify(userRepository, never()).findByEmail(anyString());
    }


}
