package com.nguyenquyen.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SKU không được để trống")
    @Size(max = 100, message = "SKU không được vượt quá 100 ký tự")
    @Column(name = "sku", length = 100, unique = true, nullable = false)
    private String sku;

    @DecimalMin(value = "0.00", message = "Giá vốn phải lớn hơn hoặc bằng 0")
    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.00", message = "Giá phải lớn hơn hoặc bằng 0")
    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @Min(value = 0, message = "Số lượng trong kho phải lớn hơn hoặc bằng 0")
    @Column(name = "stock")
    private Integer stock;

    @Column(name = "image", length = 255)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "variant_attribute_values",
            joinColumns = @JoinColumn(name = "variant_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    @Builder.Default
    private List<AttributeValue> attributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "variant")
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "variant")
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();
}