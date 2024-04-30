package com.stavaray.ms.users.service.impl;

import com.stavaray.ms.users.dto.request.SigninRequest;
import com.stavaray.ms.users.dto.response.JwtAuthenticationResponse;
import com.stavaray.ms.users.entity.User;
import com.stavaray.ms.users.jwt.JwtService;
import com.stavaray.ms.users.repository.UserRepository;
import com.stavaray.ms.users.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Optional<JwtAuthenticationResponse> login(SigninRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Optional<User> user = userRepository.findByEmail(request.getEmail());
            String token = jwtService.getToken(user.get());
            User userUpdate = user.get();
            userUpdate.setToken(token);
            userUpdate.setLastLogin(new Date());
            userRepository.save(userUpdate);
            JwtAuthenticationResponse tokenB = JwtAuthenticationResponse.builder()
                    .token(token)
                    .build();
            return Optional.of(tokenB);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
