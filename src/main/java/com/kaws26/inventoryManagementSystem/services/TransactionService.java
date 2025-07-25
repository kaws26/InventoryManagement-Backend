package com.kaws26.inventoryManagementSystem.services;

import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.TransactionRequest;
import com.kaws26.inventoryManagementSystem.enums.TransactionStatus;

public interface TransactionService {

    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page,int size,String filter);

    Response getTranscationById(Long id);

    Response getAllTransactionsByMonthAndYear(int month,int year);

    Response updateTranscationStatus(Long transactionId, TransactionStatus status);
}
