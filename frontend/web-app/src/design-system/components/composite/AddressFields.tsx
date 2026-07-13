import React, { useState, useCallback, useRef } from 'react';
import { FormRow } from '../forms/FormRow';
import { FormField } from '../forms/FormField';
import { Input } from '../core/Input';
import { RadioGroup } from '../core/RadioGroup';
import type { RadioOption } from '../core/RadioGroup';
import { ToggleSwitch } from '../core/ToggleSwitch';
import type { AddressValues } from './AddressForm';

export interface AddressFieldsProps {
  value?: Partial<AddressValues>;
  onChange?: (field: string, value: any) => void;
  disabled?: boolean;
  readOnly?: boolean;
  errors?: Record<string, string | undefined>;
  includeFields?: (keyof AddressValues)[];
  className?: string;
}

const ADDRESS_TYPE_OPTIONS: RadioOption[] = [
  { value: 'home', label: 'Home' },
  { value: 'work', label: 'Work' },
  { value: 'other', label: 'Other' },
];

const DEFAULT_INCLUDE: (keyof AddressValues)[] = [
  'fullName', 'phone', 'addressLine1', 'city', 'state', 'country', 'pinCode',
];

export const AddressFields: React.FC<AddressFieldsProps> = ({
  value = {},
  onChange,
  disabled: _disabled,
  readOnly: _readOnly,
  errors,
  includeFields = DEFAULT_INCLUDE,
  className = '',
}) => {
  const generatedId = useRef(`sk-af-${Math.random().toString(36).substr(2, 9)}`);
  const [localValues, setLocalValues] = useState<Partial<AddressValues>>(value);

  const currentValues = { ...localValues, ...value };

  const handleChange = useCallback(
    (field: string, fieldValue: any) => {
      setLocalValues((prev) => ({ ...prev, [field]: fieldValue }));
      onChange?.(field, fieldValue);
    },
    [onChange]
  );

  const shouldRender = (field: keyof AddressValues): boolean => {
    return includeFields.includes(field);
  };

  const wrapperStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-component-gap)',
    width: '100%',
  };

  const renderInput = (
    field: keyof AddressValues,
    label: string,
    placeholder: string,
    type: 'text' | 'tel' | 'email' = 'text',
    required = false
  ) => {
    if (!shouldRender(field)) return null;
    return (
      <FormField
        key={field}
        name={field}
        label={label}
        error={errors?.[field]}
        required={required}
      >
        <Input
          name={field}
          type={type}
          value={(currentValues as any)[field] || ''}
          onChange={(e) => handleChange(field, e.target.value)}
          placeholder={placeholder}
          fullWidth
        />
      </FormField>
    );
  };

  return (
    <div
      className={`sk-address-fields ${className}`}
      style={wrapperStyle}
    >
      {(shouldRender('fullName') || shouldRender('phone')) && (
        <FormRow columns={2}>
          {renderInput('fullName', 'Full Name', 'Enter full name', 'text', true)}
          {renderInput('phone', 'Phone', '10-digit mobile number', 'tel', true)}
        </FormRow>
      )}

      {shouldRender('email') && renderInput('email', 'Email', 'email@example.com', 'email')}

      {shouldRender('addressLine1') && renderInput('addressLine1', 'Address Line 1', 'House number, street, area', 'text', true)}
      {shouldRender('addressLine2') && renderInput('addressLine2', 'Address Line 2', 'Apartment, suite, unit, etc.')}
      {shouldRender('landmark') && renderInput('landmark', 'Landmark', 'Nearby landmark (optional)')}

      {(shouldRender('city') || shouldRender('district')) && (
        <FormRow columns={2}>
          {renderInput('city', 'City', 'City', 'text', true)}
          {renderInput('district', 'District', 'District')}
        </FormRow>
      )}

      {(shouldRender('state') || shouldRender('country')) && (
        <FormRow columns={2}>
          {renderInput('state', 'State', 'State', 'text', true)}
          {renderInput('country', 'Country', 'Country', 'text', true)}
        </FormRow>
      )}

      {shouldRender('pinCode') && renderInput('pinCode', 'PIN Code', '6-digit PIN code', 'text', true)}

      {shouldRender('addressType') && (
        <FormField name="addressType" label="Address Type">
          <RadioGroup
            name={`${generatedId.current}-addressType`}
            options={ADDRESS_TYPE_OPTIONS}
            value={(currentValues as any).addressType || 'home'}
            onChange={(val) => handleChange('addressType', val)}
            orientation="horizontal"
            size="sm"
          />
        </FormField>
      )}

      {shouldRender('isDefault') && (
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-md)',
          }}
        >
          <ToggleSwitch
            checked={!!(currentValues as any).isDefault}
            onChange={(val) => handleChange('isDefault', val)}
            size="sm"
          />
          <span
            style={{
              fontFamily: 'var(--font-family-sans)',
              fontSize: 'var(--text-body)',
              color: 'var(--color-text-primary)',
            }}
          >
            Set as default address
          </span>
        </div>
      )}
    </div>
  );
};

AddressFields.displayName = 'AddressFields';

export default AddressFields;
