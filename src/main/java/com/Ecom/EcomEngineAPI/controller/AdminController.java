package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.*;
import com.Ecom.EcomEngineAPI.entity.Brand;
import com.Ecom.EcomEngineAPI.entity.Category;
import com.Ecom.EcomEngineAPI.entity.Role;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    //registerSellerUser
    @PostMapping("/registerSellerUser")
    public ResponseEntity<?> registerSellerUser(@RequestBody RegisterRequest request) {
        return authService.registerSellerUser(request);
    }

    //registerAgentUser
    @PostMapping("/registerAgentUser")
    public ResponseEntity<?> registerAgentUser(@RequestBody RegisterRequest request) {
        return authService.registerAgentUser(request);
    }

    //deactivate Agent User by email
    @PutMapping("/deactivateAgentUser")
    public ResponseEntity<?> deactivateAgentUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateAgentUser(request);
    }

    //reActivate Agent User by email
    @PutMapping("/reActivateAgentUser")
    public ResponseEntity<?> reActivateAgentUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateAgentUser(request);
    }

    //deactivate Seller User by email
    @PutMapping("/deactivateSellerUser")
    public ResponseEntity<?> deactivateSellerUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateSellerUser(request);
    }

    //reActivate Seller User by email
    @PutMapping("/reActivateSellerUser")
    public ResponseEntity<?> reActivateSellerUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateSellerUser(request);
    }

    //deactivate Customer by email
    @PutMapping("/deactivateCustomer")
    public ResponseEntity<?> deactivateCustomer(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateCustomer(request);
    }

    //reActivate Customer by email
    @PutMapping("/reActivateCustomer")
    public ResponseEntity<?> reActivateCustomer(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateCustomer(request);
    }

    //get the list of users on the basis of the roles
    @GetMapping("/users/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role){
        try {
            List<User> users = adminService.getUsersByRole(role);
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found for the role :" + role);
            }
            return ResponseEntity.ok(users);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //get the details of the user
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUsersById(@PathVariable Long userId){
        try {
            User user = adminService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Admin profile
    @GetMapping("/myprofile")
    public ResponseEntity<UserProfileResponseDTO> viewMyProfile(HttpServletRequest request){
        ResponseEntity<UserProfileResponseDTO> userProfileResponseDTO=userService.getMyProfileDetails(request);
        return userProfileResponseDTO;
    }

    //add the categories
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreateDTO dto) {
        Category created = categoryService.createCategory(dto);
        return ResponseEntity.status(201).body(created);
    }
    //list of categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
        List<CategoryResponseDTO> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    //update the categories
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryUpdateResponseDTO> updateCategory(@PathVariable Long id,@RequestBody CategoryUpdateDTO dto){
        CategoryUpdateResponseDTO updateResponseDTO=categoryService.updateCategory(id,dto);
        return ResponseEntity.ok(updateResponseDTO);
    }


    //Add the brands
    @PostMapping("/brands")
    public ResponseEntity<?> createBrand(@RequestBody BrandCreateDTO dto) {
        Brand created = brandService.createBrand(dto);
        return ResponseEntity.status(201).body(created);
    }

    //list of brand
    @GetMapping("/brands")
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands(){
        List<BrandResponseDTO> brands=brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }
    //Get particular brand
    @GetMapping("/brand/{id}")
    public ResponseEntity<BrandResponseDTO> getBrandById(@PathVariable Long id){
        BrandResponseDTO brand=brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }

    //update the brand
    @PutMapping("/brand/{id}")
    public ResponseEntity<BrandResponseDTO> updateBrand(@PathVariable Long id,@RequestBody BrandRequestDTO dto){
        BrandResponseDTO updateResponseDTO=brandService.updateBrand(id,dto);
        return ResponseEntity.ok(updateResponseDTO);
    }



    //create product
    //@PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    @PostMapping("/products/create")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateDTO dto) {
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.status(201).body(created);
    }

    //get the seller added product and is active
    @GetMapping("/sellerProductActive")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProductsBySeller(
            @RequestParam("role") Role sellerRole) {

        List<ProductResponseDTO> products = productService.getActiveProductsBySeller(sellerRole);
        return ResponseEntity.ok(products);
    }
    //get the seller added product and is InActive
    @GetMapping("/sellerProductInActive")
    public ResponseEntity<List<ProductResponseDTO>> getInActiveProductsBySeller(
            @RequestParam("role") Role sellerRole) {

        List<ProductResponseDTO> products = productService.getInActiveProductsBySeller(sellerRole);
        return ResponseEntity.ok(products);
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
    //verify the product added by agent/seller
    // Example: PUT /products/activate/12
    @PutMapping("/activate/{productId}")
    public ResponseEntity<ProductResponseDTO> activateProductByAdmin(
            @PathVariable Long productId,
            @RequestBody(required = false) String verifiedReason) {

        ProductResponseDTO updated = productService.activateProductByAdmin(productId, verifiedReason);
        return ResponseEntity.ok(updated);
    }
    //get the list of product
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    // Example: http://localhost:8083/admin/product/search?categoryId=1&name=Smart Phone&page=0&size=20&sort=price,asc
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
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductResponseDTO> results = productService.searchProducts(categoryId, name, brandName, (org.springframework.data.domain.Pageable) pageable);
        return ResponseEntity.ok(results);
    }

    // get product by id
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        ProductResponseDTO dto = productService.searchProducts(null, null, null, PageRequest.of(0,100))
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





}
