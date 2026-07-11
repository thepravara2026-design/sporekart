package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.GSTTransaction;
import com.sporekart.ai.common.exception.GSTException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class GSTService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<UUID, GSTTransaction> transactions = new HashMap<>();
    private final Map<String, BigDecimal> gstRates = Map.ofEntries(
            Map.entry("RATE_0", BigDecimal.ZERO),
            Map.entry("RATE_5", new BigDecimal("5")),
            Map.entry("RATE_12", new BigDecimal("12")),
            Map.entry("RATE_18", new BigDecimal("18")),
            Map.entry("RATE_28", new BigDecimal("28")));

    public GSTService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public GSTTransaction createTransaction(String transType, LocalDate date, BigDecimal amount,
            String supplierGSTIN, String buyerGSTIN) throws GSTException {
        if (!isValidGSTIN(supplierGSTIN) && supplierGSTIN != null) {
            throw new GSTException("Invalid supplier GSTIN");
        }
        if (!isValidGSTIN(buyerGSTIN) && buyerGSTIN != null) {
            throw new GSTException("Invalid buyer GSTIN");
        }

        GSTTransaction tx = new GSTTransaction(UUID.randomUUID(), "GST-" + System.currentTimeMillis(),
                transType, date, amount);
        tx.calculateTax();
        transactions.put(tx.getId(), tx);

        publishEvent("GSTCalculated", tx.getId().toString());
        return tx;
    }

    public void processTransaction(UUID txId) throws GSTException {
        GSTTransaction tx = transactions.get(txId);
        if (tx == null)
            throw new GSTException("Transaction not found");
        tx.process();
    }

    public void fileReturn(UUID txId) throws GSTException {
        GSTTransaction tx = transactions.get(txId);
        if (tx == null)
            throw new GSTException("Transaction not found");
        tx.file();
    }

    public Map<String, Object> getGSTReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        BigDecimal totalIGST = BigDecimal.ZERO;
        BigDecimal totalSGST = BigDecimal.ZERO;
        BigDecimal totalCGST = BigDecimal.ZERO;
        Integer processedCount = 0;

        for (GSTTransaction tx : transactions.values()) {
            if (tx.getTransactionDate().isAfter(startDate) && tx.getTransactionDate().isBefore(endDate)) {
                if ("PROCESSED".equals(tx.getStatus())) {
                    totalIGST = totalIGST.add(tx.getIgstAmount());
                    totalSGST = totalSGST.add(tx.getSgstAmount());
                    totalCGST = totalCGST.add(tx.getCgstAmount());
                    processedCount++;
                }
            }
        }

        report.put("totalIGST", totalIGST);
        report.put("totalSGST", totalSGST);
        report.put("totalCGST", totalCGST);
        report.put("processedTransactions", processedCount);
        report.put("period", startDate + " to " + endDate);

        return report;
    }

    public boolean isValidGSTIN(String gstin) {
        return gstin == null || (gstin.length() == 15 && gstin.matches("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9]{1}"));
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            kafkaTemplate.send("gst-events", eventType, eventData);
        } catch (Exception e) {
            System.out.println("Failed to publish GST event: " + e.getMessage());
        }
    }
}
