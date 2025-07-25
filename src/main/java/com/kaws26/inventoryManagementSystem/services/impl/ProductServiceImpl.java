package com.kaws26.inventoryManagementSystem.services.impl;

import com.kaws26.inventoryManagementSystem.dtos.ProductDto;
import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.exceptions.NotFoundException;
import com.kaws26.inventoryManagementSystem.models.Category;
import com.kaws26.inventoryManagementSystem.models.Product;
import com.kaws26.inventoryManagementSystem.repositories.CategoryRepository;
import com.kaws26.inventoryManagementSystem.repositories.ProductRepository;
import com.kaws26.inventoryManagementSystem.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/products";



    @Override
    public Response saveProduct(ProductDto productDto, MultipartFile imageFile) {
        Category category=categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()->new NotFoundException("Category not found"));

        Product productToSave=Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity())
                .description(productDto.getDescription())
                .category(category)
                .build();

        if(imageFile!=null && !imageFile.isEmpty()){
            log.info("Image file exists");
            String imagePath=saveImage(imageFile);

            System.out.println("IMAGE URL IS: "+imagePath);
            productToSave.setImageUrl(imagePath);
        }
        productRepository.save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product created successfully")
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDto> productDTOList = modelMapper.map(productList, new TypeToken<List<ProductDto>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Product product=productRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Product not found"));

        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product,ProductDto.class))
                .build();
    }

    @Override
    public Response updateProduct(ProductDto productDto, MultipartFile imageFile) {
        Product existingProduct=productRepository.findById(productDto.getProductId())
                .orElseThrow(()->new NotFoundException("Product not found"));

        if(imageFile!=null && !imageFile.isEmpty()){
            String imagePath=saveImage(imageFile);
            System.out.println("IMAGE PATH IS: "+imagePath);

            existingProduct.setImageUrl(imagePath);
        }

        if(productDto.getCategoryId()!=null && productDto.getCategoryId()>0){
            Category category=categoryRepository.findById(productDto.getProductId())
                    .orElseThrow(()->new NotFoundException("Category not found."));

            existingProduct.setCategory(category);

        }

        if(productDto.getSku()!=null && !productDto.getSku().isBlank()){
            existingProduct.setSku(productDto.getSku());
        }

        if(productDto.getName()!=null && !productDto.getName().isBlank()){
            existingProduct.setName(productDto.getName());
        }

        if(productDto.getPrice()!=null && productDto.getPrice().compareTo(BigDecimal.ZERO)>=0){
            existingProduct.setPrice(productDto.getPrice());
        }

        if(productDto.getDescription()!=null && !productDto.getDescription().isBlank()){
            existingProduct.setDescription(productDto.getDescription());
        }

        if(productDto.getStockQuantity()!=null && productDto.getStockQuantity()>=0){
            existingProduct.setStockQuantity(productDto.getStockQuantity());
        }

        productRepository.save(existingProduct);

        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Product not found"));
        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public Response searchProduct(String input) {
        List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);

        if (products.isEmpty()) {
            throw new NotFoundException("Product Not Found");
        }

        List<ProductDto> productDTOList = modelMapper.map(products, new TypeToken<List<ProductDto>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }
    private String saveImage(MultipartFile imageFile) {
        // Validate
        if (!imageFile.getContentType().startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Only image files under 1GB are allowed");
        }

        // Ensure directory exists
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
            log.info("Directory created at " + IMAGE_DIRECTORY);
        }

        // Generate file name
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        File destinationFile = new File(directory, uniqueFileName);

        try {
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving image: " + e.getMessage());
        }


        return "/products/" + uniqueFileName;
    }

}
