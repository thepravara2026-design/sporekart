package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.BulkOrder;
import com.sporekart.ai.domain.model.CorporateAccount;
import com.sporekart.ai.domain.model.CreditAccount;
import com.sporekart.ai.domain.model.Dealer;
import com.sporekart.ai.domain.model.Distributor;
import com.sporekart.ai.domain.model.PurchaseAgreement;
import com.sporekart.ai.domain.model.Quotation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class B2bService {
    private final List<Dealer> dealers = new ArrayList<>();
    private final List<Distributor> distributors = new ArrayList<>();
    private final List<CorporateAccount> corporateAccounts = new ArrayList<>();
    private final List<Quotation> quotations = new ArrayList<>();
    private final List<BulkOrder> bulkOrders = new ArrayList<>();
    private final List<CreditAccount> creditAccounts = new ArrayList<>();
    private final List<PurchaseAgreement> purchaseAgreements = new ArrayList<>();

    public Dealer registerDealer(String name, String gstNumber) {
        Dealer dealer = new Dealer(UUID.randomUUID(), name, gstNumber);
        dealers.add(dealer);
        return dealer;
    }

    public List<Dealer> listDealers() {
        return new ArrayList<>(dealers);
    }

    public Distributor registerDistributor(String name, String region) {
        Distributor distributor = new Distributor(UUID.randomUUID(), name, region);
        distributors.add(distributor);
        return distributor;
    }

    public List<Distributor> listDistributors() {
        return new ArrayList<>(distributors);
    }

    public CorporateAccount createCorporateAccount(String name, String gstNumber, String panNumber) {
        CorporateAccount account = new CorporateAccount(UUID.randomUUID(), name, gstNumber, panNumber);
        corporateAccounts.add(account);
        return account;
    }

    public List<CorporateAccount> listCorporateAccounts() {
        return new ArrayList<>(corporateAccounts);
    }

    public Quotation createQuotation(String reference, UUID dealerId) {
        Quotation quotation = new Quotation(UUID.randomUUID(), reference, dealerId, "DRAFT");
        quotations.add(quotation);
        return quotation;
    }

    public List<Quotation> listQuotations() {
        return new ArrayList<>(quotations);
    }

    public BulkOrder createBulkOrder(String reference, int minimumQuantity) {
        BulkOrder order = new BulkOrder(UUID.randomUUID(), reference, minimumQuantity);
        bulkOrders.add(order);
        return order;
    }

    public List<BulkOrder> listBulkOrders() {
        return new ArrayList<>(bulkOrders);
    }

    public CreditAccount createCreditAccount(String customerName, BigDecimal creditLimit) {
        CreditAccount account = new CreditAccount(UUID.randomUUID(), customerName, creditLimit);
        creditAccounts.add(account);
        return account;
    }

    public List<CreditAccount> listCreditAccounts() {
        return new ArrayList<>(creditAccounts);
    }

    public PurchaseAgreement createPurchaseAgreement(String reference) {
        PurchaseAgreement agreement = new PurchaseAgreement(UUID.randomUUID(), reference, "ACTIVE");
        purchaseAgreements.add(agreement);
        return agreement;
    }

    public List<PurchaseAgreement> listPurchaseAgreements() {
        return new ArrayList<>(purchaseAgreements);
    }
}

