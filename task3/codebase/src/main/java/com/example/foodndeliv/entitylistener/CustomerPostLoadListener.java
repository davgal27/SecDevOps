package main.java.com.example.foodndeliv.entitylistener;

import jakarta.persistence.PostLoad;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.foodndeliv.dto.CustomerResponseDTO;
import com.example.foodndeliv.entity.Customer;

@Component
public class CustomerPostLoadListener {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    @PostLoad
    public void handlePostLoad(Customer customer) {
        CustomerResponseDTO customerDTO = modelMapper.map(customer, CustomerResponseDTO.class);
        validateCustomerDTO(customerDTO);
    }

    private void validateCustomerDTO(CustomerResponseDTO customerDTO) {
        Set<ConstraintViolation<CustomerResponseDTO>> violations = validator.validate(customerDTO);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Customer DTO validation failed", violations);
        }
    }
}