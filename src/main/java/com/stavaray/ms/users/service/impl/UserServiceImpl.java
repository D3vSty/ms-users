package com.stavaray.ms.users.service.impl;

import com.stavaray.ms.users.dto.request.PhoneSaveRequest;
import com.stavaray.ms.users.dto.request.UserSaveRequest;
import com.stavaray.ms.users.dto.request.UserUpdateRequest;
import com.stavaray.ms.users.dto.response.PhoneResponse;
import com.stavaray.ms.users.dto.response.UserDetailsResponse;
import com.stavaray.ms.users.dto.response.UserResponse;
import com.stavaray.ms.users.entity.Phone;
import com.stavaray.ms.users.entity.Role;
import com.stavaray.ms.users.entity.User;
import com.stavaray.ms.users.repository.PhoneRepository;
import com.stavaray.ms.users.repository.UserRepository;
import com.stavaray.ms.users.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        encryptAllUserPasswords();
    }

    public void encryptAllUserPasswords() {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
        }
    }

    @Override
    public Optional<UserDetailsResponse> findById(Long id) {
        Optional<User> x = userRepository.findById(id);
        if (x.isPresent()) {
            List<PhoneResponse> phones = new ArrayList<>();
            x.get().getPhones().forEach((item) -> {
                phones.add(PhoneResponse.builder()
                        .id(item.getId())
                        .number(item.getNumber())
                        .citycode(item.getCityCode())
                        .countrycode(item.getCountryCode())
                        .build());
            });
            return Optional.of(UserDetailsResponse.builder()
                    .id(x.get().getId())
                    .name(x.get().getName())
                    .email(x.get().getEmail())
                    .password(x.get().getPassword())
                    .phones(phones)
                    .lastLogin(x.get().getLastLogin())
                    .token(x.get().getToken())
                    .isActive(x.get().isActive())
                    .build());

        }
        return Optional.empty();

    }

    @Override
    public List<UserDetailsResponse> findAll() {
        List<User> x = userRepository.findAll();
        List<UserDetailsResponse> users = new ArrayList<>();
        if (!x.isEmpty()) {
            x.forEach((item) -> {
                List<PhoneResponse> phones = new ArrayList<>();
                item.getPhones().forEach((phone) -> {
                    phones.add(PhoneResponse.builder()
                            .id(phone.getId())
                            .number(phone.getNumber())
                            .citycode(phone.getCityCode())
                            .countrycode(phone.getCountryCode())
                            .build());
                });
                users.add(UserDetailsResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .email(item.getEmail())
                        .password(item.getPassword())
                        .phones(phones)
                        .lastLogin(item.getLastLogin())
                        .token(item.getToken())
                        .isActive(item.isActive())
                        .build());
            });
            return users;
        }
        return List.of();
    }

    @Override
    @Transactional
    public Optional<UserResponse> saveUser(String token, UserSaveRequest userSaveRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userSaveRequest.getEmail());
        if (existingUser.isPresent())
            return Optional.empty();

        User newUser = createUserFromRequest(token, userSaveRequest);
        User savedUser = userRepository.save(newUser);

        List<Phone> phones = phoneRepository.saveAll(createPhonesForUser(userSaveRequest.getPhones(), savedUser));

        return Optional.of(buildUserResponse(savedUser));

    }


    @Override
    public Optional<UserResponse> updateUser(String token, UserUpdateRequest userUpdateRequest) {
        Optional<User> existingUser = userRepository.findById(userUpdateRequest.getId());
        if (existingUser.isPresent()) {
            User userUpdate = existingUser.get();
            userUpdate.setName(userUpdateRequest.getName());
            userUpdate.setEmail(userUpdateRequest.getEmail());
            userUpdate.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
            if (!userUpdate.getPhones().isEmpty()) {
                userUpdate.getPhones().forEach((item) -> {
                    Optional<Phone> phone = phoneRepository.findById(item.getId());
                    Phone updatePhone = phone.get();
                    userUpdateRequest.getPhones().forEach((phoneItem) -> {
                        if (Objects.equals(item.getId(), phoneItem.getId())) {
                            updatePhone.setNumber(phoneItem.getNumber());
                            updatePhone.setCityCode(phoneItem.getCityCode());
                            updatePhone.setCountryCode(phoneItem.getCountryCode());
                            phoneRepository.save(updatePhone);
                        }
                    });

                });
            }
            User user = userRepository.save(userUpdate);
            return Optional.of(buildUserResponse(user));

        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteUser(Long id) {
        Optional<User> x = userRepository.findById(id);
        if (x.isPresent()) {
            if (!x.get().getPhones().isEmpty())
                x.get().getPhones().forEach((item) -> phoneRepository.deleteById(item.getId()));
            userRepository.deleteById(x.get().getId());
            return true;
        }
        return false;
    }

    private User createUserFromRequest(String token, UserSaveRequest userSaveRequest) {
        return User.builder()
                .name(userSaveRequest.getName())
                .email(userSaveRequest.getEmail())
                .password(passwordEncoder.encode(userSaveRequest.getPassword()))
                .token(token)
                .created(new Date())
                .modified(new Date())
                .lastLogin(new Date())
                .role(Role.admin)
                .isActive(true).build();
    }

    private List<Phone> createPhonesForUser(List<PhoneSaveRequest> phoneSaveRequests, User user) {
        return phoneSaveRequests.stream().map(phoneSaveRequest ->
                        Phone.builder()
                                .number(phoneSaveRequest.getNumber())
                                .cityCode(phoneSaveRequest.getCityCode())
                                .countryCode(phoneSaveRequest.getCountryCode())
                                .users(user)
                                .build())
                .toList();
    }

    private UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .build();
    }
}
