package com.kaws26.inventoryManagementSystem.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kaws26.inventoryManagementSystem.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    //Genric
    private int status;
    private String message;

    //login
    private String token;
    private UserRole role;
    private String expirationTime;

    //for pagination
    private Integer totalPages;
    private Long totalElements;

    //data output optionals
    private UserDto user;
    private List<UserDto> users;

    private CategoryDto category;
    private List<CategoryDto> categories;

    private ProductDto product;
    private List<ProductDto> products;

    private SupplierDto supplier;
    private List<SupplierDto> suppliers;

    private TransactionDto transaction;
    private List<TransactionDto> transactions;

    //timestamp
    private final LocalDateTime timestamp=LocalDateTime.now();

}
