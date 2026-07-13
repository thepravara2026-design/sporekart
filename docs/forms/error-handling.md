# Error Handling

## Overview

The form system provides multi-level error handling: inline field errors, section-level errors, form-level summary, and submission error handling with retry support.

## Field Errors

Inline errors appear below the field immediately after validation triggers (onBlur or onChange depending on configuration).

```tsx
<FormField name="email" label="Email" required>
  <Input
    aria-invalid={!!error}
    aria-describedby={error ? `${name}-error` : undefined}
    aria-errormessage={error ? `${name}-error` : undefined}
  />
  {touched && error && (
    <FormError id={`${name}-error`} role="alert">
      {error}
    </FormError>
  )}
</FormField>
```

| Validation Timing | Trigger | Behavior |
|---|---|---|
| `validateOnBlur` (default) | Field blur | Error appears after user leaves field |
| `validateOnChange` | Each change | Error appears as user types (debounced) |

## Section Errors

Sections can display a collective error banner when one or more fields within the section are invalid.

```tsx
<FormSection
  title="Address"
  error={hasSectionErrors ? 'Please fix the errors below' : undefined}
>
  <AddressFields />
</FormSection>
```

## Form-Level Errors (ValidationSummary)

The `ValidationSummary` component renders a list of all visible errors at the top of the form. Each error links to its corresponding field.

```tsx
<ValidationSummary
  errors={form.errors}
  touched={form.touched}
  onFieldFocus={(name) => document.getElementsByName(name)[0]?.focus()}
/>
```

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `errors` | `Record<string, string>` | — | All field errors |
| `touched` | `Record<string, boolean>` | — | Touched state |
| `showAll` | `boolean` | `false` | Show all errors (including untouched) |
| `title` | `string` | `'Please correct the following errors'` | Summary heading |

## Submission Errors

When `onSubmit` throws or returns a rejected promise, the form transitions to `failure` state. Errors can be set programmatically:

```tsx
const form = useForm({
  onSubmit: async (values) => {
    try {
      await api.submit(values);
    } catch (err) {
      if (err instanceof ValidationError) {
        // Set field-level errors from server
        return err.fields;
      }
      // Set form-level error
      throw new Error('Submission failed. Please try again.');
    }
  },
});
```

## Retry Support

When submission fails, the submit button shows "Retry" and the form can be resubmitted without losing field values.

```tsx
<FormActions
  submitText={submitFailed ? 'Retry' : 'Submit'}
  submitting={isSubmitting}
/>
```

## Focus First Invalid Field

On submission failure, the form automatically focuses the first field with an error.

```tsx
useEffect(() => {
  if (submitCount > 0 && Object.keys(errors).length > 0) {
    const firstError = Object.keys(errors)[0];
    document.getElementsByName(firstError)[0]?.focus();
  }
}, [submitCount, errors]);
```

## Screen Reader Announcements

| Event | Announcement |
|---|---|
| Field error | `"Error: {field} - {message}"` (via `aria-live="polite"`) |
| Form submission | `"Submitting form..."` |
| Submission success | `"Form submitted successfully"` |
| Submission failure | `"Form submission failed. {count} error(s) found."` |

```tsx
<div aria-live="polite" aria-atomic="true" className="sr-only">
  {announcement}
</div>
```

## Error Message Patterns

| Pattern | Example |
|---|---|
| Required | `"Email is required"` |
| Format | `"Enter a valid email address"` |
| Length | `"Password must be at least 8 characters"` |
| Range | `"Value must be between 18 and 150"` |
| Match | `"Passwords must match"` |
| Server | `"Email is already registered"` |
| Network | `"Connection lost. Please check your internet and try again."` |
| Timeout | `"Request timed out. Please try again."` |

## Internationalization

```tsx
// i18n integration
const { t } = useTranslation();

const validators = [
  required(t('forms.errors.required')),
  email(t('forms.errors.email')),
];

<ValidationSummary title={t('forms.errors.summary')} />
```
