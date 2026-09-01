package com.example.transactionstarter;

import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        String request = """
                {
                    "transactionId": "TXN1001",
                    "customerId": "CUS1001",
                    "amount": 1500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN1001"))
                .andExpect(jsonPath("$.customerId").value("CUS1001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldRejectInvalidTransaction() throws Exception {
        String request = """
                {
                    "transactionId": "",
                    "customerId": "CUS1001",
                    "amount": 1500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionId")
                        .value("Transaction ID is required"));
    }

    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {
        String request = """
                {
                    "transactionId": "TXN1002",
                    "customerId": "CUS1001",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error")
                        .value("Transaction ID already exists: TXN1002"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/DOES_NOT_EXIST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Transaction not found: DOES_NOT_EXIST"));
    }

    @Test
    void shouldUpdateTransactionStatusSuccessfully() throws Exception {
        String createRequest = """
                {
                    "transactionId": "TXN1003",
                    "customerId": "CUS1002",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated());

        String updateRequest = """
                {
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(patch("/api/transactions/TXN1003/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN1003"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldGetTransactionsForCustomer() throws Exception {
        String firstRequest = """
                {
                    "transactionId": "TXN1004",
                    "customerId": "CUS1003",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        String secondRequest = """
                {
                    "transactionId": "TXN1005",
                    "customerId": "CUS1003",
                    "amount": 750.00,
                    "currency": "INR",
                    "transactionType": "TRANSFER",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstRequest))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondRequest))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/customers/CUS1003/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerId").value("CUS1003"))
                .andExpect(jsonPath("$[1].customerId").value("CUS1003"));
    }
}