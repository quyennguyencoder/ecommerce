package com.nguyenquyen.ecommerce.models;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "product_images")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {
    public static final int MAXIMUM_IMAGE_PER_PRODUCT = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
