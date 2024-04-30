package com.stavaray.ms.users.controller;

import com.stavaray.ms.users.dto.request.UserSaveRequest;
import com.stavaray.ms.users.dto.request.UserUpdateRequest;
import com.stavaray.ms.users.dto.response.*;
import com.stavaray.ms.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserDetailsResponse>> findById(@PathVariable("id") Long id) {
        Optional<UserDetailsResponse> userDetailsResponse = userService.findById(id);
        return userDetailsResponse.map(detailsResponse ->
                        ResponseEntity.ok(new ApiResponse<>(200, "Obtención de Usuario por ID", detailsResponse)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "No se encontró usuario con ID: " + id, null)));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<UserDetailsResponse>>> findAll() {
        List<UserDetailsResponse> userDetailsList = userService.findAll();
        if (!userDetailsList.isEmpty())
            return ResponseEntity.ok(new ApiResponse<>(200, "Obtención de todos los usuarios", userDetailsList));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, "No se encontraron usuarios", null));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponse>> saveUser(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody UserSaveRequest saveRequest) {
        String token = authorizationHeader.replace("Bearer ", "");
        Optional<UserResponse> user = userService.saveUser(token, saveRequest);
        return user.map(userResponse ->
                        ResponseEntity.ok(new ApiResponse<>(200, "Usuario guardado con exito", userResponse)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(400, "El correo electrónico ya está registrado", null)));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        String token = authorizationHeader.replace("Bearer ", "");
        Optional<UserResponse> user = userService.updateUser(token, userUpdateRequest);
        return user.map(userResponse ->
                        ResponseEntity.ok(new ApiResponse<>(200, "Usuario actualizado con exito", userResponse)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "No se encontro usuario con id: " + userUpdateRequest.getId(), null)));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable("id") Long id) {
        if (userService.deleteUser(id))
            return ResponseEntity.ok(new ApiResponse<>(200, "Eliminacion de Usuario por ID", new MessageResponse("Usuario con id: " + id + " eliminado con éxito")));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, "Eliminacion de Usuario por ID", new ErrorResponse("No se encontro usuario con id: " + id)));
    }

}
