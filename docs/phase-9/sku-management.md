# SKU Management

## SKU Pattern

`{Category-Prefix}-{Product-Code}-{Variant-Identifier}`

Examples:
- `FR-MSH-200` — Fresh Mushroom 200g
- `DR-SHT-100` — Dried Shiitake 100g
- `KIT-HYD-BEG` — Hydroponic Kit Beginner

## Features

| Feature | Description |
|---------|-------------|
| Auto-generate | System-generated SKU based on pattern |
| Manual | Administrator-defined SKU |
| Prefix/Suffix | Configurable pre/post fix |
| Category Prefix | SKU starts with category code |
| Brand Prefix | SKU starts with brand code |
| Variant Prefix | SKU starts with variant code |
| Duplicate Warning | Flags matching SKUs |
| Reserved SKU | Marks SKU as unavailable |

## Status

- Active — available for use
- Inactive — deprecated
- Reserved — held for future use

## Future

Bulk SKU generation, marketplace-specific SKU mapping, GS1 standardization.
