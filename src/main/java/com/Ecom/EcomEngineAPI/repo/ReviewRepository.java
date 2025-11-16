package com.Ecom.EcomEngineAPI.repo;

import com.Ecom.EcomEngineAPI.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByProductId(Long productId);
    List<Review> findByUserId(Long userId);

    boolean existsByProductIdAndUserIdAndOrderId(Long productId,Long userId,Long orderId);

}
