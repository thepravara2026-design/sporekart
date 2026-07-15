# Form Template — Form Standard

Forms follow a strict composition: `FormLayout` → `FormSection` → `FormField` → input, with `FormActions` for actions. All inputs come from the design-system forms or `../components/forms`.

```ts
import { FormLayout, FormSection, FormField, FormRow, FormActions, FormFooter }
  from '../../../design-system/components/forms';
import { Textarea, NumberInput, CurrencyInput, PhoneInput, EmailInput, TimePicker, TagSelector }
  from '../components/forms';
```

## FormField props

`FormField` accepts: `children`, `name?`, `label?`, `description?`, `helperText?`, `error?`, `warning?`, `success?`, `required?`, `disabled?`, `readOnly?`, `characterLimit?`, `currentLength?`, `className?`, `layout?: 'vertical'|'horizontal'`, `id?`.

## Complete create/edit form skeleton

```tsx
import { memo, useState } from 'react';
import { FormLayout, FormSection, FormField, FormActions }
  from '../../../design-system/components/forms';
import { Textarea, NumberInput, CurrencyInput } from '../components/forms';
import { PermissionGate } from '../permissions/PermissionGate';

interface ProductFormValues {
  name: string;
  description: string;
  price: number;
  stock: number;
}

export const ProductForm = memo(function ProductForm({
  initial,
  onSubmit,
  onCancel,
  saving = false,
}: {
  initial?: Partial<ProductFormValues>;
  onSubmit: (v: ProductFormValues) => void;
  onCancel: () => void;
  saving?: boolean;
}) {
  const [values, setValues] = useState<ProductFormValues>({
    name: initial?.name ?? '',
    description: initial?.description ?? '',
    price: initial?.price ?? 0,
    stock: initial?.stock ?? 0,
  });
  const [errors, setErrors] = useState<Partial<Record<keyof ProductFormValues, string>>>({});

  const validate = (): boolean => {
    const next: Partial<Record<keyof ProductFormValues, string>> = {};
    if (!values.name.trim()) next.name = 'Name is required';
    if (values.price < 0) next.price = 'Price cannot be negative';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = () => {
    if (validate()) onSubmit(values);
  };

  return (
    <FormLayout>
      <FormSection title="Product details" description="Basic product information">
        <FormField
          label="Name"
          required
          name="name"
          error={errors.name}
          characterLimit={120}
          currentLength={values.name.length}
        >
          <Textarea
            value={values.name}
            onChange={(e) => setValues((v) => ({ ...v, name: e.target.value }))}
          />
        </FormField>

        <FormField label="Description" name="description" helperText="Optional marketing copy">
          <Textarea
            value={values.description}
            onChange={(e) => setValues((v) => ({ ...v, description: e.target.value }))}
          />
        </FormField>

        <FormRow>
          <FormField label="Price" name="price" error={errors.price} required>
            <CurrencyInput
              value={values.price}
              onChange={(v) => setValues((s) => ({ ...s, price: v }))}
            />
          </FormField>
          <FormField label="Stock" name="stock">
            <NumberInput
              value={values.stock}
              onChange={(v) => setValues((s) => ({ ...s, stock: v }))}
            />
          </FormField>
        </FormRow>
      </FormSection>

      <FormActions>
        <PermissionGate action={initial ? 'update' : 'create'} resource="products">
          <Button onClick={handleSubmit} disabled={saving}>
            {saving ? 'Saving…' : initial ? 'Save' : 'Create'}
          </Button>
        </PermissionGate>
        <Button variant="ghost" onClick={onCancel}>Cancel</Button>
      </FormActions>
    </FormLayout>
  );
});
```

## Rules

- Wrap the submit button in the correct `PermissionGate` (`create` for new, `update` for edit).
- Surface validation via `FormField` `error`/`warning`/`success`; never inline raw text alerts.
- Use `FormRow` to group related fields horizontally; `FormFooter` for sticky footers.
- Manage `saving`/`success`/`error` in the page (lifted state). Show `saving` on the submit button.
- Use `characterLimit` + `currentLength` for bounded inputs.
- Validation is field-level and client-side; server errors map back to `FormField error`.
