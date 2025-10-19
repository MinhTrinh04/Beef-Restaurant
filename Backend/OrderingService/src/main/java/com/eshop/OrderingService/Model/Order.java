package com.eshop.OrderingService.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "description")
    private String description;

    // Address fields
    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_state")
    private String addressState;

    @Column(name = "address_country")
    private String addressCountry;

    @Column(name = "address_zip_code")
    private String addressZipCode;

    // Payment fields
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_expiration")
    private LocalDateTime cardExpiration;

    @Column(name = "card_security_number")
    private String cardSecurityNumber;

    @Column(name = "card_type_id")
    private Integer cardTypeId;

    // Buyer information
    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
