package com.example.foodndeliv.config;

import com.example.foodndeliv.entity.Customer;
import com.example.foodndeliv.entity.Order;
import com.example.foodndeliv.entity.Restaurant;

import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class SpringDataRestCustomization implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
        RepositoryRestConfiguration repositoryRestConfiguration,
        CorsRegistry cors) {

        ExposureConfiguration config = repositoryRestConfiguration.getExposureConfiguration();

        //Disable support for HTTP PUT to create resources for each class 
        config.forDomainType(Order.class).disablePutForCreation();
        config.forDomainType(Restaurant.class).disablePutForCreation();
        config.forDomainType(Customer.class).disablePutForCreation();

        // Disable all non GET methods on item resources
        // From spring documentation: Item resource supports only GET, PUT, PATCH, and DELETE
        config.withItemExposure((metadata, httpMethods) ->
                httpMethods.disable(
                        HttpMethod.PUT,
                        HttpMethod.PATCH,
                        HttpMethod.DELETE
                )
        );

        // Disable all non GET methods on collection resources
        // From spring documentation: Collection resource supportsonly POST and GET
        config.withCollectionExposure((metadata, httpMethods) ->
                httpMethods.disable(
                        HttpMethod.POST
                )
        );

        // Disable all non GET methods on Association resources 
        // From spring documentation: Association resource supports only GET, PUT, POST, DELETE
        config.withAssociationExposure((metadata, httpMethods) ->
                httpMethods.disable(
                        HttpMethod.PUT,
                        HttpMethod.POST,
                        HttpMethod.DELETE
                )
        );

        /*
        Query Method resource and Search resource
        don't need to be disabled because they only
        support GET methods in the first place 
        */

    }
}
