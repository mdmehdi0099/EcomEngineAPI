package com.Ecom.EcomEngineAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_sku", columnList = "stockKeepingUnit")
})
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false)
    private Long price;
    private String pictureURL;

    @JsonIgnoreProperties({"products"})
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @JsonIgnoreProperties({"products"})
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    private boolean active=true;
    private Role createdBy;
    private LocalDateTime addedOn;
    private boolean verified=false;
    @Enumerated(EnumType.STRING)
    private Role verifiedBy;
    private String verifiedReason;
    private LocalDateTime verifiedOn;
    @Column(unique = true)
    private Long stockKeepingUnit;
    private Long inventoryCount;
    private String deactivationReason;
    private LocalDateTime deactivatedAt;
    private String reactivationReason;
    private LocalDateTime reactivatedAt;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
