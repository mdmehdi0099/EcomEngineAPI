package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.CategoryResponseDTO;
import com.Ecom.EcomEngineAPI.dto.ProductResponseDTO;
import com.Ecom.EcomEngineAPI.dto.ReviewRequestDTO;
import com.Ecom.EcomEngineAPI.dto.ReviewResponseDTO;
import com.Ecom.EcomEngineAPI.service.CategoryService;
import com.Ecom.EcomEngineAPI.service.ProductService;
import com.Ecom.EcomEngineAPI.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/free")
public class FreeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    //list of categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
        List<CategoryResponseDTO> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    //get the list of product
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    // Example: http://localhost:8083/free/product/search?categoryId=1&name=Smart Phone&page=0&size=20&sort=price,asc
    @GetMapping("/product/search")
    public ResponseEntity<org.springframework.data.domain.Page<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brandName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort.length > 1 ? sort[1] : "desc");
        String sortBy = sort[0];
        org.springframework.data.domain.Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductResponseDTO> results = productService.searchProducts(categoryId, name, brandName, (org.springframework.data.domain.Pageable) pageable);
        return ResponseEntity.ok(results);
    }

    // get product by id
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        ProductResponseDTO dto = productService.searchProducts(null, null, null, PageRequest.of(0,1))
                .stream().filter(p -> p.getProductId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(dto);
    }

    //review system
    //get review for a product
    @GetMapping("/review/product/{productid}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(reviewService.getReviewByProduct(productId));
    }

}
