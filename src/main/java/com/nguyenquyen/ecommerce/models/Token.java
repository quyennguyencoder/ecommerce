package com.nguyenquyen.ecommerce.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "tokens")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String tokenType;
    private LocalDateTime expirationDate;
    private Boolean revoked;
    private Boolean expired;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
