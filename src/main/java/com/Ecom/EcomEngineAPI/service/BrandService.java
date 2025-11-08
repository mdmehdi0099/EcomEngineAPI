package com.Ecom.EcomEngineAPI.service;


import com.Ecom.EcomEngineAPI.dto.*;
import com.Ecom.EcomEngineAPI.entity.Brand;
import com.Ecom.EcomEngineAPI.entity.Category;
import com.Ecom.EcomEngineAPI.repo.BrandRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
    @Transactional
    public Brand createBrand(BrandCreateDTO dto) {
        if (brandRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Brand already exists: " + dto.getName());
        }
        Brand b = Brand.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .logoURL(dto.getLogoUrl())
                .websiteURL(dto.getWebsiteUrl())
                .active(true)
                .build();
        return brandRepository.save(b);
    }

    public List<BrandResponseDTO> getAllBrands(){
        List<Brand> brands=brandRepository.findAll();
        return brands.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private BrandResponseDTO convertToResponseDTO(Brand brand){
        return BrandResponseDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .logoURL(brand.getLogoURL())
                .websiteURL(brand.getWebsiteURL())
                .active(brand.isActive())
                .deactivationReason(brand.getDeactivationReason())
                .deactivatedAt(brand.getDeactivatedAt())
                .reactivationReason(brand.getReactivationReason())
                .reactivatedAt(brand.getReactivatedAt())
                .build();
    }

    //update brand
    public BrandResponseDTO updateBrand(Long id, BrandRequestDTO dto){
        Brand brand=brandRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Brand not found with id: "+id));
        //update name if provided and should be unique
        if (dto.getName()!=null && dto.getName().isBlank()){
            if (!dto.getName().equalsIgnoreCase(brand.getName())&& brandRepository.existsByNameIgnoreCase(dto.getName())){
                throw new RuntimeException("Brand name already exists: "+dto.getName());
            }
            brand.setName(dto.getName());
        }
        if (dto.getDescription()!=null){
            brand.setDescription(dto.getDescription());
        }
        if (dto.isActive()){
            brand.setActive(dto.isActive());
            brand.setReactivationReason(dto.getReactivationReason());
            brand.setReactivatedAt(LocalDateTime.now());
        }else{
            brand.setActive(dto.isActive());
            brand.setDeactivationReason(dto.getDeactivationReason());
            brand.setDeactivatedAt(LocalDateTime.now());
        }
        Brand updatedBrand=brandRepository.save(brand);

        //return response DTO
        return BrandResponseDTO.builder()
                .id(updatedBrand.getId())
                .name(updatedBrand.getName())
                .description(updatedBrand.getDescription())
                .active(updatedBrand.isActive())
                .reactivationReason(updatedBrand.getReactivationReason())
                .reactivatedAt(updatedBrand.getReactivatedAt())
                .deactivationReason(updatedBrand.getDeactivationReason())
                .deactivatedAt(updatedBrand.getDeactivatedAt())
                .build();
    }


    public BrandResponseDTO getBrandById(Long id) {
        Brand brand=brandRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Brand not found with Id :"+id));
        return convertToResponseDTO(brand);
    }
}
