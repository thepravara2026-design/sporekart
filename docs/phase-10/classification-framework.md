# Classification Framework

## Overview
The Classification Framework provides a two-dimensional taxonomy system for Inventory Items: Type (vertical) and Grade (horizontal). Together they define the operational characteristics and quality tier of every item.

## Classification Type
| Type | Label | Description |
|------|-------|-------------|
| `raw_material` | Raw Material | Unprocessed mushroom cultivation inputs |
| `work_in_progress` | Work in Progress | Materials in production or spawning phase |
| `finished_good` | Finished Good | Ready for sale or distribution |
| `consumable` | Consumable | Disposable supplies and consumables |
| `asset` | Asset | Long-term capital equipment and fixtures |
| `packaging` | Packaging | Packing materials, labels and containers |
| `supplies` | Supplies | General supplies and maintenance items |

## Classification Grade
| Grade | Label | Description |
|-------|-------|-------------|
| `a_plus` | A+ | Premium grade — highest quality |
| `a` | A | Standard grade — meets all specifications |
| `b` | B | Economy grade — minor deviations acceptable |
| `c` | C | Utility grade — for processing or discounted channels |
| `unclassified` | Unclassified | Grade not yet assigned |

## Data Model
```typescript
interface Classification {
  type: ClassificationType;
  category: string;        // From product type (spawn, compost, etc.)
  subcategory: string;     // From classification type label
  class: string;           // From product name
  grade: ClassificationGrade;
}
```

## UI
- `ClassificationPage`: displays all types and grades in a card grid
- `ClassificationBadge`: inline badge showing classification type with color coding
- Registry table includes classification type and grade columns

## Default Mappings
- spawn products → raw_material or finished_good
- compost products → raw_material
- tools → asset or consumable
- packaging → packaging
- supplements → consumable

## Coverage
Mock data reports 95.8% classification coverage across 14,820 items.
