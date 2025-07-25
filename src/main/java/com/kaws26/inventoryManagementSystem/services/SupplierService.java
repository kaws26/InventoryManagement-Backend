package com.kaws26.inventoryManagementSystem.services;

import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.SupplierDto;

public interface SupplierService {

    Response addSupplier(SupplierDto supplierDto);

    Response updateSupplier(Long id,SupplierDto supplierDto);

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);

    Response getAllSuppliers();
}
