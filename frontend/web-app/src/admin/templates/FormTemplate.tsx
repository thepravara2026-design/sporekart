import { memo, useState, type FormEvent } from 'react';
import { FormLayout, FormSection, FormField, FormActions } from '../../design-system/components/forms';
import Textarea from '../components/forms/Textarea';
import NumberInput from '../components/forms/NumberInput';
import EmailInput from '../components/forms/EmailInput';

export const ExampleForm = memo(function ExampleForm() {
  const [notes, setNotes] = useState('');
  const [quantity, setQuantity] = useState<number | undefined>(undefined);
  const [email, setEmail] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    setTimeout(() => {
      setSubmitting(false);
      setError('Demo only: submission is not persisted.');
    }, 600);
  };

  const handleCancel = () => {
    setNotes('');
    setQuantity(undefined);
    setEmail('');
    setError('');
  };

  return (
    <FormLayout onSubmit={handleSubmit} aria-label="Example form">
      <FormSection title="Record Details">
        <FormField label="Name / Description" required error={error || undefined}>
          <Textarea value={notes} onChange={setNotes} placeholder="Enter details" />
        </FormField>
        <FormField label="Quantity" required>
          <NumberInput value={quantity} onChange={setQuantity} placeholder="0" min={0} />
        </FormField>
        <FormField label="Contact Email" required error={error || undefined}>
          <EmailInput value={email} onChange={setEmail} placeholder="name@example.com" />
        </FormField>
      </FormSection>
      <FormActions
        submitLabel="Submit"
        cancelLabel="Cancel"
        onCancel={handleCancel}
        submitting={submitting}
      />
    </FormLayout>
  );
});
