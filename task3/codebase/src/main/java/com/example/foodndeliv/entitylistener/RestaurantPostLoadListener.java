package main.java.com.example.foodndeliv.entitylistener;

import jakarta.persistence.PostLoad;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.foodndeliv.dto.RestaurantDTO;
import com.example.foodndeliv.entity.Restaurant;

@Component
public class RestaurantPostLoadListener {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    @PostLoad
    public void handlePostLoad(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = modelMapper.map(restaurant, RestaurantDTO.class);
        validateRestaurantDTO(restaurantDTO);
    }

    private void validateRestaurantDTO(RestaurantDTO restaurantDTO) {
        Set<ConstraintViolation<RestaurantDTO>> violations = validator.validate(restaurantDTO);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Restaurant DTO validation failed", violations);
        }
    }
}