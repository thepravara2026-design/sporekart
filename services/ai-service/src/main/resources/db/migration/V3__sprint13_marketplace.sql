CREATE TABLE IF NOT EXISTS vendors (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gst_number VARCHAR(64) NOT NULL,
    bank_account VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS vendor_profiles (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    contact_email VARCHAR(255),
    contact_phone VARCHAR(64),
    warehouse_address TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS vendor_documents (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    document_type VARCHAR(64) NOT NULL,
    document_url TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS vendor_products (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(64) NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS vendor_orders (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    order_reference VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vendor_commissions (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    category VARCHAR(64) NOT NULL,
    commission_rate NUMERIC(6,4) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vendor_settlements (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vendor_reviews (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    rating INTEGER NOT NULL,
    review_text TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vendor_support_tickets (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    subject VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vendor_regions (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL,
    region VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_vendors_status ON vendors(status);
CREATE INDEX IF NOT EXISTS idx_vendor_products_vendor_id ON vendor_products(vendor_id);
CREATE INDEX IF NOT EXISTS idx_vendor_orders_vendor_id ON vendor_orders(vendor_id);
CREATE INDEX IF NOT EXISTS idx_vendor_commissions_vendor_id ON vendor_commissions(vendor_id);
CREATE INDEX IF NOT EXISTS idx_vendor_settlements_vendor_id ON vendor_settlements(vendor_id);
CREATE INDEX IF NOT EXISTS idx_vendor_reviews_vendor_id ON vendor_reviews(vendor_id);
