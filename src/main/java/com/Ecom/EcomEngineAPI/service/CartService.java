package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.AddToCartDTO;
import com.Ecom.EcomEngineAPI.dto.CartItemResponseDTO;
import com.Ecom.EcomEngineAPI.dto.CartResponseDTO;
import com.Ecom.EcomEngineAPI.entity.Cart;
import com.Ecom.EcomEngineAPI.entity.CartItem;
import com.Ecom.EcomEngineAPI.entity.Product;
import com.Ecom.EcomEngineAPI.repo.CartRepository;
import com.Ecom.EcomEngineAPI.repo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;



    @Transactional
    public CartResponseDTO addToCart(Long userId, AddToCartDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() ->{
                    Cart newCart=Cart.builder()
                        .userId(userId)
                        .totalItems(0L)
                        .totalAmount(0L)
                        .cartItems(new ArrayList<>())
                        .build();
                    return newCart;
                        });
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        CartItem existingItem = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getProductId().equals(dto.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            existingItem.setTotalPrice(existingItem.getQuantity() * existingItem.getPricePerUnit());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .pricePerUnit(product.getPrice())
                    .quantity(dto.getQuantity())
                    .totalPrice(product.getPrice() * dto.getQuantity())
                    .build();
            cart.getCartItems().add(newItem);
        }

        // Recalculate totals
        long totalAmount = cart.getCartItems().stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        long totalItems = cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum();

        cart.setTotalAmount(totalAmount);
        cart.setTotalItems(totalItems);

        Cart savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }


    public CartResponseDTO getCart(Long customerId) {
        Cart cart = cartRepository.findByUserId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return mapToResponse(cart);
    }

    private CartResponseDTO mapToResponse(Cart cart) {
        List<CartItemResponseDTO> itemDTOs = cart.getCartItems().stream()
                .map(item -> CartItemResponseDTO.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getName())
                        .pricePerUnit(item.getPricePerUnit())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return CartResponseDTO.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .totalItems(cart.getTotalItems())
                .totalAmount(cart.getTotalAmount())
                .items(itemDTOs)
                .build();
    }



}
