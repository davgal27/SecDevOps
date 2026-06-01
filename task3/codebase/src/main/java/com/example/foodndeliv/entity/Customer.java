package com.example.foodndeliv.entity;

import main.java.com.example.foodndeliv.entitylistener.CustomerPostLoadListener;
import jakarta.persistence.*;
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

import com.example.foodndeliv.types.*;

@Entity
@Table(name = "customers")
@EntityListeners({AuditingEntityListener.class, CustomerPostLoadListener.class})
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank (message="Name cannot be blank") @Size(max = 30, message = "Name max length exceeded")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Invalid Name")
    @Column(name = "name", nullable = false, unique = true, length=30)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email")
    @Size(max = 30, message = "Email max length exceeded")
    @Column(name = "email", nullable = false, length=30)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    @NotNull(message = "state cannot be blank")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private CustomerState state;
    
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

