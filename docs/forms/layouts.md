# Form Layout Components

## FormLayout

The root container for all forms. Supports single and two-column layouts with responsive behavior.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `columns` | `1 \| 2` | `1` | Number of layout columns |
| `maxWidth` | `string` | `'720px'` | Maximum container width |
| `gap` | `string` | `'24px'` | Gap between fields/rows |
| `responsive` | `boolean` | `true` | Collapse to single column on mobile |

### Two-Column Example

```tsx
<FormLayout columns={2} maxWidth="960px">
  <FormField name="firstName" label="First Name" required />
  <FormField name="lastName" label="Last Name" required />
  <FormRow columns={2}>
    <FormField name="city" label="City" />
    <FormField name="state" label="State" />
  </FormRow>
</FormLayout>
```

## FormSection

Groups related fields with an optional title, description, and collapsible behavior.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `title` | `string` | — | Section heading |
| `description` | `string` | — | Helper text below title |
| `collapsible` | `boolean` | `false` | Allow collapse/expand |
| `defaultOpen` | `boolean` | `true` | Initial collapsed state |
| `required` | `boolean` | `false` | Show "Required" indicator |

```tsx
<FormSection title="Personal Information" description="Your basic contact details" required>
  <FormField name="name" label="Full Name" />
  <FormField name="phone" label="Phone" />
</FormSection>
```

## FormField

Renders a single field with label, helper text, validation error, required indicator, and character counter.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `name` | `string` | — | Field name (registered via useField) |
| `label` | `string` | — | Visible label |
| `helperText` | `string` | — | Additional description |
| `required` | `boolean` | `false` | Show required asterisk |
| `maxLength` | `number` | — | Show character counter |
| `children` | `ReactNode` | — | The input control |

```tsx
<FormField name="bio" label="Bio" helperText="Tell us about yourself" maxLength={500}>
  <Textarea />
</FormField>
```

## FormRow

Arranges child fields in a horizontal row with responsive collapse.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `columns` | `number` | children count | Number of columns |
| `gap` | `string` | — | Inherits from FormLayout |
| `responsive` | `boolean` | `true` | Stack on mobile |

```tsx
<FormRow columns={3}>
  <FormField name="house" label="House" />
  <FormField name="street" label="Street" />
  <FormField name="pin" label="PIN Code" />
</FormRow>
```

## FormActions

Renders submit and optional cancel buttons with loading state.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `submitText` | `string` | `'Submit'` | Submit button label |
| `cancelText` | `string` | `'Cancel'` | Cancel button label |
| `onCancel` | `() => void` | — | Cancel handler |
| `submitting` | `boolean` | — | Override submitting state |
| `align` | `'left' \| 'right' \| 'center'` | `'right'` | Button alignment |

```tsx
<FormActions submitText="Save Profile" onCancel={() => navigate(-1)} />
```

## FormFooter

A sticky footer that remains visible during scroll.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `sticky` | `boolean` | `true` | Enable sticky positioning |
| `children` | `ReactNode` | — | Footer content |

```tsx
<FormFooter>
  <FormActions submitText="Next" />
</FormFooter>
```

## Layout Composition

```tsx
<FormLayout maxWidth="800px">
  <FormSection title="Account" collapsible>
    <FormRow columns={2}>
      <FormField name="email" label="Email" required />
      <FormField name="phone" label="Phone" />
    </FormRow>
  </FormSection>

  <FormSection title="Address">
    <FormField name="addressLine1" label="Address Line 1" required />
    <FormField name="addressLine2" label="Address Line 2" />
    <FormRow columns={3}>
      <FormField name="city" label="City" required />
      <FormField name="state" label="State" required />
      <FormField name="pinCode" label="PIN Code" required />
    </FormRow>
  </FormSection>

  <FormFooter>
    <FormActions submitText="Save" onCancel={handleCancel} />
  </FormFooter>
</FormLayout>
```

## Token Usage

| Token | Default | Usage |
|---|---|---|
| `--form-max-width` | `720px` | FormLayout maxWidth |
| `--form-gap` | `24px` | Vertical spacing |
| `--form-field-gap` | `16px` | Field row gap |
| `--form-section-gap` | `32px` | Between sections |
| `--form-label-weight` | `600` | Label font weight |
| `--form-helper-size` | `0.875rem` | Helper/error text size |
| `--form-error-color` | `var(--color-danger)` | Error text color |
