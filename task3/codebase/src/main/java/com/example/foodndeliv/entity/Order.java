package com.example.foodndeliv.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.foodndeliv.entitylistener.OrderPostLoadListener;
import com.example.foodndeliv.types.OrderState;

@Entity
@Table(name = "orders")
@EntityListeners({AuditingEntityListener.class, OrderPostLoadListener.class})
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Missing order details") @Size(max = 500, message = "Order details max length exceeded")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Invalid Order details")
    @Column(name = "order_details", length = 500)
    private String orderDetails;

    @NotNull(message = "Missing restaurant")
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull(message = "Missing customer")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Cannot leave orderline empty")
    @Valid
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderLine> orderLines;

    @NotNull(message = "Missing order state")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private OrderState state;

    // For total calculation bii
    @PositiveOrZero(message = "Total price >=0")
    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonIgnore
    private long createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    @JsonIgnore
    private long modifiedDate;

}
