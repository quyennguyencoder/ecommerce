package com.nguyenquyen.ecommerce.models;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "social_accounts")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String provider; // e.g., "facebook", "google"
    private String providerId;
    private String name;
    private String email;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
