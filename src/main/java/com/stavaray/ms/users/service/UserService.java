package com.stavaray.ms.users.service;

import com.stavaray.ms.users.dto.request.UserSaveRequest;
import com.stavaray.ms.users.dto.request.UserUpdateRequest;
import com.stavaray.ms.users.dto.response.UserDetailsResponse;
import com.stavaray.ms.users.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserDetailsResponse> findById(Long id);

    List<UserDetailsResponse> findAll();

    Optional<UserResponse> saveUser(String token, UserSaveRequest userSaveRequest);

    Optional<UserResponse> updateUser(String token, UserUpdateRequest userUpdateRequest);

    Boolean deleteUser(Long id);


}
