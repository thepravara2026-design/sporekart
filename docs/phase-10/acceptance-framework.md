# Acceptance & Rejection Framework

## Overview
After inspection, goods are either accepted or rejected. The framework supports 6 acceptance outcomes and 9 rejection reasons, with full traceability through the timeline and audit trail.

## Acceptance Statuses
| Status | Description |
|--------|-------------|
| `fully_accepted` | All goods accepted without conditions |
| `partially_accepted` | Some goods accepted, some rejected |
| `conditionally_accepted` | Accepted with conditions/notes |
| `accepted_with_notes` | Accepted but flagged for follow-up |
| `awaiting_approval` | Awaiting manager approval |
| `not_yet_accepted` | No decision made yet |

## Rejection Reasons
| Reason | Description |
|--------|-------------|
| `fully_rejected` | Entire shipment rejected |
| `partially_rejected` | Partial rejection |
| `damaged_goods` | Goods damaged in transit |
| `expired_goods` | Past expiry date |
| `packaging_failure` | Packaging integrity compromised |
| `quality_failure` | Failed quality inspection |
| `wrong_item` | Incorrect product delivered |
| `wrong_quantity` | Quantity mismatch |
| `missing_documents` | Incomplete documentation |

## Acceptance Page
- Table view of all approved/completed receipts
- Shows acceptance status and allocation status
- Quantity breakdown: total vs accepted

## Rejection Page
- Card-based layout with red border for rejected receipts
- Rejection reason highlighted in danger-colored banner
- Action buttons: Return to Supplier, Dispose, Re-inspect

## Integration Points
- Future: Supplier return portal
- Future: Auto-approval rules engine
- Future: Partial acceptance workflow
- Future: Return merchandise authorization (RMA)
