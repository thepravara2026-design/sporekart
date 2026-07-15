# Inspection Framework

## Overview
The Inspection Framework provides 11-status quality checks for received goods. Each receipt can be inspected across multiple dimensions, with pass/fail per check. Results feed into the acceptance or rejection decision.

## Inspection Types
| Type | Description | Status Values |
|------|-------------|---------------|
| Visual | Physical appearance check | pending → visual → passed/failed |
| Quality | Product quality assessment | pending → quality → passed/failed |
| Packaging | Packaging integrity check | pending → packaging → passed/failed |
| Quantity | Quantity verification | pending → quantity_verification → passed/failed |
| Documentation | Paperwork/documentation check | pending → documentation → passed/failed |
| Temperature | Temperature condition check (placeholder) | pending → temperature_check → passed/failed |
| Humidity | Humidity condition check (placeholder) | pending → humidity_check → passed/failed |
| Laboratory | Lab test results (placeholder) | pending → laboratory_test → passed/failed |

## InspectionRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Inspection ID (INSP-001) |
| `receiptId` | string | Linked receipt |
| `product` | string | Product name |
| `inspector` | string | Inspector user |
| `type` | string | Inspection type |
| `status` | InspectionStatus | Current status (11 values) |
| `packagingQuality` | boolean | Packaging quality pass/fail |
| `productQuality` | boolean | Product quality pass/fail |
| `quantityMatch` | boolean | Quantity match pass/fail |
| `labelVerified` | boolean | Label verification |
| `expiryVerified` | boolean | Expiry verification |
| `batchVerified` | boolean | Batch verification |
| `damageDetected` | boolean | Damage flag |
| `storageCompliant` | boolean | Storage compliance |
| `certified` | boolean | Certification flag |

## Inspection Page
- Full table showing all 30 inspection records
- Columns: Receipt, Product, Type, Inspector, Status, Quality, Packaging, Damage, Verified
- Checkmark (✓) or cross (✗) indicators for boolean checks
- StatusBadge with variant from inspection status config

## Future Extensions
- AI-powered visual inspection
- Automated temperature/humidity sensor integration
- Laboratory test result API
- Real-time inspection dashboard
