package com.kaws26.inventoryManagementSystem.services.impl;

import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.TransactionDto;
import com.kaws26.inventoryManagementSystem.dtos.TransactionRequest;
import com.kaws26.inventoryManagementSystem.enums.TransactionStatus;
import com.kaws26.inventoryManagementSystem.enums.TransactionType;
import com.kaws26.inventoryManagementSystem.exceptions.NameValueRequiredException;
import com.kaws26.inventoryManagementSystem.exceptions.NotFoundException;
import com.kaws26.inventoryManagementSystem.models.Product;
import com.kaws26.inventoryManagementSystem.models.Supplier;
import com.kaws26.inventoryManagementSystem.models.Transaction;
import com.kaws26.inventoryManagementSystem.models.User;
import com.kaws26.inventoryManagementSystem.repositories.ProductRepository;
import com.kaws26.inventoryManagementSystem.repositories.SupplierRepository;
import com.kaws26.inventoryManagementSystem.repositories.TransactionRepository;
import com.kaws26.inventoryManagementSystem.services.TransactionService;
import com.kaws26.inventoryManagementSystem.services.UserService;
import com.kaws26.inventoryManagementSystem.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response purchase(TransactionRequest transactionRequest) {
        Long productId= transactionRequest.getProductId();
        Long supplierId=transactionRequest.getSupplierId();
        Integer quantity =transactionRequest.getQuantity();

        if(supplierId==null) throw new NameValueRequiredException("Supplier Id is required.");

        Product product=productRepository.findById(productId).orElseThrow(()->new NotFoundException("Product not found"));
        Supplier supplier=supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found."));
        User user=userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity()+quantity);
        productRepository.save(product);

        //create transaction
        Transaction transaction=Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase made successfully.")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId=transactionRequest.getProductId();
        Integer quantity=transactionRequest.getQuantity();

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new NotFoundException("Product not found."));

        User user=userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity()-quantity);
        productRepository.save(product);

        //create a transaction
        Transaction transaction=Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Product sale successfully.")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        Long productId=transactionRequest.getProductId();
        Long supplierId=transactionRequest.getSupplierId();
        Integer quantity=transactionRequest.getQuantity();

        if(supplierId==null) throw new NameValueRequiredException("Supplier Id is required.");

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new NotFoundException("Product not found."));

        Supplier supplier=supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found."));

        User user=userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity()-quantity);
        productRepository.save(product);

        //create transaction
        Transaction transaction=Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Product returned in progress")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        //user the Transaction specification
        Specification<Transaction> spec = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionDto> transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDto>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();
    }

    @Override
    public Response getTranscationById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        TransactionDto transactionDTO = modelMapper.map(transaction, TransactionDto.class);

        transactionDTO.getUser().setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDTO)
                .build();
    }

    @Override
    public Response getAllTransactionsByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDto> transactionDtos = modelMapper.map(transactions, new TypeToken<List<TransactionDto>>() {
        }.getType());

        transactionDtos.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDtos)
                .build();
    }

    @Override
    public Response updateTranscationStatus(Long transactionId, TransactionStatus status) {
        Transaction existingTransaction=transactionRepository.findById(transactionId)
                .orElseThrow(()->new NotFoundException("Transaction not found."));
        existingTransaction.setStatus(status);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction status updated successfully.")
                .build();
    }
}
