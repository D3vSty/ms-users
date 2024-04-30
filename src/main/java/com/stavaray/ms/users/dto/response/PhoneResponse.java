package com.stavaray.ms.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PhoneResponse {

    private Long id;
    private String number;
    private String citycode;
    private String countrycode;

}
