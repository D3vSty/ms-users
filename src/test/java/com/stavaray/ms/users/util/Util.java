package com.stavaray.ms.users.util;

import com.stavaray.ms.users.dto.request.*;
import com.stavaray.ms.users.dto.response.UserDetailsResponse;
import com.stavaray.ms.users.entity.Phone;
import com.stavaray.ms.users.entity.Role;
import com.stavaray.ms.users.entity.User;

import java.util.Date;
import java.util.List;

public class Util {

    public static SigninRequest signinRequest() {
        return SigninRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }

    public static User user() {
        return User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .phones(phones())
                .token("token")
                .created(new Date())
                .modified(new Date())
                .lastLogin(new Date())
                .role(Role.admin)
                .isActive(true).build();
    }

    public static List<Phone> phones() {
        return List.of(Phone.builder()
                .id(1L)
                .number("955050906")
                .cityCode("+51")
                .countryCode("00")
                .build());
    }

    public static UserSaveRequest createUserSaveRequest() {
        return UserSaveRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .phones(List.of(PhoneSaveRequest.builder()
                        .number("123456789")
                        .cityCode("1")
                        .countryCode("1")
                        .build())).build();

    }

    private static List<PhoneUpdateRequest> phonesUpdate() {
        return List.of(PhoneUpdateRequest.builder()
                .id(1L)
                .number("955050906")
                .cityCode("+51")
                .countryCode("00")
                .build());
    }

    public static UserUpdateRequest updateRequest() {
        return UserUpdateRequest.builder()
                .id(1L)
                .name("Updated User")
                .email("updated@example.com")
                .password("updated_password")
                .phones(phonesUpdate())
                .build();
    }

    public static List<UserDetailsResponse> userDetailsResponseList() {
        return List.of(UserDetailsResponse.builder()
                .id(1L)
                .name("Simon")
                .email("test@example.com")
                .password("password")
                .phones(null)
                .lastLogin(new Date())
                .token("token")
                .isActive(true)
                .build());
    }

}
