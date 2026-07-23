# Reporting Guide

## Overview

The Admin Copilot Reporting Engine generates structured reports from platform analytics data. Reports aggregate, filter, and format data into downloadable documents suitable for business review, compliance, and operational analysis.

## Report Types

### 1. Dashboard Snapshot Report

Captures the current state of the admin dashboard at a point in time.

| Property | Value |
|---|---|
| Report Type | `dashboard_snapshot` |
| Default Format | CSV |
| Content | All dashboard widgets with current values |
| Use Case | Daily business review, meeting prep |
| Example | Morning snapshot emailed to exec team |

**Sample Output (CSV):**

```csv
Metric,Value,Previous Value,Change %
Revenue Today,42500.00,38900.00,+9.3
Orders Today,142,128,+10.9
Average Order Value,47.50,45.20,+5.1
Conversion Rate,3.8,4.1,-7.3
New Customers,18,22,-18.2
Active Users Today,312,298,+4.7
Low Stock Items,11,9,+22.2
Out of Stock Items,2,1,+100.0
Inventory Value,1240000.00,1180000.00,+5.1
Training Completion Rate,92.0,90.5,+1.7
Platform Uptime Today,99.98,99.95,+0.03
Error Rate,0.02,0.03,-33.3
```

### 2. Sales Report

Detailed breakdown of sales performance over a specified period.

| Property | Value |
|---|---|
| Report Type | `sales` |
| Default Format | CSV |
| Available Dimensions | Product, Category, Region, Date |
| Metrics | Revenue, Orders, AOV, Refunds, Net Revenue, Discounts |
| Use Case | Monthly sales review, product performance analysis |

**Sample Output (CSV):**

```csv
Date,Product,Category,Revenue,Orders,AOV,Refunds,Net Revenue
2026-07-01,Oyster Mushroom Kit,Kits,2850.00,58,49.14,0.00,2850.00
2026-07-01,Shiitake Spawn Log,Logs,2100.00,35,60.00,42.00,2058.00
2026-07-01,Lion's Mane Grow Kit,Kits,1800.00,30,60.00,0.00,1800.00
2026-07-01,Substrate Premium,Materials,950.00,22,43.18,0.00,950.00
2026-07-01,Spore Syringe Variety Pack,Spores,720.00,18,40.00,18.00,702.00
2026-07-02,Oyster Mushroom Kit,Kits,3100.00,62,50.00,0.00,3100.00
2026-07-02,Shiitake Spawn Log,Logs,1950.00,30,65.00,0.00,1950.00
...
```

**Summary Section (appended to CSV):**

```csv
,,,,,,
PERIOD SUMMARY,,,,,,
Total Revenue,,84500.00,Total Orders,,1680
Avg Daily Revenue,,12071.43,Avg Daily Orders,,240
AOV (Period),,50.30,Refund Total,,420.00
Top Product,,Oyster Mushroom Kit,Top Category,,Kits
Growth vs Prev Period,,+12.3%,Growth vs YoY,,+8.1%
```

### 3. Inventory Report

Comprehensive inventory position with ABC classification.

| Property | Value |
|---|---|
| Report Type | `inventory` |
| Default Format | CSV |
| Available Dimensions | Product, Category, Classification |
| Metrics | Stock Qty, Reserved, Available, Days of Supply, Turnover, Value |
| Use Case | Procurement planning, warehouse management |

**Sample Output (CSV):**

```csv
SKU,Product,Category,Class,Stock,Reserved,Available,Days Supply,Turnover,Unit Cost,Total Value
KIT-OYSTER-001,Oyster Mushroom Kit,Kits,A,450,38,412,22,12.5,12.00,5400.00
LOG-SHII-001,Shiitake Spawn Log,Logs,A,280,25,255,30,9.8,18.50,5180.00
KIT-LIONS-001,Lion's Mane Grow Kit,Kits,A,190,15,175,28,10.2,22.00,4180.00
SUB-PREM-001,Substrate Premium,Materials,B,1200,45,1155,45,6.2,3.50,4200.00
SPORE-VAR-001,Spore Syringe Variety,Spores,B,850,30,820,60,4.8,8.00,6800.00
SUB-BASIC-001,Substrate Basic,Materials,C,3000,12,2988,90,3.1,1.50,4500.00
...

ABC SUMMARY,,,,,,,,,,
Class A (10 SKUs),,,,,,,,Value,,84500.00,78%
Class B (28 SKUs),,,,,,,,Value,,18200.00,17%
Class C (304 SKUs),,,,,,,,Value,,5400.00,5%
```

### 4. Customer Report

Customer segmentation and behavior analysis.

| Property | Value |
|---|---|
| Report Type | `customer` |
| Default Format | CSV |
| Available Dimensions | Cohort, Segment, Region |
| Metrics | New Customers, Active Customers, Churn Rate, CLV, Repeat Rate |
| Use Case | Marketing analysis, retention strategy |

**Sample Output (CSV):**

```csv
Cohort,Size,Month 1,Month 2,Month 3,Month 4,Month 5,Month 6
2026-01,450,100%,62%,48%,41%,36%,32%
2026-02,520,100%,58%,44%,38%,33%,
2026-03,490,100%,64%,50%,42%,,
2026-04,610,100%,60%,46%,,,
2026-05,580,100%,55%,,,,
2026-06,720,100%,,,,,,
```

**Segment Analysis:**

```csv
Segment,Customers,Total Revenue,AOV,Avg Orders,Lifetime Value,Churn Rate
Hobbyist,5200,962000.00,32.50,5.7,185.00,6.2%
Commercial,1800,2250000.00,185.00,6.8,1250.00,2.1%
Enthusiast,1450,754000.00,52.00,10.0,520.00,3.8%
```

### 5. Training Report

Training program performance analytics.

| Property | Value |
|---|---|
| Report Type | `training` |
| Default Format | CSV |
| Available Dimensions | Program, Category, Difficulty, Language |
| Metrics | Enrollments, Completions, Revenue, Utilization, Satisfaction |
| Use Case | Program effectiveness, capacity planning |

**Sample Output (CSV):**

```csv
Program,Difficulty,Language,Enrollments,Completed,Completion%,Revenue,Utilization%,Satisfaction
Beginner Mushroom Growing,Beginner,en,45,42,93.3,6750.00,90.0,4.8
Advanced Cultivation,Advanced,en,22,18,81.8,5500.00,73.3,4.5
Commercial Grower Workshop,Intermediate,en,30,28,93.3,9000.00,100.0,4.9
Lion's Mane Specialty,Intermediate,en,18,16,88.9,3600.00,60.0,4.6
Spanish Beginner Basic, Beginner,es,25,23,92.0,3750.00,83.3,4.7
```

## Export Formats

### CSV (Currently Supported)

CSV reports include a header row followed by data rows. Summary sections are appended after a blank separator row. All values are UTF-8 encoded with standard CSV escaping.

```csv
Metric,Value,Change
Revenue,42500.00,+9.3%
Orders,142,+10.9%
```

### PDF (Architecture for Future)

The architecture supports PDF generation through a pluggable renderer:

```java
public interface ReportRenderer {
    byte[] render(ReportData data, ReportFormat format);
    ReportFormat supportedFormat();
}

public class CsvRenderer implements ReportRenderer { ... }
public class PdfRenderer implements ReportRenderer { ... } // future
public class ExcelRenderer implements ReportRenderer { ... } // future
```

PDF rendering will use Apache PDFBox or JasperReports. The engine produces an intermediate `ReportData` object that each renderer converts to the target format. This allows PDF to support:
- Branded headers and footers
- Pagination with page numbers
- Summary charts (generated via JFreeChart)
- Tables with alternating row colors
- Embedded watermark for draft/confidential status

### Excel (Architecture for Future)

Excel support will be implemented via Apache POI, enabling:
- Multiple sheets per workbook (one per dimension)
- Pivot table generation
- Conditional formatting
- Named ranges for chart data sources

## Report Scheduling (Future Capability)

The scheduling subsystem will support cron-based and interval-based report generation:

```json
{
  "scheduleId": "sched-001",
  "reportType": "sales",
  "format": "csv",
  "cron": "0 8 * * 1", // Every Monday at 8:00 UTC
  "recipients": ["exec-team@sporekart.example"],
  "filters": {
    "period": "last_week",
    "granularity": "day"
  },
  "deliveryMethod": "email",
  "enabled": true
}
```

Schedule management will be exposed via:
- `POST /api/v1/copilot/admin/schedules`
- `GET /api/v1/copilot/admin/schedules`
- `DELETE /api/v1/copilot/admin/schedules/{id}`

## Custom Metrics Selection

When requesting a report, clients can specify which metrics to include via the `metrics` parameter. Available metrics depend on the report type:

### Sales Metrics

| Metric ID | Description | Formula |
|---|---|---|
| `revenue` | Total revenue | SUM(order.amount) |
| `orders` | Order count | COUNT(order.id) |
| `aov` | Average order value | revenue / orders |
| `refunds` | Total refunds | SUM(refund.amount) |
| `net_revenue` | Revenue minus refunds | revenue - refunds |
| `discounts` | Total discounts applied | SUM(discount.amount) |
| `conversion` | Conversion rate | orders / sessions * 100 |
| `units_sold` | Total units | SUM(item.quantity) |

### Inventory Metrics

| Metric ID | Description |
|---|---|
| `stock_qty` | Current stock quantity |
| `reserved_qty` | Reserved for orders |
| `available_qty` | Available for sale |
| `days_supply` | Days until stockout at current rate |
| `turnover` | Inventory turnover ratio |
| `value` | Inventory value at cost |
| `reorder_point` | Calculated reorder threshold |

### Customer Metrics

| Metric ID | Description |
|---|---|
| `new_customers` | New registrations |
| `active_customers` | Customers with activity |
| `churn_rate` | Monthly churn percentage |
| `clv` | Customer lifetime value |
| `repeat_rate` | Repeat purchase percentage |
| `retention_n` | Retention at month N |
| `segment_size` | Customers per segment |

### Training Metrics

| Metric ID | Description |
|---|---|
| `enrollments` | Enrollment count |
| `completions` | Completion count |
| `completion_rate` | Completion percentage |
| `revenue` | Training fee revenue |
| `utilization` | Seat utilization percentage |
| `satisfaction` | Average satisfaction score |

## Requesting a Report

```json
POST /api/v1/copilot/admin/report
{
  "type": "sales",
  "format": "csv",
  "periodStart": "2026-07-01T00:00:00Z",
  "periodEnd": "2026-07-23T23:59:59Z",
  "granularity": "day",
  "metrics": ["revenue", "orders", "aov", "conversion"],
  "dimensions": ["product", "category"],
  "filters": {
    "categoryIds": ["kits", "logs"],
    "region": "us-west"
  },
  "compareWith": "previous_period"
}
```

**Response (202 Accepted):**

```json
{
  "jobId": "report-job-001",
  "status": "pending",
  "estimatedCompletion": "2026-07-23T09:35:05Z",
  "downloadUrl": null
}
```

**Polling for completion:**

```json
GET /api/v1/copilot/admin/report/report-job-001
{
  "jobId": "report-job-001",
  "status": "completed",
  "estimatedCompletion": "2026-07-23T09:35:05Z",
  "completedAt": "2026-07-23T09:35:04Z",
  "downloadUrl": "/api/v1/copilot/admin/report/report-job-001/download",
  "expiresAt": "2026-07-24T09:35:04Z",
  "fileSize": 245000,
  "recordCount": 3450
}
```
