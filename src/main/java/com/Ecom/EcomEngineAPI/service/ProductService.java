package com.Ecom.EcomEngineAPI.service;


import com.Ecom.EcomEngineAPI.dto.ProductCreateDTO;
import com.Ecom.EcomEngineAPI.dto.ProductResponseDTO;
import com.Ecom.EcomEngineAPI.entity.Brand;
import com.Ecom.EcomEngineAPI.entity.Category;
import com.Ecom.EcomEngineAPI.entity.Product;
import com.Ecom.EcomEngineAPI.entity.Role;
import com.Ecom.EcomEngineAPI.repo.BrandRepository;
import com.Ecom.EcomEngineAPI.repo.CategoryRepository;
import com.Ecom.EcomEngineAPI.repo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO dto) {
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product p = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .pictureURL(dto.getPictureUrl())
                .brand(brand)
                .category(category)
                .active(true)
                .createdBy(Role.ADMIN)
                .addedOn(LocalDateTime.now())
                .verified(true)
                .stockKeepingUnit(dto.getStockKeepingUnit())
                .inventoryCount(dto.getInventoryCount())
                .build();

        Product saved = productRepository.save(p);
        return mapToDto(saved);
    }

    @Transactional
    public ProductResponseDTO createProductBySeller(ProductCreateDTO dto) {
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product p = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .pictureURL(dto.getPictureUrl())
                .brand(brand)
                .category(category)
                .active(false)
                .createdBy(Role.SELLER)
                .addedOn(LocalDateTime.now())
                .verified(false)
                .stockKeepingUnit(dto.getStockKeepingUnit())
                .inventoryCount(dto.getInventoryCount())
                .build();

        Product saved = productRepository.save(p);
        return mapToDto(saved);
    }

    @Transactional
    public ProductResponseDTO createProductByAgent(ProductCreateDTO dto) {
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product p = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .pictureURL(dto.getPictureUrl())
                .brand(brand)
                .category(category)
                .active(false)
                .createdBy(Role.AGENT)
                .addedOn(LocalDateTime.now())
                .verified(false)
                .stockKeepingUnit(dto.getStockKeepingUnit())
                .inventoryCount(dto.getInventoryCount())
                .build();

        Product saved = productRepository.save(p);
        return mapToDto(saved);
    }
    //get the list of product as per created by role
    public List<ProductResponseDTO> getActiveProductsBySeller(Role role) {
        List<Product> products = productRepository.findByCreatedByAndActiveTrue(role);
        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }
    public List<ProductResponseDTO> getInActiveProductsBySeller(Role role) {
        List<Product> products = productRepository.findByCreatedByAndActiveFalse(role);
        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ProductResponseDTO convertToDTO(Product product){
        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .pictureUrl(product.getPictureURL())
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .active(product.isActive())
                .createdBy(String.valueOf(product.getCreatedBy()))
                .createdAt(product.getCreatedAt())
                .verified(product.isVerified())
                .build();
    }
    //verify the product added
    public ProductResponseDTO activateProductBySeller(Long productId, String verifiedReason, Role sellerRole) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // ✅ Check if product was created by an Agent
        if (product.getCreatedBy() != Role.AGENT) {
            throw new RuntimeException("Only products created by an Agent can be activated by a Seller.");
        }

        // ✅ Check if product is already active
        if (product.isActive()) {
            throw new RuntimeException("Product is already active.");
        }

        // ✅ Activate the product
        product.setActive(true);
        product.setVerifiedReason(
                verifiedReason != null ? verifiedReason : "Activated by Seller");
        product.setVerifiedBy(sellerRole); // Mark that seller verified/activated it
        product.setVerified(true);
        product.setVerifiedOn(LocalDateTime.now());

        Product updated = productRepository.save(product);

        return ProductResponseDTO.builder()
                .productId(updated.getProductId())
                .name(updated.getName())
                .description(updated.getDescription())
                .price(updated.getPrice())
                .active(updated.isActive())
                .verified(updated.isVerified())
                .build();
    }

    //Admin verify the product added
    public ProductResponseDTO activateProductByAdmin(Long productId, String verifiedReason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // ✅ Check if product is already active
        if (product.isActive()) {
            throw new RuntimeException("Product is already active.");
        }

        // ✅ Activate the product
        product.setActive(true);
        product.setVerifiedReason(
                verifiedReason != null ? verifiedReason : "Activated by Admin");
        product.setVerifiedBy(Role.ADMIN); // Mark that seller verified/activated it
        product.setVerified(true);
        product.setVerifiedOn(LocalDateTime.now());

        Product updated = productRepository.save(product);

        return ProductResponseDTO.builder()
                .productId(updated.getProductId())
                .name(updated.getName())
                .description(updated.getDescription())
                .price(updated.getPrice())
                .active(updated.isActive())
                .verified(updated.isVerified())
                .build();
    }



    // Viewing & filters: supports pagination and optional filters
    public Page<ProductResponseDTO> searchProducts(Long categoryId, String name, String brandName,
                                                   Pageable pageable) {

        // priority: category + name + brand; fall back to single filters
        if (categoryId != null && name != null && !name.isBlank()) {
            Page<Product> page = productRepository.findByCategoryCategoryIdAndNameContainingIgnoreCase(categoryId, name, pageable);
            return page.map(this::mapToDto);
        }
        if (categoryId != null && brandName != null && !brandName.isBlank()) {
            Page<Product> page = productRepository.findByCategoryCategoryIdAndBrandNameContainingIgnoreCase(categoryId, brandName, pageable);
            return page.map(this::mapToDto);
        }
        if (categoryId != null) {
            return productRepository.findByCategoryCategoryId(categoryId, pageable).map(this::mapToDto);
        }
        if (name != null && !name.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::mapToDto);
        }
        if (brandName != null && !brandName.isBlank()) {
            return productRepository.findByBrandNameContainingIgnoreCase(brandName, pageable).map(this::mapToDto);
        }

        // default: return all (paged)
        return productRepository.findAll(pageable).map(this::mapToDto);
    }
    private ProductResponseDTO mapToDto(Product p) {
        return ProductResponseDTO.builder()
                .productId(p.getProductId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .pictureUrl(p.getPictureURL())
                .brandId(p.getBrand() != null ? p.getBrand().getId() : null)
                .brandName(p.getBrand() != null ? p.getBrand().getName() : null)
                .categoryId(p.getCategory() != null ? p.getCategory().getCategoryId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .active(p.isActive())
                .verified(p.isVerified())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .inventoryCount(p.getInventoryCount())
                .stockKeepingUnit(p.getStockKeepingUnit())
                .build();
    }



}
