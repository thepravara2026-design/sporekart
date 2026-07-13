# Enterprise Form System

## Overview

The Form System provides a declarative, type-safe framework for building enterprise-grade forms. Built on React Context and controlled components, it separates state management from UI rendering, enforces validation rules, and integrates seamlessly with the Design System component library.

### Architecture

```
FormProvider (Context)
  └─ useForm (form state + lifecycle)
       └─ useField (field-level state)
            └─ UI Components (FormField, Select, FileUpload, etc.)
```

| Layer | Responsibility |
|---|---|
| `FormProvider` | Wraps a form; provides context to all children |
| `useForm` | Manages values, errors, touched, dirty, submission lifecycle |
| `useField` | Registers a field, provides value/error/t onChange/onBlur |

### State Management

| Slice | Type | Description |
|---|---|---|
| `values` | `Record<string, any>` | Current field values |
| `errors` | `Record<string, string>` | Validation error messages keyed by field name |
| `touched` | `Record<string, boolean>` | Fields that have been blurred |
| `dirty` | `boolean` | `true` if any field has been modified from initial |

### Form Lifecycle

```
idle → validating → submitting → success
                                 → failure → idle (retry)
```

| State | Description |
|---|---|
| `idle` | Form ready for input |
| `validating` | Running validation rules |
| `submitting` | `onSubmit` handler executing |
| `success` | Submission succeeded |
| `failure` | Submission failed; user may retry |

### Registration Pattern

Each field calls `useField(name, options)` which registers itself with the parent `FormContext`. Registration provides:
- Automatic value/error/touched tracking
- `onChange` / `onBlur` wiring
- Validation trigger binding
- Cleanup on unmount

```tsx
const { value, error, touched, onChange, onBlur } = useField('email');
```

## Type Definitions

```tsx
interface FormState {
  values: Record<string, any>;
  errors: Record<string, string>;
  touched: Record<string, boolean>;
  dirty: boolean;
  isSubmitting: boolean;
  isValidating: boolean;
  submitCount: number;
}

interface UseFormOptions<T = Record<string, any>> {
  initialValues: T;
  validate?: (values: T) => Record<string, string>;
  validateOnChange?: boolean;
  validateOnBlur?: boolean;
  onSubmit: (values: T) => Promise<void> | void;
}

interface UseFieldOptions {
  name: string;
  validate?: (value: any, values: Record<string, any>) => string | undefined;
  validateOnChange?: boolean;
  validateOnBlur?: boolean;
}
```

## Provider Setup

```tsx
import { FormProvider, useForm } from '@sporekart/forms';

function MyForm() {
  const form = useForm({
    initialValues: { email: '', password: '' },
    onSubmit: async (values) => {
      await api.submit(values);
    },
  });

  return (
    <FormProvider {...form}>
      <FormLayout>
        <FormField name="email" label="Email" required />
        <FormField name="password" label="Password" type="password" required />
        <FormActions />
      </FormLayout>
    </FormProvider>
  );
}
```

## Integration with Sprint 20 Part 2 Components

All Sprint 20 Part 2 components (`Button`, `Input`, `Textarea`, `Checkbox`, `Radio`, `Switch`) integrate via `useField`:

```tsx
function FormTextField({ name, label }: FormTextFieldProps) {
  const { value, error, touched, onChange, onBlur } = useField(name);
  return (
    <Input
      value={value}
      onChange={(e) => onChange(e.target.value)}
      onBlur={onBlur}
      invalid={touched && !!error}
      helperText={touched && error}
      label={label}
    />
  );
}
```

## Accessibility

- All form controls use `<label>` elements with proper `htmlFor`
- `aria-invalid` set when field has error and is touched
- `aria-describedby` links to error messages
- `aria-errormessage` for screen reader announcements
- Keyboard navigation via Tab/Shift+Tab
- Focus management: first invalid field on submission error
- Screen reader announcements on form submission status
