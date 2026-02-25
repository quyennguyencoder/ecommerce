package com.nguyenquyen.ecommerce.models;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 350)
    private String name;
    private Float price;
    private String thumbnail;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
