package com.stavaray.ms.users.controller;

import com.stavaray.ms.users.dto.request.UserSaveRequest;
import com.stavaray.ms.users.dto.request.UserUpdateRequest;
import com.stavaray.ms.users.dto.response.ApiResponse;
import com.stavaray.ms.users.dto.response.UserDetailsResponse;
import com.stavaray.ms.users.dto.response.UserResponse;
import com.stavaray.ms.users.service.UserService;
import com.stavaray.ms.users.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_UserFound() {
        UserDetailsResponse userDetailsResponse = Util.userDetailsResponseList().get(0);

        when(userService.findById(anyLong())).thenReturn(Optional.of(userDetailsResponse));

        ResponseEntity<ApiResponse<UserDetailsResponse>> responseEntity = userController.findById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(responseEntity.getBody()).getData().getId());
    }

    @Test
    public void testFindById_UserNotFound() {
        when(userService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<UserDetailsResponse>> responseEntity = userController.findById(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No se encontró usuario con ID: 1", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testFindAll_UsersFound() {
        List<UserDetailsResponse> userDetailsResponseList = Util.userDetailsResponseList();

        when(userService.findAll()).thenReturn(userDetailsResponseList);

        ResponseEntity<ApiResponse<List<UserDetailsResponse>>> responseEntity = userController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(responseEntity.getBody()).getData().size());
    }

    @Test
    public void testFindAll_NoUsersFound() {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<ApiResponse<List<UserDetailsResponse>>> responseEntity = userController.findAll();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No se encontraron usuarios", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testSaveUser_Success() {
        UserSaveRequest saveRequest = Util.createUserSaveRequest();

        when(userService.saveUser(any(), any())).thenReturn(Optional.of(new UserResponse()));

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.saveUser("Bearer token", saveRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSaveUser_DuplicateEmail() {
        UserSaveRequest saveRequest = Util.createUserSaveRequest();

        when(userService.saveUser(any(), any())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.saveUser("Bearer token", saveRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("El correo electrónico ya está registrado", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateUser_Success() {
        UserUpdateRequest updateRequest = Util.updateRequest();

        when(userService.updateUser(any(), any())).thenReturn(Optional.of(new UserResponse()));

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.updateUser("Bearer token", updateRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(1L);

        when(userService.updateUser(any(), any())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.updateUser("Bearer token", updateRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No se encontro usuario con id: 1", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;

        when(userService.deleteUser(userId)).thenReturn(true);

        ResponseEntity<ApiResponse<Object>> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        Long userId = 1L;

        when(userService.deleteUser(userId)).thenReturn(false);

        ResponseEntity<ApiResponse<Object>> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Eliminacion de Usuario por ID", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

}