package com.example.foodndeliv.service;

import com.example.foodndeliv.repository.*;
import com.example.foodndeliv.types.CustomerState;
import com.example.foodndeliv.types.OrderState;
import com.example.foodndeliv.types.RestaurantState;

import jakarta.servlet.http.HttpServletRequest;

import com.example.foodndeliv.dto.*;
import com.example.foodndeliv.entity.*;
import com.example.foodndeliv.exceptions.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RestaurantService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HttpServletRequest request;


    @Transactional
    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO) {

        Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new DomainInvariantException("Restaurant not found"));

        modelMapper.map(restaurantDTO, restaurant);

        //Domain invariant - A Closed Restaurant is not associated with pending orders
        if(restaurant.getState() == RestaurantState.CLOSED) {

            List<Order> orderlist = orderRepository.findOrdersByRestID(id);

            if (orderlist != null) {

                //Cannot close a restaurant in case of any non-cancellable orders
                for(var order : orderlist) {
                    if(order.getState() == OrderState.OPEN || order.getState() == OrderState.CONFIRMED ) {
                        throw new DomainInvariantException("Cannot close restaurant: Open and/or Pending orders");
                    }
                }
            }
        }

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDTO.class);
    }
}