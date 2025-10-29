package com.Ecom.EcomEngineAPI.repo;

import com.Ecom.EcomEngineAPI.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
        Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
        Page<Product> findByBrandBrandId(Long brandId,Pageable pageable);
        Page<Product> findByNameContainingIgnoreCase(String name,Pageable pageable);
        Page<Product> findByBrandNameContainingIgnoreCase(String brandName,Pageable pageable);

        //Combined filters
        Page<Product> findByCategoryCategoryIdAndNameContainingIgnoreCase(Long categoryId,String name,Pageable pageable);
        Page<Product> findByCategoryCategoryIdAndBrandNameContainingIgnoreCase(Long categoryid,String brandName,Pageable pageable);


}
