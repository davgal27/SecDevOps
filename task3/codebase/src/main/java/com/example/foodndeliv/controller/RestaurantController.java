package com.example.foodndeliv.controller;

import com.example.foodndeliv.service.*;
import com.example.foodndeliv.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

@RestController
@RequestMapping("/api/ctrl/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDTO updateRestaurant(@PathVariable Long id, @RequestBody @Valid RestaurantDTO restaurantDTO) {
        System.out.println("updateRestaurant called");
        return restaurantService.updateRestaurant(id, restaurantDTO);
    }
}