package com.kaws26.inventoryManagementSystem.services;

import com.kaws26.inventoryManagementSystem.dtos.CategoryDto;
import com.kaws26.inventoryManagementSystem.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDto);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id,CategoryDto categoryDto);

    Response deleteCategory(Long id);

}
