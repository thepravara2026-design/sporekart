# Validation Framework

## Overview

The validation framework provides a composable, type-safe API for defining field-level and form-level validation rules. Supports synchronous, asynchronous, and cross-field validators with internationalized error messages.

## Available Validators

### `required`

| Param | Type | Default | Description |
|---|---|---|---|
| `message` | `string` | `'This field is required'` | Error message |

```ts
required('Email is required')
```

### `minLength`

| Param | Type | Default | Description |
|---|---|---|---|
| `min` | `number` | — | Minimum character count |
| `message` | `string` | — | Custom message |

```ts
minLength(8, 'Password must be at least 8 characters')
```

### `maxLength`

| Param | Type | Default | Description |
|---|---|---|---|
| `max` | `number` | — | Maximum character count |
| `message` | `string` | — | Custom message |

```ts
maxLength(200, 'Bio must be under 200 characters')
```

### `min`

| Param | Type | Default | Description |
|---|---|---|---|
| `min` | `number` | — | Minimum numeric value |
| `message` | `string` | — | Custom message |

```ts
min(18, 'You must be at least 18 years old')
```

### `max`

| Param | Type | Default | Description |
|---|---|---|---|
| `max` | `number` | — | Maximum numeric value |
| `message` | `string` | — | Custom message |

```ts
max(150, 'Age must be under 150')
```

### `pattern`

| Param | Type | Default | Description |
|---|---|---|---|
| `regex` | `RegExp` | — | Pattern to match |
| `message` | `string` | — | Error message |

```ts
pattern(/^[A-Za-z\s]+$/, 'Only letters allowed')
```

### `email`

| Param | Type | Default | Description |
|---|---|---|---|
| `message` | `string` | `'Invalid email address'` | Error message |

```ts
email('Please enter a valid email')
```

### `phone`

| Param | Type | Default | Description |
|---|---|---|---|
| `message` | `string` | `'Invalid phone number'` | Error message |

```ts
phone('Enter a valid 10-digit phone number')
```

### `url`

| Param | Type | Default | Description |
|---|---|---|---|
| `message` | `string` | `'Invalid URL'` | Error message |

```ts
url('Please enter a valid URL')
```

### `password`

| Param | Type | Default | Description |
|---|---|---|---|
| `minLength` | `number` | `8` | Minimum length |
| `requireUppercase` | `boolean` | `true` | Require uppercase letter |
| `requireLowercase` | `boolean` | `true` | Require lowercase letter |
| `requireNumber` | `boolean` | `true` | Require digit |
| `requireSpecial` | `boolean` | `true` | Require special character |

```ts
password({ minLength: 10 })
```

### `numeric`

| Param | Type | Default | Description |
|---|---|---|---|
| `allowDecimal` | `boolean` | `false` | Allow decimal values |
| `message` | `string` | — | Custom message |

```ts
numeric({ allowDecimal: true }, 'Enter a valid number')
```

### `custom`

```ts
custom((value, values) => {
  if (value && value.length < 3) return 'Minimum 3 items required';
})
```

### `crossField`

```ts
crossField('confirmPassword', (value, values) => {
  if (value !== values.password) return 'Passwords must match';
})
```

### `async`

```ts
async(async (value) => {
  const taken = await checkUsername(value);
  return taken ? 'Username already taken' : undefined;
}, { debounce: 300 })
```

## Composing Validators

```ts
const validators = [
  required('Email is required'),
  email('Invalid email format'),
  async(async (v) => {
    const exists = await api.checkEmail(v);
    return exists ? 'Email already registered' : undefined;
  }, { debounce: 400 }),
];
```

## Field-Level Validation

```tsx
const { value, error } = useField('email', {
  validateOnChange: true,
  validateOnBlur: true,
  validate: composeValidators(required(), email()),
});
```

## Form-Level Validation

```tsx
const form = useForm({
  initialValues,
  validate: (values) => {
    const errors: Record<string, string> = {};
    if (values.password !== values.confirmPassword) {
      errors.confirmPassword = 'Passwords must match';
    }
    return errors;
  },
});
```

## Async Validation with Debounce

```tsx
const usernameValidator = async (value: string) => {
  await delay(200);
  const taken = usernames.includes(value);
  return taken ? 'Username is taken' : undefined;
};

useField('username', {
  validate: async(usernameValidator, { debounce: 300 }),
});
```

## Cross-Field Validation (Password Confirm)

```tsx
const passwordValidators = [
  required(),
  password({ minLength: 8 }),
];

const confirmValidators = [
  required(),
  crossField('confirmPassword', (value, values) =>
    value !== values.password ? 'Passwords do not match' : undefined
  ),
];
```

## Error Messages Internationalization

```tsx
const messages = {
  required: 'This field is required',
  email: 'Please enter a valid email',
  minLength: (min: number) => `Minimum ${min} characters`,
  // ...
};

const validator = required(messages.required);
```

## Validation Summary

```tsx
<ValidationSummary errors={form.errors} touched={form.touched} />
```

Renders a list of all visible errors at the top of the form with links to the corresponding fields.

## Custom Validator Creation

```ts
import { Validator } from '@sporekart/forms';

export function gstin(message = 'Invalid GSTIN'): Validator {
  return {
    name: 'gstin',
    validate: (value: string) => {
      if (!value) return;
      const pattern = /^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/;
      return pattern.test(value) ? undefined : message;
    },
  };
}
```

Usage:

```tsx
const validators = [required(), gstin('Enter a valid GSTIN')];
```
