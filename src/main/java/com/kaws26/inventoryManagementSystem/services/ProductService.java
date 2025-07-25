package com.kaws26.inventoryManagementSystem.services;

import com.kaws26.inventoryManagementSystem.dtos.ProductDto;
import com.kaws26.inventoryManagementSystem.dtos.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Response saveProduct(ProductDto productDto, MultipartFile imageFIle);

    Response getAllProducts();

    Response getProductById(Long id);

    Response updateProduct(ProductDto productDto,MultipartFile imageFile);

    Response deleteProduct(Long id);

    Response searchProduct(String input);

}
