package com.nguyenquyen.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail", length = 255)
    private String thumbnail;

    @Min(value = 0, message = "Số lượng đã bán phải lớn hơn hoặc bằng 0")
    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    @DecimalMin(value = "0.0", message = "Đánh giá phải từ 0.0")
    @DecimalMax(value = "5.0", message = "Đánh giá không được vượt quá 5.0")
    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;

    @Min(value = 0, message = "Số lượt đánh giá phải lớn hơn hoặc bằng 0")
    @Column(name = "rating_count")
    private Integer ratingCount;

    @DecimalMin(value = "0.00", message = "Giá tối thiểu phải lớn hơn hoặc bằng 0")
    @Column(name = "min_price", precision = 12, scale = 2)
    private BigDecimal minPrice;

    @DecimalMin(value = "0.00", message = "Giá tối đa phải lớn hơn hoặc bằng 0")
    @Column(name = "max_price", precision = 12, scale = 2)
    private BigDecimal maxPrice;

    @Column(name = "is_hot")
    private Boolean isHot;

    @Min(value = 0, message = "Tổng số lượng trong kho phải lớn hơn hoặc bằng 0")
    @Column(name = "total_stock")
    private Integer totalStock;

    @Size(max = 50, message = "Trạng thái không được vượt quá 50 ký tự")
    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductVariant> variants = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private Set<Feedback> feedbacks = new HashSet<>();
}