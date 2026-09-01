package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateStatusRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.exception.DuplicateTransactionException;
import com.example.transactionstarter.exception.InvalidStatusTransitionException;
import com.example.transactionstarter.exception.TransactionNotFoundException;
import com.example.transactionstarter.model.TransactionStatus;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {

        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new DuplicateTransactionException(
                    "Transaction ID already exists: " + request.getTransactionId()
            );
        }

        if (request.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalArgumentException(
                    "New transactions must start with PENDING status"
            );
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setStatus(request.getStatus());

        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found: " + transactionId
                ));
    }

    public Transaction updateStatus(
            String transactionId,
            UpdateStatusRequest request) {

        Transaction transaction = getTransaction(transactionId);

        TransactionStatus currentStatus = transaction.getStatus();
        TransactionStatus newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Cannot change transaction status from "
                            + currentStatus + " to " + newStatus
            );
        }

        transaction.setStatus(newStatus);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getCustomerTransactions(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    private boolean isValidStatusTransition(
            TransactionStatus currentStatus,
            TransactionStatus newStatus) {

        if (currentStatus == newStatus) {
            return false;
        }

        if (currentStatus != TransactionStatus.PENDING) {
            return false;
        }

        return newStatus == TransactionStatus.COMPLETED
                || newStatus == TransactionStatus.FAILED
                || newStatus == TransactionStatus.CANCELLED;
    }
}