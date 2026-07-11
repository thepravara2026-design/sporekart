package com.sporekart.ai.application.service;

import com.sporekart.ai.infrastructure.persistence.entity.FinanceAccountEntity;
import com.sporekart.ai.infrastructure.persistence.entity.JournalEntryEntity;
import com.sporekart.ai.infrastructure.persistence.repository.FinanceAccountRepository;
import com.sporekart.ai.infrastructure.persistence.repository.JournalEntryRepository;
import com.sporekart.ai.infrastructure.persistence.repository.LedgerEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceServiceTest {

        @Mock
        KafkaTemplate<String, String> kafkaTemplate;

        @Mock
        FinanceAccountRepository financeAccountRepository;

        @Mock
        JournalEntryRepository journalEntryRepository;

        @Mock
        LedgerEntryRepository ledgerEntryRepository;

        @InjectMocks
        FinanceService financeService;

        @Test
        void createAccountAndJournalLifecycle() throws Exception {
                UUID acctA = UUID.randomUUID();
                UUID acctB = UUID.randomUUID();

                when(financeAccountRepository.existsByAccountCode("A_ACC")).thenReturn(false);
                when(financeAccountRepository.save(any(FinanceAccountEntity.class))).thenAnswer(i -> i.getArgument(0));

                var created = financeService.createAccount("A_ACC", "Account A", "EXPENSE", "INR",
                                new BigDecimal("100.00"));
                assertEquals("A_ACC", created.getAccountCode());

                // prepare accounts for ledger posting
                FinanceAccountEntity entA = new FinanceAccountEntity(acctA, "A_ACC", "Account A", "EXPENSE", "INR",
                                new BigDecimal("100.00"));
                FinanceAccountEntity entB = new FinanceAccountEntity(acctB, "B_ACC", "Account B", "ASSET", "INR",
                                new BigDecimal("500.00"));

                // create a journal
                var journal = financeService.createJournal(LocalDate.now(), "Test JE", "INV", UUID.randomUUID());
                JournalEntryEntity jeEnt = new JournalEntryEntity(journal.getId(), journal.getJournalNumber(),
                                journal.getEntryDate(), journal.getDescription(), "INV", journal.getId(),
                                BigDecimal.ZERO,
                                BigDecimal.ZERO);

                when(journalEntryRepository.findById(journal.getId())).thenReturn(Optional.of(jeEnt));
                when(financeAccountRepository.existsById(acctA)).thenReturn(true);
                when(financeAccountRepository.existsById(acctB)).thenReturn(true);
                when(financeAccountRepository.findById(acctA)).thenReturn(Optional.of(entA));
                when(financeAccountRepository.findById(acctB)).thenReturn(Optional.of(entB));
                when(ledgerEntryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
                when(financeAccountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

                // add two lines - debit A 50, credit B 50
                financeService.addJournalLine(journal.getId(), acctA, new BigDecimal("50.00"), null, "expense line");
                financeService.addJournalLine(journal.getId(), acctB, null, new BigDecimal("50.00"), "payment line");

                // post journal -> should persist ledger entries and update balances
                financeService.postJournal(journal.getId(), UUID.randomUUID());

                verify(ledgerEntryRepository, times(2)).save(any());
                verify(financeAccountRepository, times(3)).save(any());
        }
}
