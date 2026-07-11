package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.BalanceSheetReport;
import com.sporekart.ai.domain.model.FinanceAccount;
import com.sporekart.ai.domain.model.JournalLine;
import com.sporekart.ai.domain.model.JournalEntry;
import com.sporekart.ai.domain.model.LedgerEntry;
import com.sporekart.ai.domain.model.ProfitLossReport;
import com.sporekart.ai.domain.model.TrialBalanceReport;
import com.sporekart.ai.common.exception.AccountingException;
import com.sporekart.ai.infrastructure.persistence.entity.FinanceAccountEntity;
import com.sporekart.ai.infrastructure.persistence.entity.JournalEntryEntity;
import com.sporekart.ai.infrastructure.persistence.entity.LedgerEntryEntity;
import com.sporekart.ai.infrastructure.persistence.repository.FinanceAccountRepository;
import com.sporekart.ai.infrastructure.persistence.repository.JournalEntryRepository;
import com.sporekart.ai.infrastructure.persistence.repository.LedgerEntryRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class FinanceService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final FinanceAccountRepository financeAccountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    // keep lightweight in-memory domain objects for in-progress operations
    private final Map<UUID, JournalEntry> journals = new HashMap<>();

    public FinanceService(KafkaTemplate<String, String> kafkaTemplate,
            FinanceAccountRepository financeAccountRepository,
            JournalEntryRepository journalEntryRepository,
            LedgerEntryRepository ledgerEntryRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.financeAccountRepository = financeAccountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public FinanceAccount createAccount(String code, String name, String type, String currency,
            BigDecimal initialBalance)
            throws AccountingException {
        if (financeAccountRepository.existsByAccountCode(code)) {
            throw new AccountingException("Account code already exists: " + code);
        }
        FinanceAccountEntity entity = new FinanceAccountEntity(UUID.randomUUID(), code, name, type, currency,
                initialBalance);
        financeAccountRepository.save(entity);
        return new FinanceAccount(entity.getId(), entity.getAccountCode(), entity.getAccountName(),
                entity.getAccountType(), entity.getCurrency(), entity.getBalance());
    }

    public JournalEntry createJournal(LocalDate date, String description, String refType, UUID refId)
            throws AccountingException {
        JournalEntry entry = new JournalEntry(UUID.randomUUID(), "JE-" + System.currentTimeMillis(),
                date, description, refType, refId);
        // persist a draft journal entry entity
        JournalEntryEntity ent = new JournalEntryEntity(entry.getId(), entry.getJournalNumber(), entry.getEntryDate(),
                entry.getDescription(), refType, refId, BigDecimal.ZERO, BigDecimal.ZERO);
        journalEntryRepository.save(ent);
        journals.put(entry.getId(), entry);
        return entry;
    }

    public void postJournal(UUID journalId, UUID userId) throws AccountingException {
        JournalEntry journal = journals.get(journalId);
        if (journal == null)
            throw new AccountingException("Journal not found");
        if (!journal.isBalanced())
            throw new AccountingException("Journal entries not balanced");

        // update persisted journal entity
        Optional<JournalEntryEntity> maybe = journalEntryRepository.findById(journalId);
        if (maybe.isPresent()) {
            JournalEntryEntity ent = maybe.get();
            ent.setStatus("POSTED");
            ent.setTotalDebit(journal.getTotalDebit());
            ent.setTotalCredit(journal.getTotalCredit());
            journalEntryRepository.save(ent);
        }

        // persist ledger entries and update account balances
        for (JournalLine line : journal.getLines()) {
            LedgerEntryEntity ledgerEnt = new LedgerEntryEntity(UUID.randomUUID(), line.getAccountId(), journalId,
                    journal.getEntryDate(), line.getDebitAmount(), line.getCreditAmount(), line.getDescription());
            ledgerEntryRepository.save(ledgerEnt);

            Optional<FinanceAccountEntity> acctMaybe = financeAccountRepository.findById(line.getAccountId());
            if (acctMaybe.isEmpty()) {
                throw new AccountingException("Account not found: " + line.getAccountId());
            }
            FinanceAccountEntity acct = acctMaybe.get();
            BigDecimal debit = line.getDebitAmount() != null ? line.getDebitAmount() : BigDecimal.ZERO;
            BigDecimal credit = line.getCreditAmount() != null ? line.getCreditAmount() : BigDecimal.ZERO;
            BigDecimal delta = debit.subtract(credit);
            acct.setBalance((acct.getBalance() != null ? acct.getBalance() : BigDecimal.ZERO).add(delta));
            acct.setUpdatedAt(OffsetDateTime.now());
            financeAccountRepository.save(acct);
        }

        // mark domain as posted
        journal.post(userId);
        publishEvent("JournalPosted", journalId.toString());
    }

    public JournalEntry addJournalLine(UUID journalId, UUID accountId, BigDecimal debit, BigDecimal credit,
            String description) throws AccountingException {
        JournalEntry journal = journals.get(journalId);
        if (journal == null)
            throw new AccountingException("Journal not found");
        if ((debit == null || debit.compareTo(BigDecimal.ZERO) == 0)
                && (credit == null || credit.compareTo(BigDecimal.ZERO) == 0))
            throw new AccountingException("Either debit or credit must be provided");
        if (!financeAccountRepository.existsById(accountId))
            throw new AccountingException("Account not found: " + accountId);

        JournalLine line = new JournalLine(accountId, debit, credit, description);
        journal.addLine(line);

        // update persisted journal totals
        Optional<JournalEntryEntity> maybe = journalEntryRepository.findById(journalId);
        if (maybe.isPresent()) {
            JournalEntryEntity ent = maybe.get();
            ent.setTotalDebit(journal.getTotalDebit());
            ent.setTotalCredit(journal.getTotalCredit());
            journalEntryRepository.save(ent);
        }

        return journal;
    }

    public List<FinanceAccount> getChartOfAccounts() {
        List<FinanceAccount> result = new ArrayList<>();
        for (FinanceAccountEntity ent : financeAccountRepository.findAll()) {
            result.add(new FinanceAccount(ent.getId(), ent.getAccountCode(), ent.getAccountName(), ent.getAccountType(),
                    ent.getCurrency(), ent.getBalance()));
        }
        return result;
    }

    public TrialBalanceReport getTrialBalance(LocalDate asOf) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (LedgerEntryEntity entry : ledgerEntryRepository.findAll()) {
            if (entry.getEntryDate().isBefore(asOf) || entry.getEntryDate().isEqual(asOf)) {
                totalDebit = totalDebit.add(entry.getDebitAmount() != null ? entry.getDebitAmount() : BigDecimal.ZERO);
                totalCredit = totalCredit
                        .add(entry.getCreditAmount() != null ? entry.getCreditAmount() : BigDecimal.ZERO);
            }
        }

        return new TrialBalanceReport(totalDebit, totalCredit, totalDebit.compareTo(totalCredit) == 0, asOf);
    }

    public ProfitLossReport getProfitAndLoss(LocalDate startDate, LocalDate endDate) {
        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;

        return new ProfitLossReport(revenue, expenses, revenue.subtract(expenses), startDate + " to " + endDate);
    }

    public BalanceSheetReport getBalanceSheet(LocalDate asOf) {
        return new BalanceSheetReport(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, asOf);
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            kafkaTemplate.send("finance-events", eventType, eventData);
        } catch (Exception e) {
            System.out.println("Failed to publish finance event: " + e.getMessage());
        }
    }
}
