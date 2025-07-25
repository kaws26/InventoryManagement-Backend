package com.kaws26.inventoryManagementSystem.services.impl;


import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.SupplierDto;
import com.kaws26.inventoryManagementSystem.exceptions.NotFoundException;
import com.kaws26.inventoryManagementSystem.models.Supplier;
import com.kaws26.inventoryManagementSystem.repositories.SupplierRepository;
import com.kaws26.inventoryManagementSystem.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDto supplierDto) {
        Supplier supplierToSave=modelMapper.map(supplierDto,Supplier.class);
        supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier Created Successfully")
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier=supplierRepository.findById(id)
                .orElseThrow(()->
                        new NotFoundException("Supplier not found."));

        if(supplierDto.getName()!=null ) existingSupplier.setName(supplierDto.getName());
        if(supplierDto.getContactInfo()!=null) existingSupplier.setContactInfo(supplierDto.getContactInfo());
        if(supplierDto.getAddress()!=null) existingSupplier.setAddress(supplierDto.getAddress());

        supplierRepository.save(existingSupplier);
        return Response.builder()
                .status(200)
                .message("Supplier Info updated successfully!")
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier=supplierRepository.findById(id)
                .orElseThrow(()->
                        new NotFoundException("Supplier Not Found "));

        SupplierDto supplierDto=modelMapper.map(supplier,SupplierDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .supplier(supplierDto)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {
        Supplier supplier=supplierRepository.findById(id)
                .orElseThrow(()->
                        new NotFoundException("Supplier not found")
                );
        supplierRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Supplier Deleted Successfully")
                .build();

    }

    @Override
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<SupplierDto> supplierDTOList = modelMapper.map(suppliers, new TypeToken<List<SupplierDto>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .suppliers(supplierDTOList)
                .build();
    }
}
