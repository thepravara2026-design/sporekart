import React from 'react';
import { FormField } from '../../../../design-system/components/forms';
import type { ProductWizardData, WizardErrors } from './types';
import { hasValue } from './validation';

export interface ValidatedFieldProps {
  name: keyof ProductWizardData;
  label: string;
  data: ProductWizardData;
  errors: WizardErrors;
  required?: boolean;
  description?: string;
  helperText?: string;
  warning?: string;
  characterLimit?: number;
  children: React.ReactNode;
}

const ValidatedField: React.FC<ValidatedFieldProps> = ({
  name,
  label,
  data,
  errors,
  required,
  description,
  helperText,
  warning,
  characterLimit,
  children,
}) => {
  const value = data[name];
  const error = errors[name];
  const success = !error && !warning && hasValue(value);
  const currentLength = characterLimit && typeof value === 'string' ? value.length : undefined;

  return (
    <FormField
      label={label}
      required={required}
      error={error}
      success={success}
      warning={warning}
      description={description}
      helperText={helperText}
      characterLimit={characterLimit}
      currentLength={currentLength}
    >
      {children}
    </FormField>
  );
};

export default ValidatedField;
