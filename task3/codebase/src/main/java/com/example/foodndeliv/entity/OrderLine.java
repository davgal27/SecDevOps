package com.example.foodndeliv.entity;

import main.java.com.example.foodndeliv.entitylistener.OrderLinePostLoadListener;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_lines")
@EntityListeners({AuditingEntityListener.class, OrderLinePostLoadListener.class})
@Data
@NoArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Product cannot be blank") @Size(max = 30, message = "Product max length exceeded")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Invalid Product")
    @Column(name = "product_name", nullable = false, length=30)
    private String productName;

    @NotNull(message = "Quantity missing")
    @Positive(message = "Quantity cannot be 0!")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull(message = "Line price cannot be null")
    @PositiveOrZero(message = "Price larger or equal to zero")
    @Column(name = "price", nullable = false)
    private double price;

    @NotNull(message = "Missing order")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

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
