package com.Ecom.EcomEngineAPI.repo;

import com.Ecom.EcomEngineAPI.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
        Page<Product> findByCategoryCategoryId(Long categoryId, org.springframework.data.domain.Pageable pageable);
        Page<Product> findByBrandBrandId(Long brandId, org.springframework.data.domain.Pageable pageable);
        Page<Product> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable);
        Page<Product> findByBrandNameContainingIgnoreCase(String brandName, org.springframework.data.domain.Pageable pageable);

        //Combined filters
        Page<Product> findByCategoryCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name, org.springframework.data.domain.Pageable pageable);
        Page<Product> findByCategoryCategoryIdAndBrandNameContainingIgnoreCase(Long categoryid, String brandName, org.springframework.data.domain.Pageable pageable);


}
