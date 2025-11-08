package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.ProductCreateDTO;
import com.Ecom.EcomEngineAPI.dto.ProductResponseDTO;
import com.Ecom.EcomEngineAPI.dto.UserProfileResponseDTO;
import com.Ecom.EcomEngineAPI.service.ProductService;
import com.Ecom.EcomEngineAPI.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    //Agent profile
    @GetMapping("/myprofile")
    public ResponseEntity<UserProfileResponseDTO> viewMyProfile(HttpServletRequest request){
        ResponseEntity<UserProfileResponseDTO> userProfileResponseDTO=userService.getMyProfileDetails(request);
        return userProfileResponseDTO;
    }

    // Seller (AGENT) uploads product
    //@PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    @PostMapping("/products/upload")
    public ResponseEntity<ProductResponseDTO> uploadProduct(@RequestBody ProductCreateDTO dto) {
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.status(201).body(created);
    }

    // Users can view / search products (no auth required or ROLE_USER)
    // Example: /products/search?categoryId=1&name=mouse&brandName=logitech&page=0&size=20&sort=price,asc
    @GetMapping("/product/search")
    public ResponseEntity<org.springframework.data.domain.Page<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brandName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort.length > 1 ? sort[1] : "desc");
        String sortBy = sort[0];
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(direction, sortBy));

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



}
