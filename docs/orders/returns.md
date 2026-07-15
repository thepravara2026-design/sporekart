# Returns Framework

The returns workflow provides a transparent system for customers to claim refunds for perishable spawn or contaminated goods.

## Claim Submission Flow

1. **Eligibility Check**: Checks order status and delivery timestamps. Return button is disabled if the item is older than the 10-day perishability window.
2. **Item Selection Checklist**: User selects which items to return from their order.
3. **Reason Selection**: Dropdown containing categorized reasons:
   - Contamination on arrival (default for spawn).
   - Transit damage / broken bags.
   - Incorrect item.
   - Quality / moisture level issues.
4. **Description**: Textarea for notes describing laboratory-related contamination details.
5. **Evidence Upload**: Drag-and-drop zone requiring photos of mold, broken bags, or wrong SKUs for QA checks.
6. **Submit**: Triggers submitting state and confirmation receipt screen.
