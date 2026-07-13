import React, { useState, useCallback, useRef, useEffect } from 'react';
import { FormRow } from '../forms/FormRow';
import { FormField } from '../forms/FormField';
import { Input } from '../core/Input';
import { RadioGroup } from '../core/RadioGroup';
import type { RadioOption } from '../core/RadioGroup';
import { ToggleSwitch } from '../core/ToggleSwitch';

export interface AddressValues {
  fullName: string;
  phone: string;
  email: string;
  addressLine1: string;
  addressLine2: string;
  landmark: string;
  city: string;
  district: string;
  state: string;
  country: string;
  pinCode: string;
  addressType: 'home' | 'work' | 'other';
  isDefault: boolean;
}

export interface AddressFormProps {
  value?: AddressValues;
  onChange?: (values: AddressValues) => void;
  onValidation?: (isValid: boolean) => void;
  disabled?: boolean;
  readOnly?: boolean;
  errors?: Record<string, string | undefined>;
  showTitle?: boolean;
  compact?: boolean;
  className?: string;
  id?: string;
}

const DEFAULT_VALUES: AddressValues = {
  fullName: '',
  phone: '',
  email: '',
  addressLine1: '',
  addressLine2: '',
  landmark: '',
  city: '',
  district: '',
  state: '',
  country: '',
  pinCode: '',
  addressType: 'home',
  isDefault: false,
};

const ADDRESS_TYPE_OPTIONS: RadioOption[] = [
  { value: 'home', label: 'Home' },
  { value: 'work', label: 'Work' },
  { value: 'other', label: 'Other' },
];

function validatePhone(phone: string): boolean {
  return /^[6-9]\d{9}$/.test(phone.replace(/\s/g, ''));
}

function validatePinCode(pin: string): boolean {
  return /^\d{6}$/.test(pin);
}

function validateEmail(email: string): boolean {
  if (!email) return true;
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function getLocalErrors(values: AddressValues): Record<string, string | undefined> {
  const errs: Record<string, string | undefined> = {};
  if (!values.fullName.trim()) errs.fullName = 'Full name is required';
  if (!values.phone.trim()) errs.phone = 'Phone number is required';
  else if (!validatePhone(values.phone)) errs.phone = 'Enter a valid 10-digit Indian phone number';
  if (values.email && !validateEmail(values.email)) errs.email = 'Enter a valid email address';
  if (!values.addressLine1.trim()) errs.addressLine1 = 'Address line 1 is required';
  if (!values.city.trim()) errs.city = 'City is required';
  if (!values.state.trim()) errs.state = 'State is required';
  if (!values.country.trim()) errs.country = 'Country is required';
  if (!values.pinCode.trim()) errs.pinCode = 'PIN code is required';
  else if (!validatePinCode(values.pinCode)) errs.pinCode = 'Enter a valid 6-digit PIN code';
  return errs;
}

export const AddressForm: React.FC<AddressFormProps> = ({
  value,
  onChange,
  onValidation,
  disabled: _disabled,
  readOnly: _readOnly,
  errors: externalErrors,
  showTitle = true,
  compact = false,
  className = '',
  id,
}) => {
  const generatedId = useRef(`sk-addr-${Math.random().toString(36).substr(2, 9)}`);
  const formId = id || generatedId.current;
  const isControlled = value !== undefined;
  const [internalValues, setInternalValues] = useState<AddressValues>(value || DEFAULT_VALUES);
  const [internalErrors, setInternalErrors] = useState<Record<string, string | undefined>>({});

  const currentValues = isControlled ? (value || DEFAULT_VALUES) : internalValues;
  const allErrors = { ...internalErrors, ...externalErrors };

  const hasError = Object.values(allErrors).some(Boolean);

  useEffect(() => {
    onValidation?.(!hasError);
  }, [hasError, onValidation]);

  const handleFieldChange = useCallback(
    (field: keyof AddressValues, fieldValue: any) => {
      const newValues = { ...currentValues, [field]: fieldValue };
      if (!isControlled) {
        setInternalValues(newValues);
      }
      onChange?.(newValues);
      const localErrors = getLocalErrors(newValues);
      setInternalErrors(localErrors);
    },
    [currentValues, isControlled, onChange]
  );

  const formStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-component-gap)',
    width: '100%',
  };

  const titleStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-h6)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    lineHeight: 'var(--leading-normal)',
    margin: 0,
  };

  const dividerStyle: React.CSSProperties = {
    border: 'none',
    borderTop: 'var(--border-width-thin) solid var(--color-border-default)',
    margin: 0,
  };

  const toggleRowStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-md)',
  };

  return (
    <div
      id={formId}
      className={`sk-address-form ${compact ? 'sk-address-form--compact' : ''} ${className}`}
      style={formStyle}
    >
      {showTitle && (
        <h3 style={titleStyle}>Address Details</h3>
      )}

      <FormRow columns={2}>
        <FormField
          name="fullName"
          label="Full Name"
          error={allErrors.fullName}
          required
        >
          <Input
            name="fullName"
            value={currentValues.fullName}
            onChange={(e) => handleFieldChange('fullName', e.target.value)}
            placeholder="Enter full name"
            fullWidth
          />
        </FormField>

        <FormField
          name="phone"
          label="Phone"
          error={allErrors.phone}
          required
        >
          <Input
            name="phone"
            type="tel"
            value={currentValues.phone}
            onChange={(e) => handleFieldChange('phone', e.target.value)}
            placeholder="10-digit mobile number"
            fullWidth
          />
        </FormField>
      </FormRow>

      {!compact && (
        <FormRow columns={2}>
          <FormField
            name="email"
            label="Email"
            error={allErrors.email}
          >
            <Input
              name="email"
              type="email"
              value={currentValues.email}
              onChange={(e) => handleFieldChange('email', e.target.value)}
              placeholder="email@example.com"
              fullWidth
            />
          </FormField>

          <FormField
            name="addressLine2"
            label="Address Line 2"
          >
            <Input
              name="addressLine2"
              value={currentValues.addressLine2}
              onChange={(e) => handleFieldChange('addressLine2', e.target.value)}
              placeholder="Apartment, suite, unit, etc."
              fullWidth
            />
          </FormField>
        </FormRow>
      )}

      <FormField
        name="addressLine1"
        label="Address Line 1"
        error={allErrors.addressLine1}
        required
      >
        <Input
          name="addressLine1"
          value={currentValues.addressLine1}
          onChange={(e) => handleFieldChange('addressLine1', e.target.value)}
          placeholder="House number, street, area"
          fullWidth
        />
      </FormField>

      {!compact && (
        <FormField
          name="landmark"
          label="Landmark"
        >
          <Input
            name="landmark"
            value={currentValues.landmark}
            onChange={(e) => handleFieldChange('landmark', e.target.value)}
            placeholder="Nearby landmark (optional)"
            fullWidth
          />
        </FormField>
      )}

      <FormRow columns={2}>
        <FormField
          name="city"
          label="City"
          error={allErrors.city}
          required
        >
          <Input
            name="city"
            value={currentValues.city}
            onChange={(e) => handleFieldChange('city', e.target.value)}
            placeholder="City"
            fullWidth
          />
        </FormField>

        <FormField
          name="district"
          label="District"
        >
          <Input
            name="district"
            value={currentValues.district}
            onChange={(e) => handleFieldChange('district', e.target.value)}
            placeholder="District"
            fullWidth
          />
        </FormField>
      </FormRow>

      <FormRow columns={2}>
        <FormField
          name="state"
          label="State"
          error={allErrors.state}
          required
        >
          <Input
            name="state"
            value={currentValues.state}
            onChange={(e) => handleFieldChange('state', e.target.value)}
            placeholder="State"
            fullWidth
          />
        </FormField>

        <FormField
          name="country"
          label="Country"
          error={allErrors.country}
          required
        >
          <Input
            name="country"
            value={currentValues.country}
            onChange={(e) => handleFieldChange('country', e.target.value)}
            placeholder="Country"
            fullWidth
          />
        </FormField>
      </FormRow>

      <FormRow columns={compact ? 1 : 2}>
        <FormField
          name="pinCode"
          label="PIN Code"
          error={allErrors.pinCode}
          required
        >
          <Input
            name="pinCode"
            value={currentValues.pinCode}
            onChange={(e) => {
              const val = e.target.value.replace(/\D/g, '').slice(0, 6);
              handleFieldChange('pinCode', val);
            }}
            placeholder="6-digit PIN code"
            fullWidth
          />
        </FormField>

        {!compact && (
          <FormField name="addressType" label="Address Type">
            <RadioGroup
              name={`${formId}-addressType`}
              options={ADDRESS_TYPE_OPTIONS}
              value={currentValues.addressType}
              onChange={(val) => handleFieldChange('addressType', val)}
              orientation="horizontal"
              size="sm"
            />
          </FormField>
        )}
      </FormRow>

      <hr style={dividerStyle} />

      <div style={toggleRowStyle}>
        <ToggleSwitch
          checked={currentValues.isDefault}
          onChange={(val) => handleFieldChange('isDefault', val)}
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
    </div>
  );
};

AddressForm.displayName = 'AddressForm';

export default AddressForm;
