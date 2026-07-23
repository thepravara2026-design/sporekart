# Analytics Engines

## Revenue Analytics Engine

Provides comprehensive revenue analysis across multiple dimensions.

### Supported Metrics

| Metric | Description |
|--------|-------------|
| `grossRevenue` | Total revenue before refunds |
| `netRevenue` | Revenue after refunds |
| `refundAmount` | Total refund value |
| `averageOrderValue` | Average revenue per order |
| `revenueGrowth` | MoM growth percentage |
| `orderCount` | Number of orders |
| `refundCount` | Number of refund transactions |
| `refundRate` | Refund amount as % of gross |

### Dimensions

| Dimension | Values |
|-----------|--------|
| Category | Mushroom Products, Training, Equipment, Substrates, Services |
| Product | 39 SKUs across all categories |
| Region | Maharashtra, Karnataka, Tamil Nadu, Punjab, Himachal |
| Customer Segment | Home Growers, Hobbyists, Commercial Farmers, Enterprise Buyers |
| Channel | Online Direct, Online Marketplace, Retail, Wholesale, Training Center |
| Training | Mushroom Cultivation 101, Advanced Oyster Farming, Commercial Farming Program, Disease Management Course, Spawn Production Workshop |

### Seed Data

- 24 months of monthly snapshots
- Seasonal revenue patterns (peak Oct-Feb, moderate Jun-Sep, low Mar-May)
- 5,000+ seed data points across all dimensions
- Category-based revenue distribution with stochastic variation

---

## Customer Analytics Engine

Provides customer behavior analysis, segmentation, and lifecycle metrics.

### Supported Metrics

| Metric | Description |
|--------|-------------|
| `totalCustomers` | Total active customer count |
| `newCustomers` | Customers acquired in period |
| `returningCustomers` | Repeat purchasers |
| `churnedCustomers` | Customers with no activity in 3+ months |
| `retentionRate` | % of customers who made >1 purchase |
| `churnRate` | % of customers who churned |
| `customerLifetimeValue` | Average total spend per customer |
| `repeatPurchaseRate` | % of customers who purchased more than once |
| `inactiveCustomers` | Customers with no recent activity |

### Dimensions

| Dimension | Values |
|-----------|--------|
| Segment | Home Growers, Hobbyists, Commercial Farmers, Enterprise Buyers |
| Region | Maharashtra, Karnataka, Tamil Nadu, Punjab, Himachal |

### Seed Data

- 5,000 customer records with randomized purchase histories
- 12 months of acquisition data
- Segment-based average order values: Home Growers (1,500), Hobbyists (5,000), Commercial Farmers (25,000), Enterprise Buyers (75,000)
- 5 regions with weighted distribution

---

## Product Analytics Engine

Provides product performance analysis, profitability, and trend detection.

### Supported Metrics

| Metric | Description |
|--------|-------------|
| `topProducts` | Top N products by revenue |
| `worstProducts` | Bottom N products by revenue |
| `fastMovers` | Products with growth > 5% |
| `slowMovers` | Products with low units or negative growth |
| `conversionRate` | Estimated conversion rate |
| `categoryPerformance` | Revenue, units, margin by category |

### Dimensions

| Dimension | Values |
|-----------|--------|
| Category | Mushroom Products, Training, Equipment, Substrates, Services |
| Product | 39 products (18 mushroom products, 5 training courses, 7 equipment, 5 substrates, 4 services) |

### Seed Data

- 12 months of product performance data
- Cost ratios per category (0.30 to 0.60)
- Seasonal factors applied across months
- Dynamic search trend and wishlist trend generation

---

## Inventory Analytics Engine

Provides inventory health monitoring, stock level analysis, and restocking recommendations.

### Supported Metrics

| Metric | Description |
|--------|-------------|
| `totalStock` | Total units across all products |
| `lowStockItems` | Items at or below reorder level |
| `deadStockItems` | Items with no sales or turnover > 180 days |
| `fastMoving` | Items with turnover < 30 days and > 10 units sold |
| `slowMoving` | Items with turnover > 90 days or < 5 units sold |
| `turnoverRate` | Sales-to-stock ratio |
| `restockingPriority` | Ordered list of items needing restock (Critical, High, Medium) |
| `inventoryRisk` | Composite risk score (0-100) |

### Dimensions

| Dimension | Values |
|-----------|--------|
| Category | Mushroom Products, Training Materials, Equipment, Substrates, Services |

### Seed Data

- 48 products across 5 categories
- Per-product: current stock, reorder level, safety stock, max stock, lead time, unit price
- 12 months of sales velocity data
- Dynamic stock depletion and restock simulation

---

## Training Analytics Engine

Provides training program analytics, batch performance, and trainer effectiveness.

### Supported Metrics

| Metric | Description |
|--------|-------------|
| `totalBatches` | Total batches in period |
| `activeBatches` | Batches in progress |
| `completedBatches` | Batches fully completed |
| `totalStudents` | Total enrolled students |
| `averageAttendance` | Average attendance rate |
| `averageScore` | Average assessment score |
| `completionRate` | % of attending students who completed |
| `certificationsIssued` | Number of certifications granted |

### Dimensions

| Dimension | Values |
|-----------|--------|
| Course | Mushroom Cultivation 101, Advanced Oyster Farming, Commercial Farming Program, Disease Management Course, Spawn Production Workshop |
| Trainer | 10 trainers with specializations |

### Seed Data

- 96 training batches across 12 months
- 10 trainers with ratings (3.5-5.0)
- Batch sizes: 20-30 students
- Course prices: 5,000 - 25,000 INR
- Enrollment, attendance, completion, and certification funnel tracking
