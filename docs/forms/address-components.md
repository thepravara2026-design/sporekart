# Address Form Components

## AddressValues Interface

```tsx
interface AddressValues {
  addressLine1: string;
  addressLine2?: string;
  landmark?: string;
  city: string;
  district: string;
  state: string;
  pinCode: string;
  country: string;
  addressType: 'home' | 'work' | 'other';
}
```

## AddressForm

A standalone address form that manages its own validation and state.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `initialValues` | `Partial<AddressValues>` | `{}` | Prefilled values |
| `onChange` | `(values: AddressValues) => void` | — | Change handler |
| `compact` | `boolean` | `false` | Compact mode (single column) |
| `states` | `string[]` | India states list | Available state options |
| `disabled` | `boolean` | `false` | Disable all fields |
| `hideAddressType` | `boolean` | `false` | Hide address type selector |

### Usage

```tsx
<AddressForm
  initialValues={user.address}
  onChange={(addr) => updateUserAddress(addr)}
/>
```

## AddressFields

Lower-level component for embedding address fields directly into an existing form.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `prefix` | `string` | `'address'` | Field name prefix (e.g. `shippingAddress`) |
| `compact` | `boolean` | `false` | Single column layout |
| `states` | `string[]` | India states list | State dropdown options |
| `hideAddressType` | `boolean` | `false` | Hide address type selector |

### Usage

```tsx
<FormSection title="Shipping Address">
  <AddressFields prefix="shipping" compact />
</FormSection>
```

## India-Standard Fields

| Field | Type | Validation | Notes |
|---|---|---|---|
| `addressLine1` | Text | required, maxLength(255) | Door/building/street |
| `addressLine2` | Text | maxLength(255) | Area/locality |
| `landmark` | Text | maxLength(100) | Near/opposite |
| `city` | Text | required, pattern(letters) | City/town/village |
| `district` | Select | required | District selection |
| `state` | Select | required | 36 states + UTs |
| `pinCode` | Text | required, pattern(/^\d{6}$/) | 6-digit PIN |
| `country` | Hidden | — | Defaults to "India" |
| `addressType` | Radio | required | Home / Work / Other |

### State List

```tsx
const INDIA_STATES = [
  'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh',
  'Goa', 'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand',
  'Karnataka', 'Kerala', 'Madhya Pradesh', 'Maharashtra', 'Manipur',
  'Meghalaya', 'Mizoram', 'Nagaland', 'Odisha', 'Punjab',
  'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura',
  'Uttar Pradesh', 'Uttarakhand', 'West Bengal',
  'Andaman and Nicobar Islands', 'Chandigarh', 'Dadra and Nagar Haveli and Daman and Diu',
  'Delhi', 'Jammu and Kashmir', 'Ladakh', 'Lakshadweep', 'Puducherry',
];
```

## Field-Level Validation Patterns

```tsx
const pinCodeValidators = [
  required('PIN Code is required'),
  pattern(/^\d{6}$/, 'PIN Code must be exactly 6 digits'),
];

const phoneValidators = [
  required('Phone is required'),
  pattern(/^[6-9]\d{9}$/, 'Enter a valid 10-digit Indian mobile number'),
];
```

## Compact Mode

```tsx
<AddressForm compact />
// Renders fields in a single column with reduced spacing
```

## Embedding in Other Forms

```tsx
<FormProvider {...form}>
  <FormLayout>
    <FormSection title="Contact">
      <FormField name="email" label="Email" />
      <FormField name="phone" label="Phone" />
    </FormSection>

    <FormSection title="Address">
      <AddressFields prefix="address" />
    </FormSection>

    <FormActions />
  </FormLayout>
</FormProvider>
```

## Accessibility

- All fields have associated `<label>` elements
- PIN code field has `inputMode="numeric"` and `pattern` for mobile keyboards
- State/City fields use searchable combobox for large option sets
- Error messages linked via `aria-describedby`
- Address type radio group wrapped in `<fieldset>` with `<legend>`
