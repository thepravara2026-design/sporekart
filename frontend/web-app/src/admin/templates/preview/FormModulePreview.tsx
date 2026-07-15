import { memo } from 'react';
import { ExampleForm } from '../FormTemplate';

export const FormModulePreview = memo(function FormModulePreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
      <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Form Module</h1>
      <ExampleForm />
    </div>
  );
});
