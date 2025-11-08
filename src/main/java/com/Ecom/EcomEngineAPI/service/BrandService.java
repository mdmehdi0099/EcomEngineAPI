package com.Ecom.EcomEngineAPI.service;


import com.Ecom.EcomEngineAPI.dto.BrandCreateDTO;
import com.Ecom.EcomEngineAPI.entity.Brand;
import com.Ecom.EcomEngineAPI.repo.BrandRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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




}
