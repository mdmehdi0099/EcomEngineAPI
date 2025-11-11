package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressCreateDTO {
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private String streetAddress;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private boolean defaultAddress;
}
