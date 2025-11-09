package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.AddressCreateDTO;
import com.Ecom.EcomEngineAPI.dto.AddressResponseDTO;
import com.Ecom.EcomEngineAPI.entity.Address;
import com.Ecom.EcomEngineAPI.repo.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;


    public AddressResponseDTO addAddress(AddressCreateDTO dto) {
        if (dto.isDefaultAddress()) {
            // ensure only one default per user
            addressRepository.findByUserIdAndDefaultAddressTrue(dto.getUserId())
                    .ifPresent(existing -> {
                        existing.setDefaultAddress(false);
                        addressRepository.save(existing);
                    });
        }

        Address address = Address.builder()
                .userId(dto.getUserId())
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .streetAddress(dto.getStreetAddress())
                .landmark(dto.getLandmark())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .defaultAddress(dto.isDefaultAddress())
                .build();

        Address saved = addressRepository.save(address);
        return mapToDTO(saved);
    }
    public List<AddressResponseDTO> getAddressesByUser(Long userId) {
        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public AddressResponseDTO getDefaultAddress(Long userId) {
        return addressRepository.findByUserIdAndDefaultAddressTrue(userId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("No default address found"));
    }
    private AddressResponseDTO mapToDTO(Address address) {
        return AddressResponseDTO.builder()
                .addressId(address.getAddressId())
                .userId(address.getUserId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .streetAddress(address.getStreetAddress())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .defaultAddress(address.isDefaultAddress())
                .build();
    }



}
