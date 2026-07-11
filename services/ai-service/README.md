# SporeKart AI Service

This service implements the Sprint 11 AI foundation platform and the Sprint 12 AI customer experience, grower intelligence, admin assistant, knowledge retrieval, semantic search, and recommendation layers, plus Sprint 16 operations platform and ERP integration.

## Capabilities

- Provider abstraction for Gemini, OpenAI, Claude, Azure OpenAI, Local, and Mock providers
- Prompt orchestration and version-aware prompt assembly
- Conversation lifecycle support with history and delete flows
- Knowledge document repository and semantic search
- Recommendations and feedback endpoints
- RFC 9457-style problem details via a global exception handler
- Warehouse, procurement, and supplier management
- ERP integration and synchronization
- Finance accounting, journal entries, and reporting
- GST transaction processing and filing
- Mobile device management, push notifications, and offline sync
- B2B commerce (dealers, distributors, bulk orders, quotations, credit)
- Marketplace vendor management and commission rules
- Operations dashboard with aggregation across domains

## Key endpoints

### AI Platform
- `GET /ai/providers` — List available AI providers
- `POST /ai/providers` — Register an AI provider
- `GET /ai/prompts` — List prompts
- `POST /ai/prompts` — Create a prompt
- `GET /ai/conversations` — List conversations
- `POST /ai/conversations` — Start a conversation
- `GET /ai/chat/history` — Get chat history
- `DELETE /ai/chat/{sessionId}` — Delete a chat session
- `GET /ai/knowledge` — List knowledge documents
- `POST /ai/knowledge` — Upload a knowledge document
- `POST /ai/search` — Semantic search
- `GET /ai/recommendations` — Get recommendations
- `GET /ai/customer-assistant` — Customer assistant
- `GET /ai/grower-assistant` — Grower assistant
- `GET /ai/admin-assistant` — Admin assistant
- `POST /ai/feedback` — Submit feedback
- `POST /ai/chat` — Chat completion
- `POST /ai/embeddings` — Generate embeddings
- `GET /ai/metrics` — Service health metrics

### Warehouse Management
- `POST /warehouses` — Create a new warehouse
- `POST /warehouses/{warehouseId}/stock` — Add stock to a warehouse
- `POST /warehouses/transfer` — Transfer stock between warehouses
- `GET /warehouses/{warehouseId}/stock` — Get stock levels for a warehouse
- `GET /warehouses` — List all warehouses

### Supplier Management
- `POST /suppliers` — Register a new supplier
- `POST /suppliers/{supplierId}/approve` — Approve a supplier
- `POST /suppliers/{supplierId}/blacklist` — Blacklist a supplier
- `POST /suppliers/{supplierId}/rating` — Update supplier rating
- `GET /suppliers` — List suppliers (optional status filter)
- `GET /suppliers/{supplierId}` — Get supplier details

### Procurement
- `POST /purchase-orders` — Create a purchase order
- `POST /purchase-orders/{poId}/submit` — Submit a PO for approval
- `POST /purchase-orders/{poId}/approve` — Approve a PO
- `POST /purchase-orders/{poId}/reject` — Reject a PO
- `POST /purchase-orders/{poId}/mark-received` — Mark PO as received
- `GET /purchase-orders` — List purchase orders (optional status filter)
- `GET /purchase-orders/{poId}` — Get PO details

### ERP Integration
- `GET /erp/providers` — List available ERP providers
- `POST /erp/configuration` — Configure an ERP provider
- `POST /erp/configure` — Configure an ERP provider (alias)
- `POST /erp/sync` — Initiate ERP synchronization
- `POST /erp/sync/{syncId}/complete` — Mark sync as completed
- `POST /erp/sync/{syncId}/fail` — Mark sync as failed
- `GET /erp/status` — Get ERP provider status
- `GET /erp/sync-history` — Get ERP sync history

### Finance
- `POST /finance/accounts` — Create a finance account
- `GET /finance/accounts` — List chart of accounts
- `POST /finance/journal` — Create a journal entry
- `POST /finance/journal/{journalId}/lines` — Add a journal line
- `POST /finance/journal/{journalId}/post` — Post a journal entry
- `GET /finance/reports/trial-balance` — Get trial balance
- `GET /finance/reports/profit-loss` — Get profit and loss
- `GET /finance/reports/balance-sheet` — Get balance sheet

### GST
- `POST /gst/transactions` — Create a GST transaction
- `POST /gst/transactions/{txId}/process` — Process a GST transaction
- `POST /gst/transactions/{txId}/file` — File a GST return
- `GET /gst/reports` — Get GST report

### Operations
- `GET /operations/dashboard` — Get operations dashboard summary

### Mobile
- `POST /mobile/register-device` — Register a mobile device
- `POST /mobile/sync` — Sync offline changes
- `GET /mobile/config` — Get mobile configuration
- `GET /mobile/notifications` — Get push notifications
- `POST /mobile/push-token` — Register push notification token
- `GET /mobile/version` — Get app version info
- `POST /mobile/offline-sync` — Initiate offline sync
- `GET /mobile/settings` — Get mobile settings

### Marketplace / Vendors
- `POST /vendors/register` — Register a vendor
- `GET /vendors` — List vendors
- `GET /vendors/{id}` — Get vendor details
- `PUT /vendors/{id}` — Update vendor
- `POST /vendors/approve` — Approve a vendor
- `GET /vendors/dashboard` — Vendor dashboard
- `GET /vendors/products` — List vendor products
- `GET /vendors/settlements` — List settlements
- `GET /vendors/marketplace/products` — Marketplace products
- `GET /vendors/marketplace/vendors` — Marketplace vendors
- `GET /vendors/marketplace/search` — Marketplace search
- `GET /vendors/marketplace/reviews` — Marketplace reviews
- `GET /vendors/commission/rules` — Commission rules

### B2B Commerce
- `POST /dealers/register` — Register a dealer
- `GET /dealers` — List dealers
- `GET /dealers/dashboard` — Dealer dashboard
- `POST /distributors/register` — Register a distributor
- `GET /distributors` — List distributors
- `GET /corporate-accounts` — List corporate accounts
- `POST /quotations` — Create a quotation
- `GET /quotations` — List quotations
- `PUT /quotations/{id}` — Update a quotation
- `POST /bulk-orders` — Create a bulk order
- `GET /bulk-orders` — List bulk orders
- `GET /credit-accounts` — List credit accounts
- `POST /credit-accounts` — Create a credit account
- `GET /purchase-agreements` — List purchase agreements
- `GET /sales-territories` — List sales territories
- `GET /b2b/pricing` — Get B2B pricing
