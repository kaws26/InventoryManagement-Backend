package com.kaws26.inventoryManagementSystem.controllers;


import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.TransactionRequest;
import com.kaws26.inventoryManagementSystem.enums.TransactionStatus;
import com.kaws26.inventoryManagementSystem.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Response> purchaseInventory(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.purchase(transactionRequest));

    }

    @PostMapping("/sell")
    public ResponseEntity<Response> makeSale(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @PostMapping("/return")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String filter
    ){
        System.out.println("SEARCH VALUE IS "+filter);
        return ResponseEntity.ok(transactionService.getAllTransactions(page,size,filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTranscationById(id));
    }

    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getTransactionByMonthYear(
            @RequestParam int month,
            @RequestParam int year
    ){
        return ResponseEntity.ok(transactionService.getAllTransactionsByMonthAndYear(month,year));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Response> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestBody TransactionStatus status
            ){
        return ResponseEntity.ok(transactionService.updateTranscationStatus(transactionId,status));
    }
}
