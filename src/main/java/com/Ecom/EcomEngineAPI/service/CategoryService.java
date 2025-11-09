package com.Ecom.EcomEngineAPI.service;


import com.Ecom.EcomEngineAPI.dto.CategoryCreateDTO;
import com.Ecom.EcomEngineAPI.dto.CategoryResponseDTO;
import com.Ecom.EcomEngineAPI.dto.CategoryUpdateDTO;
import com.Ecom.EcomEngineAPI.dto.CategoryUpdateResponseDTO;
import com.Ecom.EcomEngineAPI.entity.Category;
import com.Ecom.EcomEngineAPI.repo.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                .description(dto.getDescription())
                .active(true)
                .build();
        return categoryRepository.save(c);
    }
    //update categories
    public CategoryUpdateResponseDTO updateCategory(Long id, CategoryUpdateDTO dto){
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found with id: "+id));
        //update name if provided and should be unique
        if (dto.getName()!=null && dto.getName().isBlank()){
            if (!dto.getName().equalsIgnoreCase(category.getName())&& categoryRepository.existsByNameIgnoreCase(dto.getName())){
                throw new RuntimeException("Category name already exists: "+dto.getName());
            }
            category.setName(dto.getName());
        }
        if (dto.getDescription()!=null){
            category.setDescription(dto.getDescription());
        }
        if (dto.isActive()){
            category.setActive(dto.isActive());
            category.setReactivationReason(dto.getReactivationReason());
            category.setReactivatedAt(LocalDateTime.now());
        }else{
            category.setActive(dto.isActive());
            category.setDeactivationReason(dto.getDeactivationReason());
            category.setDeactivatedAt(LocalDateTime.now());
        }
        Category updatedCategory=categoryRepository.save(category);

        //return response DTO
        return CategoryUpdateResponseDTO.builder()
                .id(updatedCategory.getCategoryId())
                .name(updatedCategory.getName())
                .description(updatedCategory.getDescription())
                .active(updatedCategory.isActive())
                .reactivationReason(updatedCategory.getReactivationReason())
                .reactivatedAt(updatedCategory.getReactivatedAt())
                .deactivationReason(updatedCategory.getDeactivationReason())
                .deactivatedAt(updatedCategory.getDeactivatedAt())
                .build();
    }

    public List<CategoryResponseDTO> getAllCategories(){
        List<Category> categories=categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private CategoryResponseDTO convertToResponseDTO(Category category){
        return CategoryResponseDTO.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }


}
