package com.kaws26.inventoryManagementSystem.repositories;

import com.kaws26.inventoryManagementSystem.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
