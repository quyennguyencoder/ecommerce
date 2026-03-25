package com.nguyenquyen.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Provider ID không được để trống")
    @Size(max = 255, message = "Provider ID không được vượt quá 255 ký tự")
    @Column(name = "provider_id", length = 255, nullable = false)
    private String providerId;

    @NotBlank(message = "Provider name không được để trống")
    @Size(max = 50, message = "Provider name không được vượt quá 50 ký tự")
    @Column(name = "provider_name", length = 50, nullable = false)
    private String providerName;

    @Email(message = "Email không hợp lệ")
    @Column(name = "email", length = 255)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}