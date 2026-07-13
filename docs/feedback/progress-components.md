# Progress Components

## Overview

Progress components communicate operation status, completion percentage, and multi-step workflow progress. They range from simple spinners to complex multi-step indicators with validation states.

### Component Hierarchy

```
Progress
├── LinearProgress
├── CircularProgress
├── StepProgress
├── IndeterminateProgress
├── UploadProgress
└── TaskProgress
```

---

## Variants

| Component | Type | Best For |
|-----------|------|----------|
| **LinearProgress** | Determinate / Indeterminate | Page loads, form submissions, file processing |
| **CircularProgress** | Determinate / Indeterminate | Button loading states, inline operations |
| **StepProgress** | Discrete steps | Multi-step forms, wizard progress, workflow tracking |
| **IndeterminateProgress** | Indeterminate animation | Unknown duration operations |
| **UploadProgress** | Determinate with details | File uploads with speed, ETA, file name |
| **TaskProgress** | Multi-task aggregate | Bulk operations, batch processing |

---

## Props

### LinearProgress Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `number` (0-100) | — | Determinate progress. Omit for indeterminate. |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Height: 4px / 8px / 12px |
| `color` | `string` | `--color-primary` | Bar color |
| `trackColor` | `string` | `--color-surface-alt` | Track background color |
| `animate` | `boolean` | `true` | Smooth transition animation |
| `rounded` | `boolean` | `true` | Rendered with rounded corners |
| `label` | `string` | — | Screen reader label |
| `showValue` | `boolean` | `false` | Show percentage text |
| `valuePosition` | `'inside' \| 'outside' \| 'tooltip'` | `'outside'` | Value label position |
| `buffer` | `number` (0-100) | — | Secondary buffer bar (for streaming) |

### CircularProgress Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `number` (0-100) | — | Determinate progress. Omit for indeterminate spinner. |
| `size` | `'sm' \| 'md' \| 'lg' \| 'xl'` | `'md'` | Diameter: 24px / 40px / 56px / 80px |
| `strokeWidth` | `number` | `4` | Ring thickness |
| `color` | `string` | `--color-primary` | Ring color |
| `trackColor` | `string` | `--color-surface-alt` | Track color |
| `label` | `string` | — | Screen reader label |
| `showValue` | `boolean` | `true` | Show percentage in center |
| `animate` | `boolean` | `true` | Animate value changes |

### StepProgress Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `steps` | `Step[]` | — | Step definitions |
| `currentStep` | `number` | `0` | Active step index (0-based) |
| `orientation` | `'horizontal' \| 'vertical'` | `'horizontal'` | Layout direction |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Indicator size |
| `clickable` | `boolean` | `false` | Allow clicking steps to navigate |
| `onStepClick` | `(index: number) => void` | — | Step click handler |
| `showConnector` | `boolean` | `true` | Show connector lines between steps |
| `variant` | `'numbered' \| 'icon' \| 'dot'` | `'numbered'` | Step indicator style |

```typescript
interface Step {
  id: string;
  title: string;
  description?: string;
  icon?: ReactNode;
  status?: 'pending' | 'active' | 'completed' | 'error' | 'skipped';
  optional?: boolean;
}
```

### UploadProgress Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `fileName` | `string` | — | Current file name |
| `fileSize` | `number` | — | Total file size in bytes |
| `uploadedBytes` | `number` | `0` | Bytes uploaded so far |
| `speed` | `number` | — | Upload speed in bytes/sec |
| `eta` | `number` | — | Estimated time remaining in seconds |
| `status` | `'uploading' \| 'processing' \| 'completed' \| 'error' \| 'cancelled'` | `'uploading'` | Current status |
| `onCancel` | `() => void` | — | Cancel upload handler |
| `onRetry` | `() => void` | — | Retry upload handler |
| `error` | `string` | — | Error message on failure |
| `formatSpeed` | `(bytes: number) => string` | — | Speed formatter |
| `formatEta` | `(seconds: number) => string` | — | ETA formatter |

### TaskProgress Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `tasks` | `Task[]` | — | Individual task list |
| `showProgressBar` | `boolean` | `true` | Show aggregate progress bar |
| `collapsible` | `boolean` | `true` | Allow collapsing task list |
| `showTaskCount` | `boolean` | `true` | Show "3 of 10 completed" |

```typescript
interface Task {
  id: string;
  label: string;
  status: 'pending' | 'running' | 'completed' | 'failed' | 'skipped';
  error?: string;
  progress?: number; // 0-100 for individual task progress
}
```

---

## ARIA Attributes

| Component | Role | Attributes |
|-----------|------|------------|
| LinearProgress | `progressbar` | `aria-valuenow`, `aria-valuemin="0"`, `aria-valuemax="100"`, `aria-label` |
| CircularProgress | `progressbar` | `aria-valuenow`, `aria-valuemin="0"`, `aria-valuemax="100"`, `aria-label` |
| IndeterminateProgress | `progressbar` | `aria-label`, omit `aria-valuenow` |
| StepProgress | `navigation` | `aria-label="Step progress"`, `aria-current="step"` on active step |
| UploadProgress | `progressbar` | `aria-valuenow`, `aria-valuetext="{fileName} - {progress}%"` |
| TaskProgress | `group` | `aria-label="Task progress: {completed} of {total}"` |

---

## Examples

### LinearProgress for page loading

```tsx
import { LinearProgress } from '@sporekart/ui';

function PageLoader({ isLoading }: { isLoading: boolean }) {
  if (!isLoading) return null;

  return <LinearProgress size="sm" aria-label="Loading page" />;
}
```

### CircularProgress for button state

```tsx
import { CircularProgress, Button } from '@sporekart/ui';

function SubmitButton({ loading }: { loading: boolean }) {
  return (
    <Button disabled={loading}>
      {loading ? (
        <>
          <CircularProgress size="sm" aria-label="Saving" />
          Saving...
        </>
      ) : (
        'Save Changes'
      )}
    </Button>
  );
}
```

### StepProgress for wizard

```tsx
import { StepProgress } from '@sporekart/ui';

const wizardSteps: Step[] = [
  { id: 'info', title: 'Basic Info', status: 'completed' },
  { id: 'details', title: 'Details', status: 'active' },
  { id: 'review', title: 'Review', status: 'pending' },
  { id: 'confirm', title: 'Confirmation', status: 'pending' },
];

function SetupWizardProgress({ currentStep }: { currentStep: number }) {
  return (
    <StepProgress
      steps={wizardSteps}
      currentStep={currentStep}
      orientation="horizontal"
      variant="numbered"
      clickable
      onStepClick={navigateToStep}
    />
  );
}
```

### UploadProgress for file upload

```tsx
import { UploadProgress } from '@sporekart/ui';

function UploadItem({ file, onCancel }: UploadItemProps) {
  return (
    <UploadProgress
      fileName={file.name}
      fileSize={file.size}
      uploadedBytes={file.uploaded}
      speed={file.speed}
      eta={file.eta}
      status={file.status}
      onCancel={onCancel}
      onRetry={() => retryUpload(file.id)}
      error={file.error}
    />
  );
}
```

### TaskProgress for batch operations

```tsx
import { TaskProgress } from '@sporekart/ui';

function BulkImportProgress({ tasks }: { tasks: Task[] }) {
  return (
    <TaskProgress
      tasks={tasks}
      collapsible
      showTaskCount
      showProgressBar
    />
  );
}
```

---

## Best Practices

- **Determinate vs Indeterminate**: Always use determinate when you can estimate duration. Switch to determinate once progress is known (e.g., after file size is read).
- **Buffer bar**: Use `buffer` on LinearProgress for streaming scenarios (video loading, data fetching) to show secondary progress.
- **Show value**: Show percentage on CircularProgress only when space allows (> 56px diameter). For small sizes, use `aria-label` only.
- **Step validation**: Each step in StepProgress should have a validation function. Show error state on the step that failed.
- **Upload ETA**: Update speed and ETA at least every second. Smooth the values to avoid jitter (moving average over 3-5 samples).
- **Task grouping**: Batch 50+ operations into TaskProgress. Show individual progress for groups of < 20 tasks.
- **Animation**: Progress bar animations should use CSS transforms for GPU acceleration. Duration: 300ms transition.
- **Accessibility**: Always include `aria-label` or `aria-labelledby`. Announce completion to screen readers via `aria-live` region.
