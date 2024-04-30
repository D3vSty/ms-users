package com.stavaray.ms.users.service.impl;

import com.stavaray.ms.users.dto.request.UserSaveRequest;
import com.stavaray.ms.users.dto.request.UserUpdateRequest;
import com.stavaray.ms.users.dto.response.UserDetailsResponse;
import com.stavaray.ms.users.dto.response.UserResponse;
import com.stavaray.ms.users.entity.Phone;
import com.stavaray.ms.users.entity.User;
import com.stavaray.ms.users.repository.PhoneRepository;
import com.stavaray.ms.users.repository.UserRepository;
import com.stavaray.ms.users.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_UserExists_ReturnsUserDetailsResponse() {

        Long userId = 1L;
        User mockUser = Util.user();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        Optional<UserDetailsResponse> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository).findById(userId);

    }

    @Test
    void findById_UserDoesNotExist_ReturnsEmpty() {

        Long userId = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<UserDetailsResponse> result = userService.findById(userId);

        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);

    }

    @Test
    void findAll_ReturnsAllUsers() {

        List<User> usersList = List.of(Util.user());
        when(userRepository.findAll()).thenReturn(usersList);

        List<UserDetailsResponse> users = userService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findAll();

    }

    @Test
    void findAll_ReturnsListEmpty() {

        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDetailsResponse> users = userService.findAll();

        assertNotNull(users);
        assertEquals(0, users.size());

        verify(userRepository, times(1)).findAll();

    }

    @Test
    void saveUser_UserAlreadyExists_ReturnsEmpty() {

        UserSaveRequest request = Util.createUserSaveRequest();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        Optional<UserResponse> result = userService.saveUser("token", request);

        assertTrue(result.isEmpty());

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void saveUser_NewUser_ReturnsUserResponse() {

        UserSaveRequest request = Util.createUserSaveRequest();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<UserResponse> result = userService.saveUser("token", request);

        assertTrue(result.isPresent());

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() {

        UserUpdateRequest updateRequest = Util.updateRequest();
        Long userId = 1L;
        User existingUser = Util.user();

        Phone existingPhone = Util.phones().get(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(phoneRepository.findById(anyLong())).thenReturn(Optional.of(existingPhone));

        Optional<UserResponse> result = userService.updateUser("token", updateRequest);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
        verify(phoneRepository).findById(anyLong());

    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser_PhonesNotEquals() {

        UserUpdateRequest updateRequest = Util.updateRequest();
        Long userId = 1L;
        User existingUser = Util.user();
        existingUser.getPhones().get(0).setId(4L);
        Phone existingPhone = Util.phones().get(0);
        existingPhone.setId(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(phoneRepository.findById(anyLong())).thenReturn(Optional.of(existingPhone));

        Optional<UserResponse> result = userService.updateUser("token", updateRequest);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
        verify(phoneRepository).findById(anyLong());

    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser_NotPhones() {

        UserUpdateRequest updateRequest = Util.updateRequest();
        Long userId = 1L;
        User existingUser = Util.user();
        existingUser.setPhones(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<UserResponse> result = userService.updateUser("token", updateRequest);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));

    }

    @Test
    void updateUser_UserDoesNotExist_ReturnsEmpty() {

        UserUpdateRequest updateRequest = Util.updateRequest();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.updateUser("token", updateRequest);

        assertTrue(result.isEmpty());

        verify(userRepository).findById(updateRequest.getId());
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void deleteUser_UserExists_ReturnsTrue() {
        Long userId = 1L;
        User user = Util.user();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Boolean result = userService.deleteUser(userId);

        assertTrue(result);

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);

    }

    @Test
    void deleteUser_UserExists_ReturnsTrue_NotPhones() {
        Long userId = 1L;
        User user = Util.user();
        user.setPhones(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Boolean result = userService.deleteUser(userId);

        assertTrue(result);

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);

    }

    @Test
    void deleteUser_UserDoesNotExist_ReturnsFalse() {

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Boolean result = userService.deleteUser(userId);

        assertFalse(result);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).deleteById(userId);

    }


    @Test
    void testEncryptAllUserPasswords() {
        User user2 = Util.user();
        user2.setId(2L);
        List<User> users = List.of(Util.user(), user2);
        when(userRepository.findAll()).thenReturn(users);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.init();
        userService.encryptAllUserPasswords();

        verify(userRepository, times(4)).save(any(User.class));
        assertEquals("encodedPassword", users.get(0).getPassword());

    }

}

