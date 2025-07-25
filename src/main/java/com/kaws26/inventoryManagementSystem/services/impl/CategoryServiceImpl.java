package com.kaws26.inventoryManagementSystem.services.impl;

import com.kaws26.inventoryManagementSystem.dtos.CategoryDto;
import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.exceptions.NotFoundException;
import com.kaws26.inventoryManagementSystem.models.Category;
import com.kaws26.inventoryManagementSystem.repositories.CategoryRepository;
import com.kaws26.inventoryManagementSystem.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDto categoryDto) {

        Category categoryToSave=modelMapper.map(categoryDto,Category.class);
        categoryRepository.save(categoryToSave);

        return Response.builder()
                .status(200)
                .message("Category Created Successfully!")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        categories.forEach(category -> category.setProducts(null));

        List<CategoryDto> categoryDTOList = modelMapper.map(categories, new TypeToken<List<CategoryDto>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .categories(categoryDTOList)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category not found"));

        CategoryDto categoryDto=modelMapper.map(category,CategoryDto.class);

        return Response.builder()
                .status(200)
                .message("Success")
                .category(categoryDto)
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory=categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category Not found."));

        existingCategory.setName(categoryDto.getName());

        categoryRepository.save(existingCategory);
        return Response.builder()
                .status(200)
                .message("Category updated successfully")
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()->
                new NotFoundException("Category not found"));
        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Category deleted Successfully")
                .build();
    }
}
