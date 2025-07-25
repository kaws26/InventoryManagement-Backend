package com.kaws26.inventoryManagementSystem.repositories;

import com.kaws26.inventoryManagementSystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
