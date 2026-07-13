# Stepper

## Overview

The Stepper component guides users through a sequence of steps. It supports horizontal, vertical, and progress variants with completed, active, and pending states.

## Variants

| Variant | Description |
|---------|-------------|
| `horizontal` | Steps laid out left-to-right with connector lines |
| `vertical` | Steps stacked with connector lines on the left |
| `progress` | Compact progress bar showing overall completion |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `steps` | `StepData[]` | — | Step definitions |
| `activeStep` | `number` | — | Currently active step index (0-indexed) |
| `onStepClick` | `(index: number) => void` | — | Step click callback |
| `variant` | `'horizontal' \| 'vertical' \| 'progress'` | `'horizontal'` | Layout variant |
| `clickable` | `boolean` | `false` | Allow clicking completed steps |
| `orientation` | `'horizontal' \| 'vertical'` | `'horizontal'` | Orientation (horizontal only) |
| `className` | `string` | — | Additional CSS classes |

## StepData Interface

```ts
interface StepData {
  id: string;
  label: string;
  description?: string;
  icon?: ReactNode;
  optional?: boolean;
  status?: 'pending' | 'active' | 'completed' | 'error' | 'skipped';
}
```

## Step Component

Individual step indicator.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `step` | `StepData` | — | Step data |
| `index` | `number` | — | Zero-based index |
| `active` | `boolean` | — | Is active step |
| `completed` | `boolean` | — | Is completed |
| `clickable` | `boolean` | — | Is clickable |
| `onClick` | `() => void` | — | Click handler |
| `variant` | `'horizontal' \| 'vertical'` | — | Visual variant |

## States

### Completed

- Green checkmark icon
- Connector line is filled/colored
- Clickable if `clickable` is true

### Active

- Primary color circle (filled or outlined)
- Bold label text
- Connector line before is filled, after is pending

### Pending

- Gray/neutral circle (outlined)
- Default text weight
- Connector line is empty/gray

### Error

- Red circle with error icon
- Step is clickable to fix

### Skipped

- Dashed circle outline
- Optional steps only

## Clickable Steps

When `clickable` is true and steps are completed, users can click on any completed step to navigate back. Non-completed steps are not clickable.

## Connector Lines

- Horizontal: Lines between step circles
- Vertical: Lines between step circles on the left side
- Completed segments: filled with primary color
- Pending segments: gray/dashed

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-stepper-completed` | Completed step color |
| `--color-stepper-active` | Active step color |
| `--color-stepper-pending` | Pending step color |
| `--color-stepper-error` | Error step color |
| `--color-stepper-text` | Step label text |
| `--color-stepper-connector` | Connector line color |
| `--spacing-stepper` | Gap between steps |
| `--radius-stepper-circle` | Step circle radius |
| `--font-size-stepper` | Step label size |
| `--font-weight-stepper-active` | Active label weight |
