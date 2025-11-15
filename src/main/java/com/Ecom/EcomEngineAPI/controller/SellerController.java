package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.*;
import com.Ecom.EcomEngineAPI.entity.Order;
import com.Ecom.EcomEngineAPI.entity.OrderStatus;
import com.Ecom.EcomEngineAPI.entity.Role;
import com.Ecom.EcomEngineAPI.repo.CategoryRepository;
import com.Ecom.EcomEngineAPI.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    //seller profile
    @GetMapping("/myprofile")
    public ResponseEntity<UserProfileResponseDTO> viewMyProfile(HttpServletRequest request){
        ResponseEntity<UserProfileResponseDTO> userProfileResponseDTO=userService.getMyProfileDetails(request);
        return userProfileResponseDTO;
    }

    //list of categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
        List<CategoryResponseDTO> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    //create product
    @PostMapping("/products/create")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateDTO dto) {
        ProductResponseDTO created = productService.createProductBySeller(dto);
        return ResponseEntity.status(201).body(created);
    }

    //get the Agent added product and is active
    @GetMapping("/agentProductActive")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProductsByAgent(
            @RequestParam("role") Role sellerRole) {

        List<ProductResponseDTO> products = productService.getActiveProductsBySeller(sellerRole);
        return ResponseEntity.ok(products);
    }
    //get the agent added product and is InActive
    @GetMapping("/agentProductInActive")
    public ResponseEntity<List<ProductResponseDTO>> getInActiveProductsByAgent(
            @RequestParam("role") Role sellerRole) {

        List<ProductResponseDTO> products = productService.getInActiveProductsBySeller(sellerRole);
        return ResponseEntity.ok(products);
    }
    //verify the product added by agent
    // Example: PUT /products/activate/12?sellerRole=SELLER
    @PutMapping("/activate/{productId}")
    public ResponseEntity<ProductResponseDTO> activateProductBySeller(
            @PathVariable Long productId,
            @RequestParam("role") Role role,
            @RequestBody(required = false) String verifiedReason) {

        ProductResponseDTO updated = productService.activateProductBySeller(productId, verifiedReason, role);
        return ResponseEntity.ok(updated);
    }

    //get the list of product
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.listAllProducts();
        return ResponseEntity.ok(products);
    }


    // Example: http://localhost:8083/seller/product/search?categoryId=1&name=Smart Phone&page=0&size=20&sort=price,asc
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


    //Add to cart
    @PostMapping("/cart/{customerId}/add")
    public ResponseEntity<CartResponseDTO> addToCart(@PathVariable Long customerId,
                                                     @RequestBody AddToCartDTO dto) {
        CartResponseDTO response = cartService.addToCart(customerId, dto);
        return ResponseEntity.ok(response);
    }

    //get cart items
    @GetMapping("/cart/{customerId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long customerId) {
        CartResponseDTO response = cartService.getCart(customerId);
        return ResponseEntity.ok(response);
    }

    //add the address
    @PostMapping("/address/add")
    public ResponseEntity<AddressResponseDTO> addAddress(@RequestBody AddressCreateDTO dto) {
        AddressResponseDTO response = addressService.addAddress(dto);
        return ResponseEntity.status(201).body(response);
    }
    //get the address
    @GetMapping("/address/user/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddressesByUser(userId));
    }
    //get the default address
    @GetMapping("/address/user/{userId}/default")
    public ResponseEntity<AddressResponseDTO> getDefaultAddress(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getDefaultAddress(userId));
    }

    //payment
    @PostMapping("/payment/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO dto) {
        PaymentResponseDTO response = paymentService.processPayment(dto);
        return ResponseEntity.status(201).body(response);
    }

    //place an order
    @PostMapping("/oder/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO dto) {
        Order order = orderService.placeOrder(dto);
        OrderResponseDTO responsedto=orderService.convertToDTO(order);
        return ResponseEntity.status(201).body(responsedto);
    }

    //get orders by userId
    @GetMapping("/oder/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponseDTO> response=orderService.getOrdersByUserDTO(userId);
        return ResponseEntity.ok(response);
    }

    //get orders by status
    @GetMapping("/oder/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }
    //cancel order api
    @PutMapping("/order/{orderId}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long orderId) {
        OrderResponseDTO response = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(response);
    }


}
