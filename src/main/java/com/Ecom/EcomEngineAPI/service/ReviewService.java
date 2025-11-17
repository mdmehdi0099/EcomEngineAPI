package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.ReviewRequestDTO;
import com.Ecom.EcomEngineAPI.dto.ReviewResponseDTO;
import com.Ecom.EcomEngineAPI.entity.Review;
import com.Ecom.EcomEngineAPI.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    //create review
    public ReviewResponseDTO createReview(ReviewRequestDTO dto){
        //prevent the user for creating the duplicate review for the same product and orderId
        if(reviewRepository.existsByProductIdAndUserIdAndOrderId(dto.getProductId(), dto.getUserId(), dto.getProductId())){
            throw new RuntimeException("Review already submitted for this product in this order");
        }
        Review review=Review.builder()
                .productId(dto.getProductId())
                .orderId(dto.getOrderId())
                .userId(dto.getUserId())
                .starRate(dto.getStarRate())
                .remark(dto.getRemark())
                .other(dto.getOther())
                .enable(true)
                .build();
        Review savedReview=reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    //getReviewsByProduct
    public List<ReviewResponseDTO> getReviewByProduct(Long productId){
        if (productId==null){
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        List<Review> reviews=reviewRepository.findByProductId(productId);

        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //getReviewsByUser
    public List<ReviewResponseDTO> getReviewByUser(Long userId){
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    //enableDisableReview
    public ReviewResponseDTO enableDisableReview(Long reviewId,boolean status){
        Review review=reviewRepository.findById(reviewId)
                .orElseThrow(()->new RuntimeException("Review not found"));
        review.setEnable(status);
        Review updatedReview=reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }




    //DTO converter
    private ReviewResponseDTO convertToDTO(Review review){
        return ReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .productId(review.getProductId())
                .orderId(review.getOrderId())
                .userId(review.getUserId())
                .starRate(review.getStarRate())
                .remark(review.getRemark())
                .other(review.getOther())
                .enable(review.isEnable())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }


}
