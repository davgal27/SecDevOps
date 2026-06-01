package main.java.com.example.foodndeliv.entitylistener;

import jakarta.persistence.PostLoad;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.foodndeliv.dto.OrderLineDTO;
import com.example.foodndeliv.entity.OrderLine;

@Component
public class OrderLinePostLoadListener {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    @PostLoad
    public void handlePostLoad(OrderLine orderLine) {
        OrderLineDTO orderLineDTO = modelMapper.map(orderLine, OrderLineDTO.class);
        validateOrderLineDTO(orderLineDTO);
    }

    private void validateOrderLineDTO(OrderLineDTO orderLineDTO) {
        Set<ConstraintViolation<OrderLineDTO>> violations = validator.validate(orderLineDTO);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("OrderLine DTO validation failed", violations);
        }
    }
}