# Select Components

## SelectOption Interface

```tsx
interface SelectOption<T = string> {
  value: T;
  label: string;
  disabled?: boolean;
  group?: string;
  meta?: Record<string, any>;
}
```

## Select

Single-select dropdown with keyboard navigation and combobox pattern.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `options` | `SelectOption[]` | — | Available options |
| `value` | `string` | — | Selected value |
| `onChange` | `(value: string) => void` | — | Selection handler |
| `placeholder` | `string` | `'Select...'` | Placeholder text |
| `disabled` | `boolean` | `false` | Disable control |
| `clearable` | `boolean` | `false` | Allow clearing selection |
| `searchable` | `boolean` | `false` | Show filter input |

### Usage

```tsx
<Select
  options={[
    { value: 'in', label: 'India' },
    { value: 'us', label: 'United States' },
    { value: 'uk', label: 'United Kingdom' },
  ]}
  value={country}
  onChange={setCountry}
  placeholder="Select country"
  clearable
/>
```

## MultiSelect

Multi-value selection with chip display and checkbox dropdown.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `options` | `SelectOption[]` | — | Available options |
| `values` | `string[]` | — | Selected values |
| `onChange` | `(values: string[]) => void` | — | Selection handler |
| `maxItems` | `number` | — | Maximum selections |
| `showSelectAll` | `boolean` | `true` | Show select all toggle |

### Usage

```tsx
<MultiSelect
  options={categories}
  values={selectedCategories}
  onChange={setSelectedCategories}
  maxItems={5}
  showSelectAll
/>
```

## SearchableSelect

Select with built-in search filtering.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `options` | `SelectOption[]` | — | Options to filter |
| `value` | `string` | — | Selected value |
| `onChange` | `(value: string) => void` | — | Selection handler |
| `noResultsText` | `string` | `'No results found'` | Empty filter state |

### Usage

```tsx
<SearchableSelect
  options={allStates}
  value={state}
  onChange={setState}
  noResultsText="No matching state found"
/>
```

## AsyncSelect

Select that loads options asynchronously with debounced search.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `loadOptions` | `(input: string) => Promise<SelectOption[]>` | — | Async loader |
| `value` | `string` | — | Selected value |
| `onChange` | `(value: string) => void` | — | Selection handler |
| `debounce` | `number` | `300` | Debounce delay in ms |
| `minChars` | `number` | `2` | Min chars before search |
| `loadingText` | `string` | `'Loading...'` | Loading message |
| `errorText` | `string` | `'Failed to load options'` | Error message |
| `onRetry` | `() => void` | — | Retry handler |

### Usage

```tsx
<AsyncSelect
  loadOptions={async (input) => {
    const res = await api.searchProducts(input);
    return res.map((p) => ({ value: p.id, label: p.name }));
  }}
  value={productId}
  onChange={setProductId}
  debounce={400}
  minChars={3}
/>
```

## Keyboard Navigation Guide

| Key | Action |
|---|---|
| `Tab` | Enter/leave combobox |
| `ArrowDown` | Move to next option |
| `ArrowUp` | Move to previous option |
| `Enter` | Select highlighted option |
| `Escape` | Close dropdown |
| `Home` | First option |
| `End` | Last option |
| `Backspace` | (MultiSelect) Remove last chip |

## Accessibility

- All selects use `role="combobox"` with `aria-expanded`
- Dropdown list uses `role="listbox"` with `aria-multiselectable` for MultiSelect
- Each option has `role="option"` with `aria-selected`
- `aria-activedescendant` tracks keyboard focus
- Selected options announced via live region
- MultiSelect chips have remove buttons with `aria-label`
- AsyncSelect announces loading and error states to screen readers
- Focus trapped within dropdown while open
