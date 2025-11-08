package com.Ecom.EcomEngineAPI.service;


import com.Ecom.EcomEngineAPI.dto.CategoryCreateDTO;
import com.Ecom.EcomEngineAPI.entity.Category;
import com.Ecom.EcomEngineAPI.repo.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Transactional
    public Category createCategory(CategoryCreateDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Category already exists: " + dto.getName());
        }
        Category c = Category.builder()
                .name(dto.getName())
                .active(true)
                .build();
        return categoryRepository.save(c);
    }



}
