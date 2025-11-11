package com.Ecom.EcomEngineAPI.repo;

import com.Ecom.EcomEngineAPI.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    // Fetch all cart items belonging to a specific user's cart
    List<CartItem> findByCart_CartId(Long cartId);

    // Optional: find all cart items by userId directly (if you store userId in Cart)
    List<CartItem> findByCart_UserId(Long userId);

    //List<CartItem> findByCart_User_UserId(Long userId);

    //List<CartItem> findByCart_UserId(Long userId);
}
