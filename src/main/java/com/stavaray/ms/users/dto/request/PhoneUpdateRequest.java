package com.stavaray.ms.users.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PhoneUpdateRequest {
    private Long id;
    private String number;
    private String cityCode;
    private String countryCode;

}
