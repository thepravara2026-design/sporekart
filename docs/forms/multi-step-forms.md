# Multi-Step Forms

## MultiStepForm

Orchestrates multi-step form workflows with step navigation, validation gating, and progress tracking.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `steps` | `StepConfig[]` | — | Step definitions |
| `initialStep` | `number` | `0` | Starting step index |
| `onComplete` | `(values: Record<string, any>) => Promise<void>` | — | Final submission |
| `validateOnStep` | `boolean` | `true` | Validate before advancing |

### StepConfig Interface

```tsx
interface StepConfig {
  id: string;
  title: string;
  description?: string;
  icon?: ReactNode;
  validate?: (values: Record<string, any>) => Record<string, string>;
  fields?: string[]; // Field names in this step
}
```

### Usage

```tsx
<MultiStepForm
  steps={[
    { id: 'personal', title: 'Personal Info', fields: ['name', 'email'] },
    { id: 'address', title: 'Address', fields: ['city', 'state'] },
    { id: 'review', title: 'Review', fields: [] },
  ]}
  onComplete={async (values) => submitApplication(values)}
>
  {({ currentStep, goNext, goBack, isFirst, isLast }) => (
    <>
      <StepPanel step={0}>
        <FormField name="name" label="Full Name" />
        <FormField name="email" label="Email" />
      </StepPanel>
      <StepPanel step={1}>
        <AddressFields />
      </StepPanel>
      <StepPanel step={2}>
        <ReviewSummary values={form.values} />
      </StepPanel>
      <FormActions
        submitText={isLast ? 'Submit' : 'Next'}
        onCancel={isFirst ? undefined : goBack}
      />
    </>
  )}
</MultiStepForm>
```

## StepIndicator

Visual progress indicator showing all steps with completion state.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `steps` | `StepConfig[]` | — | Step definitions |
| `currentStep` | `number` | — | Active step index |
| `orientation` | `'horizontal' \| 'vertical'` | `'horizontal'` | Layout direction |
| `showLabels` | `boolean` | `true` | Show step labels |
| `showProgressBar` | `boolean` | `false` | Show progress bar |

### Usage

```tsx
<StepIndicator
  steps={steps}
  currentStep={currentStep}
  orientation="horizontal"
  showProgressBar
/>
```

### Visual States

| State | Step Circle | Label |
|---|---|---|
| Completed | Filled (primary) | Strikethrough optional |
| Active | Outlined (primary) | Bold |
| Pending | Outlined (neutral) | Normal |
| Error | Outlined (danger) | With error icon |

## StepPanel

Conditionally renders content for the current step.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `step` | `number` | — | Step index this panel belongs to |
| `children` | `ReactNode` | — | Content |

### Usage

```tsx
<StepPanel step={0}>
  <PersonalInfoFields />
</StepPanel>
<StepPanel step={1}>
  <AddressFields />
</StepPanel>
```

## Validation Before Navigation

Before advancing to the next step, `MultiStepForm` validates fields associated with the current step. If validation fails, the step indicator shows an error state and the first invalid field receives focus.

```tsx
// Automatic: steps[0].validate is called on "Next"
// Errors prevent navigation
```

## Progress Tracking

```tsx
const progress = ((currentStep + 1) / steps.length) * 100;

<StepIndicator showProgressBar currentStep={currentStep} steps={steps} />
// Renders: "Step 2 of 3 — 66% complete"
```

## State Persistence Foundation

The `MultiStepForm` stores all values centrally and exposes them for persistence:

```tsx
// Auto-save hook
useEffect(() => {
  const timer = setTimeout(() => {
    localStorage.setItem('formDraft', JSON.stringify(form.values));
  }, 2000);
  return () => clearTimeout(timer);
}, [form.values]);
```

```tsx
// Resume from draft
const draft = JSON.parse(localStorage.getItem('formDraft') || '{}');

<MultiStepForm
  initialValues={draft}
  steps={steps}
  onComplete={async (values) => {
    localStorage.removeItem('formDraft');
    await submit(values);
  }}
>
```

## Example

```tsx
function CheckoutForm() {
  const steps: StepConfig[] = [
    { id: 'cart', title: 'Review Cart', icon: <CartIcon /> },
    { id: 'shipping', title: 'Shipping', icon: <TruckIcon /> },
    { id: 'payment', title: 'Payment', icon: <CardIcon /> },
    { id: 'confirm', title: 'Confirm', icon: <CheckIcon /> },
  ];

  return (
    <MultiStepForm steps={steps} onComplete={placeOrder}>
      {({ currentStep, goNext, goBack, isFirst, isLast }) => (
        <>
          <StepIndicator
            steps={steps}
            currentStep={currentStep}
            showProgressBar
          />
          <StepPanel step={0}><CartReview /></StepPanel>
          <StepPanel step={1}>
            <AddressFields prefix="shipping" />
          </StepPanel>
          <StepPanel step={2}><PaymentForm /></StepPanel>
          <StepPanel step={3}><OrderSummary /></StepPanel>
          <FormActions
            submitText={isLast ? 'Place Order' : 'Continue'}
            cancelText={isFirst ? undefined : 'Back'}
            onCancel={goBack}
            onClick={isLast ? undefined : goNext}
          />
        </>
      )}
    </MultiStepForm>
  );
}
```

## Accessibility

- StepIndicator uses `role="list"` with `aria-label="Form progress"`
- Each step is `role="listitem"` with `aria-current="step"` on active step
- StepPanel uses `role="tabpanel"` with `aria-labelledby`
- Navigation buttons announce current step number
- Focus moves to first field or step indicator on navigation
- Error state communicated via `aria-live="polite"`
