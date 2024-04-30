package com.stavaray.ms.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDetailsResponse {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<PhoneResponse> phones;
    private Date lastLogin;
    private String token;
    private boolean isActive;

}
