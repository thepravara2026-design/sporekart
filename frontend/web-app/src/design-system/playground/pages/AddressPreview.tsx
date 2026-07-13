import { useState } from 'react';

interface AddressFields {
  fullName: string;
  street: string;
  street2: string;
  city: string;
  state: string;
  zip: string;
  country: string;
  phone: string;
}

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap)',
  padding: 'var(--space-page-y) var(--space-page-x)',
};

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  padding: '24px',
  boxShadow: 'var(--shadow-1)',
  display: 'flex',
  flexDirection: 'column',
  gap: '16px',
};

const inputBase: React.CSSProperties = {
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-md)',
  padding: '8px 12px',
  fontSize: 'var(--text-base)',
  background: 'var(--color-bg-input)',
  color: 'var(--color-text-primary)',
  outline: 'none',
  width: '100%',
  boxSizing: 'border-box',
};

const btnStyle: React.CSSProperties = {
  background: 'var(--color-bg-primary-default)',
  color: '#fff',
  border: 'none',
  borderRadius: 'var(--radius-md)',
  padding: '8px 16px',
  fontSize: 'var(--text-base)',
  fontWeight: 500,
  cursor: 'pointer',
};

const labelStyle: React.CSSProperties = {
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-medium)',
  color: 'var(--color-text-primary)',
};

const rowStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr',
  gap: '12px',
};

const jsonStyle: React.CSSProperties = {
  background: 'var(--color-bg-code, #1e1e2e)',
  color: 'var(--color-text-code, #cdd6f4)',
  borderRadius: 'var(--radius-md)',
  padding: '12px 16px',
  fontSize: 'var(--text-caption)',
  fontFamily: 'var(--font-mono)',
  whiteSpace: 'pre-wrap',
  overflow: 'auto',
  maxHeight: '200px',
};

const initialState: AddressFields = {
  fullName: '',
  street: '',
  street2: '',
  city: '',
  state: '',
  zip: '',
  country: 'US',
  phone: '',
};

const filledState: AddressFields = {
  fullName: 'Jane Doe',
  street: '123 Main Street',
  street2: 'Apt 4B',
  city: 'San Francisco',
  state: 'CA',
  zip: '94105',
  country: 'US',
  phone: '+1 415 555 0123',
};

function FormField({ label, children, error }: { label: string; children: React.ReactNode; error?: string }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
      <span style={labelStyle}>{label}</span>
      {children}
      {error && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger)' }}>{error}</span>}
    </div>
  );
}

export default function AddressPreview() {
  const [compact, setCompact] = useState(false);
  const [values, setValues] = useState<AddressFields>({ ...initialState });
  const [errors, setErrors] = useState<Partial<AddressFields>>({});
  const [submitted, setSubmitted] = useState(false);

  const update = (field: keyof AddressFields, value: string) => {
    setValues((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) setErrors((prev) => { const n = { ...prev }; delete n[field]; return n; });
  };

  const validate = (): boolean => {
    const errs: Partial<AddressFields> = {};
    if (!values.fullName.trim()) errs.fullName = 'Full name is required';
    if (!values.street.trim()) errs.street = 'Street address is required';
    if (!values.city.trim()) errs.city = 'City is required';
    if (!values.state.trim()) errs.state = 'State is required';
    if (!values.zip.trim()) errs.zip = 'ZIP code is required';
    else if (!/^\d{5}(-\d{4})?$/.test(values.zip.trim())) errs.zip = 'Invalid ZIP code';
    if (!values.phone.trim()) errs.phone = 'Phone is required';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = () => {
    if (validate()) setSubmitted(true);
    else setSubmitted(false);
  };

  const handleReset = () => {
    setValues({ ...initialState });
    setErrors({});
    setSubmitted(false);
  };

  const handleFill = () => {
    setValues({ ...filledState });
    setErrors({});
    setSubmitted(false);
  };

  const gridStyle: React.CSSProperties = compact
    ? { display: 'flex', flexDirection: 'column', gap: '12px' }
    : { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' };

  return (
    <div style={sectionStyle}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Address Form</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Address form with all fields</p>
      </div>

      <div style={cardStyle}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Shipping Address</h2>
          <label style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: 'var(--text-body)', cursor: 'pointer' }}>
            <input type="checkbox" checked={compact} onChange={() => setCompact((v) => !v)} />
            Compact Mode
          </label>
        </div>

        <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
          <button style={btnStyle} onClick={handleFill}>Fill Example</button>
          <button style={{ ...btnStyle, background: 'transparent', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)' }} onClick={handleReset}>Reset</button>
        </div>

        <div style={gridStyle}>
          <FormField label="Full Name" error={errors.fullName}><input style={{ ...inputBase, borderColor: errors.fullName ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.fullName} onChange={(e) => update('fullName', e.target.value)} placeholder="Jane Doe" /></FormField>
          <FormField label="Phone" error={errors.phone}><input style={{ ...inputBase, borderColor: errors.phone ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.phone} onChange={(e) => update('phone', e.target.value)} placeholder="+1 415 555 0123" /></FormField>
        </div>
        <FormField label="Street Address" error={errors.street}><input style={{ ...inputBase, borderColor: errors.street ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.street} onChange={(e) => update('street', e.target.value)} placeholder="123 Main Street" /></FormField>
        <FormField label="Apt / Suite / Unit (Optional)">
          <input style={inputBase} value={values.street2} onChange={(e) => update('street2', e.target.value)} placeholder="Apt 4B" />
        </FormField>
        {compact ? (
          <>
            <FormField label="City" error={errors.city}><input style={{ ...inputBase, borderColor: errors.city ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.city} onChange={(e) => update('city', e.target.value)} placeholder="San Francisco" /></FormField>
            <FormField label="State" error={errors.state}><input style={{ ...inputBase, borderColor: errors.state ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.state} onChange={(e) => update('state', e.target.value)} placeholder="CA" /></FormField>
            <FormField label="ZIP Code" error={errors.zip}><input style={{ ...inputBase, borderColor: errors.zip ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.zip} onChange={(e) => update('zip', e.target.value)} placeholder="94105" /></FormField>
          </>
        ) : (
          <div style={rowStyle}>
            <FormField label="City" error={errors.city}><input style={{ ...inputBase, borderColor: errors.city ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.city} onChange={(e) => update('city', e.target.value)} placeholder="San Francisco" /></FormField>
            <div style={{ display: 'flex', gap: '12px' }}>
              <div style={{ flex: 1 }}>
                <FormField label="State" error={errors.state}><input style={{ ...inputBase, borderColor: errors.state ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.state} onChange={(e) => update('state', e.target.value)} placeholder="CA" /></FormField>
              </div>
              <div style={{ flex: 1 }}>
                <FormField label="ZIP Code" error={errors.zip}><input style={{ ...inputBase, borderColor: errors.zip ? 'var(--color-border-danger)' : 'var(--color-border-default)' }} value={values.zip} onChange={(e) => update('zip', e.target.value)} placeholder="94105" /></FormField>
              </div>
            </div>
          </div>
        )}
        <FormField label="Country">
          <select style={{ ...inputBase, appearance: 'none' }} value={values.country} onChange={(e) => update('country', e.target.value)}>
            <option value="US">United States</option>
            <option value="CA">Canada</option>
            <option value="UK">United Kingdom</option>
            <option value="IN">India</option>
            <option value="AU">Australia</option>
          </select>
        </FormField>

        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
          <button style={{ ...btnStyle, background: 'transparent', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)' }} onClick={handleReset}>Cancel</button>
          <button style={btnStyle} onClick={handleSubmit}>{submitted ? 'Saved!' : 'Save Address'}</button>
        </div>

        {submitted && (
          <div style={{ padding: '12px', background: 'var(--color-bg-success-subtle, #f0fdf4)', border: '1px solid var(--color-border-success, #16a34a)', borderRadius: 'var(--radius-md)', color: 'var(--color-text-success, #16a34a)', fontSize: 'var(--text-body)' }}>
            Address saved successfully!
          </div>
        )}
      </div>

      <div style={cardStyle}>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Current Values (JSON)</h2>
        <div style={jsonStyle}>{JSON.stringify(values, null, 2)}</div>
      </div>
    </div>
  );
}
