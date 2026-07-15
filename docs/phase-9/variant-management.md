# Variant Management

## Architecture

Single product can have multiple variants differentiated by attributes (weight, size, grade, kit type). Each variant has its own SKU, price, stock, and packaging.

## Variant Groups

| Group | Variants | Differentiation |
|-------|----------|----------------|
| Fresh Button Mushroom | 200g, 400g, 1kg | Weight |
| Dried Shiitake | 100g, 250g, 500g | Weight |
| Oyster Spawn | 350g, 1kg, 5kg | Weight |
| Hydroponic Kit | Beginner, Standard, Premium | Skill Level |

## Features

- Parent-child hierarchy with variant groups
- Default variant designation
- Priority ordering within groups
- Status workflow: draft → active → inactive → archived
- Barcode and QR code placeholders
- Variant matrix view for attribute comparison

## Future

Marketplace variant mapping, AI attribute suggestion, bulk variant generation from templates.
