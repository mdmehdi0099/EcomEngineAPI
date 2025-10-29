package com.Ecom.EcomEngineAPI.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "brands")
@EntityListeners(AuditingEntityListener.class)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;
    @Column(nullable = false,unique = false)
    private String name;
    @Column(length = 1000)
    private String description;
    private String logoURL;
    private String websiteURL;
    private boolean active=true;
    private String deactivationReason;
    private LocalDateTime deactivatedAt;
    private String reactivationReason;
    private LocalDateTime reactivatedAt;
    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "brand",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products;



}
