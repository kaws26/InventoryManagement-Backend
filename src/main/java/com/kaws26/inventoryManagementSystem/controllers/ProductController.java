package com.kaws26.inventoryManagementSystem.controllers;


import com.kaws26.inventoryManagementSystem.dtos.ProductDto;
import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addProduct(
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam("sku") String sku,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description",required = false) String description
            ){
        ProductDto productDto=new ProductDto();
        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setCategoryId(categoryId);
        productDto.setStockQuantity(stockQuantity);
        productDto.setDescription(description);

        return ResponseEntity.ok(productService.saveProduct(productDto,imageFile));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "sku",required = false) String sku,
            @RequestParam(value = "price",required = false) BigDecimal price,
            @RequestParam(value = "stockQuantity",required = false) Integer stockQuantity,
            @RequestParam(value = "categoryId",required = false) Long categoryId,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam("productId") Long productId
    ){
        ProductDto productDto=new ProductDto();
        productDto.setProductId(productId);
        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setCategoryId(categoryId);
        productDto.setStockQuantity(stockQuantity);
        productDto.setDescription(description);

        return ResponseEntity.ok(productService.updateProduct(productDto,imageFile));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> search(@RequestParam String input){
        return ResponseEntity.ok(productService.searchProduct(input));
    }
}
