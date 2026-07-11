

CREATE TABLE IF NOT EXISTS finance_accounts (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    account_code VARCHAR(50) NOT NULL UNIQUE,
    account_name VARCHAR(255) NOT NULL,
    account_type VARCHAR(50) NOT NULL CHECK (account_type IN ('ASSET', 'LIABILITY', 'EQUITY', 'INCOME', 'EXPENSE')),
    parent_account_id UUID REFERENCES finance_accounts(id) ON DELETE SET NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    balance DECIMAL(18, 2) DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_finance_accounts_code ON finance_accounts(account_code);
CREATE INDEX idx_finance_accounts_type ON finance_accounts(account_type);
CREATE INDEX idx_finance_accounts_parent ON finance_accounts(parent_account_id);
CREATE INDEX idx_finance_accounts_active ON finance_accounts(is_active);

CREATE TABLE IF NOT EXISTS journal_entries (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    journal_number VARCHAR(50) NOT NULL UNIQUE,
    entry_date DATE NOT NULL,
    description TEXT NOT NULL,
    reference_type VARCHAR(50) NOT NULL,
    reference_id UUID,
    total_debit DECIMAL(18, 2) NOT NULL,
    total_credit DECIMAL(18, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'POSTED', 'REJECTED')),
    posted_by UUID,
    posted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_journal_entries_date ON journal_entries(entry_date);
CREATE INDEX idx_journal_entries_status ON journal_entries(status);
CREATE INDEX idx_journal_entries_ref ON journal_entries(reference_type, reference_id);

CREATE TABLE IF NOT EXISTS ledger_entries (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES finance_accounts(id) ON DELETE RESTRICT,
    journal_id UUID NOT NULL REFERENCES journal_entries(id) ON DELETE RESTRICT,
    entry_date DATE NOT NULL,
    debit_amount DECIMAL(18, 2) DEFAULT 0,
    credit_amount DECIMAL(18, 2) DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_ledger_entries_account ON ledger_entries(account_id);
CREATE INDEX idx_ledger_entries_journal ON ledger_entries(journal_id);
CREATE INDEX idx_ledger_entries_date ON ledger_entries(entry_date);


CREATE TABLE IF NOT EXISTS gst_transactions (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    transaction_number VARCHAR(50) NOT NULL UNIQUE,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('PURCHASE', 'SALE', 'CREDIT_NOTE', 'DEBIT_NOTE')),
    transaction_date DATE NOT NULL,
    supplier_gstin VARCHAR(15),
    buyer_gstin VARCHAR(15),
    total_amount DECIMAL(18, 2) NOT NULL,
    sgst_rate DECIMAL(5, 2) DEFAULT 0,
    cgst_rate DECIMAL(5, 2) DEFAULT 0,
    igst_rate DECIMAL(5, 2) DEFAULT 0,
    sgst_amount DECIMAL(18, 2) DEFAULT 0,
    cgst_amount DECIMAL(18, 2) DEFAULT 0,
    igst_amount DECIMAL(18, 2) DEFAULT 0,
    total_tax DECIMAL(18, 2) DEFAULT 0,
    grand_total DECIMAL(18, 2) NOT NULL,
    itc_eligible BOOLEAN DEFAULT true,
    reference_number VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSED', 'FILED', 'REJECTED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_gst_transactions_date ON gst_transactions(transaction_date);
CREATE INDEX idx_gst_transactions_type ON gst_transactions(transaction_type);
CREATE INDEX idx_gst_transactions_gstin ON gst_transactions(supplier_gstin, buyer_gstin);
CREATE INDEX idx_gst_transactions_status ON gst_transactions(status);

CREATE TABLE IF NOT EXISTS gst_line_items (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    gst_transaction_id UUID NOT NULL REFERENCES gst_transactions(id) ON DELETE CASCADE,
    hsn_code VARCHAR(8) NOT NULL,
    description TEXT NOT NULL,
    quantity DECIMAL(15, 2) NOT NULL,
    unit_price DECIMAL(18, 2) NOT NULL,
    line_amount DECIMAL(18, 2) NOT NULL,
    sgst_rate DECIMAL(5, 2) DEFAULT 0,
    cgst_rate DECIMAL(5, 2) DEFAULT 0,
    igst_rate DECIMAL(5, 2) DEFAULT 0,
    sgst_amount DECIMAL(18, 2) DEFAULT 0,
    cgst_amount DECIMAL(18, 2) DEFAULT 0,
    igst_amount DECIMAL(18, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gst_line_items_tx ON gst_line_items(gst_transaction_id);
CREATE INDEX idx_gst_line_items_hsn ON gst_line_items(hsn_code);


CREATE TABLE IF NOT EXISTS purchase_orders (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    po_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id UUID NOT NULL,
    po_date DATE NOT NULL,
    delivery_date DATE,
    total_amount DECIMAL(18, 2) NOT NULL,
    tax_amount DECIMAL(18, 2) DEFAULT 0,
    grand_total DECIMAL(18, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'REJECTED', 'RECEIVED', 'CANCELLED')),
    approved_by UUID,
    approved_at TIMESTAMP,
    payment_terms VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_purchase_orders_supplier ON purchase_orders(supplier_id);
CREATE INDEX idx_purchase_orders_date ON purchase_orders(po_date);
CREATE INDEX idx_purchase_orders_status ON purchase_orders(status);

CREATE TABLE IF NOT EXISTS purchase_order_items (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    purchase_order_id UUID NOT NULL REFERENCES purchase_orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity DECIMAL(15, 2) NOT NULL,
    unit_price DECIMAL(18, 2) NOT NULL,
    line_total DECIMAL(18, 2) NOT NULL,
    received_quantity DECIMAL(15, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_purchase_order_items_po ON purchase_order_items(purchase_order_id);

CREATE TABLE IF NOT EXISTS purchase_receipts (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    purchase_order_id UUID NOT NULL REFERENCES purchase_orders(id),
    receipt_date DATE NOT NULL,
    total_quantity DECIMAL(15, 2) NOT NULL,
    warehouse_id UUID NOT NULL,
    status VARCHAR(20) DEFAULT 'RECEIVED' CHECK (status IN ('RECEIVED', 'INSPECTED', 'ACCEPTED', 'REJECTED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_purchase_receipts_po ON purchase_receipts(purchase_order_id);
CREATE INDEX idx_purchase_receipts_warehouse ON purchase_receipts(warehouse_id);


CREATE TABLE IF NOT EXISTS suppliers (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    supplier_code VARCHAR(50) NOT NULL UNIQUE,
    supplier_name VARCHAR(255) NOT NULL,
    supplier_type VARCHAR(50) NOT NULL CHECK (supplier_type IN ('MANUFACTURER', 'DISTRIBUTOR', 'RETAILER', 'SERVICE_PROVIDER')),
    gstin VARCHAR(15) UNIQUE,
    pan VARCHAR(10) UNIQUE,
    email VARCHAR(255),
    phone VARCHAR(20),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    country VARCHAR(100),
    payment_terms VARCHAR(100),
    credit_limit DECIMAL(18, 2) DEFAULT 0,
    current_balance DECIMAL(18, 2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'BLACKLISTED', 'INACTIVE')),
    rating DECIMAL(3, 2) DEFAULT 0,
    approved_by UUID,
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_suppliers_code ON suppliers(supplier_code);
CREATE INDEX idx_suppliers_gstin ON suppliers(gstin);
CREATE INDEX idx_suppliers_status ON suppliers(status);
CREATE INDEX idx_suppliers_rating ON suppliers(rating);

CREATE TABLE IF NOT EXISTS supplier_contracts (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    supplier_id UUID NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,
    contract_number VARCHAR(50) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    contract_value DECIMAL(18, 2) NOT NULL,
    payment_terms VARCHAR(100),
    contract_terms TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'EXPIRED', 'TERMINATED', 'SUSPENDED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_supplier_contracts_supplier ON supplier_contracts(supplier_id);
CREATE INDEX idx_supplier_contracts_status ON supplier_contracts(status);


CREATE TABLE IF NOT EXISTS warehouses (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    warehouse_code VARCHAR(50) NOT NULL UNIQUE,
    warehouse_name VARCHAR(255) NOT NULL,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    country VARCHAR(100),
    capacity_units DECIMAL(15, 2) NOT NULL,
    warehouse_type VARCHAR(50) NOT NULL CHECK (warehouse_type IN ('DISTRIBUTION', 'REGIONAL', 'LOCAL', 'COLD_STORAGE')),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE', 'CLOSED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_warehouses_code ON warehouses(warehouse_code);
CREATE INDEX idx_warehouses_status ON warehouses(status);

CREATE TABLE IF NOT EXISTS warehouse_stock (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    warehouse_id UUID NOT NULL REFERENCES warehouses(id) ON DELETE RESTRICT,
    product_id UUID NOT NULL,
    quantity_on_hand DECIMAL(15, 2) NOT NULL,
    quantity_available DECIMAL(15, 2) NOT NULL,
    quantity_reserved DECIMAL(15, 2) DEFAULT 0,
    reorder_level DECIMAL(15, 2) NOT NULL,
    reorder_quantity DECIMAL(15, 2) NOT NULL,
    unit_cost DECIMAL(18, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_warehouse_stock UNIQUE (warehouse_id, product_id)
);

CREATE INDEX idx_warehouse_stock_warehouse ON warehouse_stock(warehouse_id);
CREATE INDEX idx_warehouse_stock_product ON warehouse_stock(product_id);

CREATE TABLE IF NOT EXISTS warehouse_transfers (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    transfer_number VARCHAR(50) NOT NULL UNIQUE,
    from_warehouse_id UUID NOT NULL REFERENCES warehouses(id),
    to_warehouse_id UUID NOT NULL REFERENCES warehouses(id),
    transfer_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'INITIATED' CHECK (status IN ('INITIATED', 'IN_TRANSIT', 'RECEIVED', 'CANCELLED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_warehouse_transfers_from ON warehouse_transfers(from_warehouse_id);
CREATE INDEX idx_warehouse_transfers_to ON warehouse_transfers(to_warehouse_id);
CREATE INDEX idx_warehouse_transfers_status ON warehouse_transfers(status);

CREATE TABLE IF NOT EXISTS warehouse_transfer_items (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    warehouse_transfer_id UUID NOT NULL REFERENCES warehouse_transfers(id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity DECIMAL(15, 2) NOT NULL,
    received_quantity DECIMAL(15, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_warehouse_transfer_items_transfer ON warehouse_transfer_items(warehouse_transfer_id);


CREATE TABLE IF NOT EXISTS erp_sync_logs (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    sync_type VARCHAR(50) NOT NULL CHECK (sync_type IN ('INVENTORY', 'SALES', 'PURCHASES', 'ACCOUNTS', 'SUPPLIERS')),
    erp_provider VARCHAR(50) NOT NULL CHECK (erp_provider IN ('TALLY_PRIME', 'ZOHO_BOOKS', 'ERPNEXT', 'SAP', 'ORACLE')),
    sync_status VARCHAR(20) DEFAULT 'PENDING' CHECK (sync_status IN ('PENDING', 'IN_PROGRESS', 'SUCCESS', 'FAILURE')),
    records_synced INTEGER DEFAULT 0,
    records_failed INTEGER DEFAULT 0,
    sync_start_time TIMESTAMP,
    sync_end_time TIMESTAMP,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_erp_sync_logs_type ON erp_sync_logs(sync_type);
CREATE INDEX idx_erp_sync_logs_provider ON erp_sync_logs(erp_provider);
CREATE INDEX idx_erp_sync_logs_status ON erp_sync_logs(sync_status);

CREATE TABLE IF NOT EXISTS erp_provider_config (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    erp_provider VARCHAR(50) NOT NULL UNIQUE CHECK (erp_provider IN ('TALLY_PRIME', 'ZOHO_BOOKS', 'ERPNEXT', 'SAP', 'ORACLE')),
    is_enabled BOOLEAN DEFAULT false,
    api_endpoint VARCHAR(500),
    api_key VARCHAR(255),
    api_secret VARCHAR(255),
    company_code VARCHAR(50),
    sync_frequency_minutes INTEGER DEFAULT 60,
    last_sync_time TIMESTAMP,
    configuration_data JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_erp_provider_config_enabled ON erp_provider_config(is_enabled);


CREATE TABLE IF NOT EXISTS feature_flags (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    feature_name VARCHAR(100) NOT NULL UNIQUE,
    feature_key VARCHAR(50) NOT NULL UNIQUE,
    is_enabled BOOLEAN DEFAULT false,
    description TEXT,
    target_users JSON,
    rollout_percentage INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID
);

CREATE INDEX idx_feature_flags_enabled ON feature_flags(is_enabled);


CREATE TABLE IF NOT EXISTS supplier_performance_metrics (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    supplier_id UUID NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,
    metric_period DATE NOT NULL,
    total_orders INTEGER DEFAULT 0,
    on_time_delivery_percentage DECIMAL(5, 2) DEFAULT 0,
    quality_rating DECIMAL(3, 2) DEFAULT 0,
    price_compliance_percentage DECIMAL(5, 2) DEFAULT 0,
    overall_score DECIMAL(3, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_supplier_perf UNIQUE (supplier_id, metric_period)
);

CREATE INDEX idx_supplier_performance_supplier ON supplier_performance_metrics(supplier_id);
CREATE INDEX idx_supplier_performance_period ON supplier_performance_metrics(metric_period);


CREATE TABLE IF NOT EXISTS financial_transaction_audits (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    transaction_id UUID NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    old_values JSON,
    new_values JSON,
    changed_by UUID NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    change_reason TEXT
);

CREATE INDEX idx_financial_audits_transaction ON financial_transaction_audits(transaction_id);
CREATE INDEX idx_financial_audits_timestamp ON financial_transaction_audits(changed_at);


