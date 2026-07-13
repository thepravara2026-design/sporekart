import { useState, useCallback } from 'react';

interface FieldState {
  value: string;
  error: string;
  touched: boolean;
}

interface ValidationSection {
  id: string;
  label: string;
  fields: Record<string, FieldState>;
}

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  padding: '24px',
  boxShadow: 'var(--shadow-1)',
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

function errorInputStyle(hasError: boolean): React.CSSProperties {
  return {
    ...inputBase,
    borderColor: hasError ? 'var(--color-border-danger)' : 'var(--color-border-default)',
  };
}

function Input({ value, onChange, placeholder, error, type }: { value: string; onChange: (v: string) => void; placeholder: string; error?: string; type?: string }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
      <input
        style={errorInputStyle(!!error)}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        type={type || 'text'}
      />
      {error && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger)' }}>{error}</span>}
    </div>
  );
}

function FieldRow({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
      <span style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)' }}>{label}</span>
      {children}
    </div>
  );
}

const strengthColors: Record<string, string> = {
  weak: 'var(--color-bg-danger-default)',
  medium: 'var(--color-bg-warning-default)',
  strong: 'var(--color-bg-success-default)',
};

function getPasswordStrength(pw: string): { label: string; score: number; color: string } {
  let score = 0;
  if (pw.length >= 6) score++;
  if (pw.length >= 10) score++;
  if (/[A-Z]/.test(pw)) score++;
  if (/[a-z]/.test(pw)) score++;
  if (/[0-9]/.test(pw)) score++;
  if (/[^A-Za-z0-9]/.test(pw)) score++;
  const map = ['', 'weak', 'weak', 'medium', 'medium', 'strong', 'strong'];
  return { label: map[score], score: score / 6, color: strengthColors[map[score]] || 'var(--color-bg-danger-default)' };
}

function simulateAsyncValidation(value: string): Promise<string> {
  return new Promise((resolve) => {
    setTimeout(() => {
      if (value === 'taken@example.com') resolve('This email is already registered');
      else resolve('');
    }, 1200);
  });
}

export default function FormValidationPreview() {
  const [sections, setSections] = useState<Record<string, ValidationSection>>({
    required: {
      id: 'required', label: 'Required Field',
      fields: { name: { value: '', error: '', touched: false } },
    },
    email: {
      id: 'email', label: 'Email Validation',
      fields: { email: { value: '', error: '', touched: false } },
    },
    phone: {
      id: 'phone', label: 'Phone Validation',
      fields: { phone: { value: '', error: '', touched: false } },
    },
    password: {
      id: 'password', label: 'Password Strength',
      fields: { password: { value: '', error: '', touched: false } },
    },
    length: {
      id: 'length', label: 'Min / Max Length',
      fields: { username: { value: '', error: '', touched: false } },
    },
    pattern: {
      id: 'pattern', label: 'Pattern (PIN)',
      fields: { pin: { value: '', error: '', touched: false } },
    },
    cross: {
      id: 'cross', label: 'Cross-field (Confirm Password)',
      fields: { pwd: { value: '', error: '', touched: false }, confirm: { value: '', error: '', touched: false } },
    },
    async: {
      id: 'async', label: 'Async Validation',
      fields: { email: { value: '', error: '', touched: false } },
    },
  });
  const [asyncLoading, setAsyncLoading] = useState(false);
  const [summary, setSummary] = useState<string[]>([]);

  const updateField = useCallback((sectionId: string, fieldName: string, value: string) => {
    setSections((prev) => {
      const section = prev[sectionId];
      if (!section) return prev;
      let error = '';
      switch (sectionId) {
        case 'required': {
          if (!value.trim()) error = 'This field is required';
          break;
        }
        case 'email': {
          if (!value.trim()) error = 'Email is required';
          else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) error = 'Invalid email format';
          break;
        }
        case 'phone': {
          if (!value.trim()) error = 'Phone is required';
          else if (!/^[\d\s+\-()]{7,15}$/.test(value)) error = 'Invalid phone number';
          break;
        }
        case 'password': {
          if (!value.trim()) error = 'Password is required';
          else if (value.length < 6) error = 'Password must be at least 6 characters';
          break;
        }
        case 'length': {
          if (!value.trim()) error = 'Username is required';
          else if (value.length < 3) error = 'Min 3 characters';
          else if (value.length > 20) error = 'Max 20 characters';
          break;
        }
        case 'pattern': {
          if (!value.trim()) error = 'PIN is required';
          else if (!/^\d{6}$/.test(value)) error = 'PIN must be exactly 6 digits';
          break;
        }
        case 'cross': {
          if (fieldName === 'pwd') {
            if (!value.trim()) error = 'Password is required';
            const confirmValue = prev.cross.fields.confirm.value;
            if (confirmValue && value !== confirmValue) {
              setSections((p) => {
                const cf = { ...p.cross.fields.confirm, error: 'Passwords do not match' };
                return { ...p, cross: { ...p.cross, fields: { ...p.cross.fields, confirm: cf } } };
              });
            }
          } else {
            if (!value.trim()) error = 'Please confirm your password';
            else if (value !== prev.cross.fields.pwd.value) error = 'Passwords do not match';
          }
          break;
        }
        default: break;
      }
      return {
        ...prev,
        [sectionId]: {
          ...section,
          fields: { ...section.fields, [fieldName]: { value, error, touched: true } },
        },
      };
    });
  }, []);

  const handleSubmit = () => {
    const errors: string[] = [];
    const updated = { ...sections };
    for (const [sid, section] of Object.entries(updated)) {
      const newFields = { ...section.fields };
      for (const [fname, field] of Object.entries(newFields)) {
        let error = '';
        if (!field.value.trim()) {
          if (sid === 'required') error = 'This field is required';
          else if (sid === 'email') error = 'Email is required';
          else if (sid === 'phone') error = 'Phone is required';
          else if (sid === 'password') error = 'Password is required';
          else if (sid === 'length') error = 'Username is required';
          else if (sid === 'pattern') error = 'PIN is required';
          else if (sid === 'cross') error = fname === 'pwd' ? 'Password is required' : 'Please confirm your password';
        } else {
          if (sid === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(field.value)) error = 'Invalid email format';
          if (sid === 'phone' && !/^[\d\s+\-()]{7,15}$/.test(field.value)) error = 'Invalid phone number';
          if (sid === 'password' && field.value.length < 6) error = 'Password must be at least 6 characters';
          if (sid === 'length') {
            if (field.value.length < 3) error = 'Min 3 characters';
            else if (field.value.length > 20) error = 'Max 20 characters';
          }
          if (sid === 'pattern' && !/^\d{6}$/.test(field.value)) error = 'PIN must be exactly 6 digits';
          if (sid === 'cross') {
            if (fname === 'confirm' && field.value !== newFields.pwd.value) error = 'Passwords do not match';
            if (fname === 'pwd' && newFields.confirm.value && field.value !== newFields.confirm.value) {
              newFields.confirm = { ...newFields.confirm, error: 'Passwords do not match' };
            }
          }
        }
        newFields[fname] = { ...field, error, touched: true };
        if (error) errors.push(`${section.label}: ${error}`);
      }
      updated[sid] = { ...section, fields: newFields };
    }
    setSections(updated);
    setSummary(errors);
  };

  const handleReset = () => {
    const reset: Record<string, ValidationSection> = {};
    for (const [sid, section] of Object.entries(sections)) {
      const fields: Record<string, FieldState> = {};
      for (const fname of Object.keys(section.fields)) {
        fields[fname] = { value: '', error: '', touched: false };
      }
      reset[sid] = { ...section, fields };
    }
    setSections(reset);
    setSummary([]);
  };

  const handleAsyncCheck = async () => {
    const emailField = sections.async.fields.email;
    if (!emailField.value.trim()) {
      setSections((prev) => ({
        ...prev,
        async: { ...prev.async, fields: { email: { ...prev.async.fields.email, error: 'Email is required', touched: true } } },
      }));
      return;
    }
    setAsyncLoading(true);
    const err = await simulateAsyncValidation(emailField.value);
    setAsyncLoading(false);
    setSections((prev) => ({
      ...prev,
      async: { ...prev.async, fields: { email: { ...prev.async.fields.email, error: err, touched: true } } },
    }));
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Form Validation</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Validation framework demo</p>
      </div>

      {summary.length > 0 && (
        <div style={{ background: 'var(--color-bg-danger-subtle, #fef2f2)', border: '1px solid var(--color-border-danger)', borderRadius: 'var(--radius-md)', padding: '16px' }}>
          <strong style={{ color: 'var(--color-text-danger)', fontSize: 'var(--text-body)' }}>Validation Summary ({summary.length} error{summary.length > 1 ? 's' : ''})</strong>
          <ul style={{ margin: '8px 0 0', paddingLeft: '20px', fontSize: 'var(--text-caption)', color: 'var(--color-text-danger)' }}>
            {summary.map((s, i) => <li key={i}>{s}</li>)}
          </ul>
        </div>
      )}

      {Object.entries(sections).map(([sid, section]) => (
        <section key={sid} style={sectionStyle}>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{section.label}</h2>
          {Object.entries(section.fields).map(([fname, field]) => {
            const labelMap: Record<string, string> = { name: 'Full Name', email: 'Email', phone: 'Phone Number', password: 'Password', username: 'Username', pin: '6-digit PIN', pwd: 'Password', confirm: 'Confirm Password' };
            const placeholderMap: Record<string, string> = { name: 'Enter your name', email: 'email@example.com', phone: '+1 234 567 890', password: 'Enter password', username: '3-20 characters', pin: '123456', pwd: 'Enter password', confirm: 'Re-enter password' };
            return (
              <FieldRow key={fname} label={labelMap[fname] || fname}>
                <Input
                  value={field.value}
                  onChange={(v) => updateField(sid, fname, v)}
                  placeholder={placeholderMap[fname] || ''}
                  error={field.touched ? field.error : undefined}
                  type={fname.includes('password') || fname === 'pwd' || fname === 'confirm' ? 'password' : sid === 'email' || fname === 'email' ? 'email' : 'text'}
                />
                {sid === 'password' && field.value.length > 0 && (
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <div style={{ flex: 1, height: '4px', borderRadius: '2px', background: 'var(--color-bg-disabled)' }}>
                      <div style={{ width: `${getPasswordStrength(field.value).score * 100}%`, height: '100%', borderRadius: '2px', background: getPasswordStrength(field.value).color, transition: 'width 0.2s' }} />
                    </div>
                    <span style={{ fontSize: 'var(--text-caption)', color: getPasswordStrength(field.value).color, textTransform: 'capitalize' }}>{getPasswordStrength(field.value).label}</span>
                  </div>
                )}
              </FieldRow>
            );
          })}
          {sid === 'async' && (
            <button style={{ ...btnStyle, display: 'inline-flex', alignItems: 'center', gap: '8px', background: 'var(--color-bg-secondary-default)', alignSelf: 'flex-start' }} onClick={handleAsyncCheck} disabled={asyncLoading}>
              {asyncLoading && <span style={{ width: 14, height: 14, border: '2px solid #fff', borderTopColor: 'transparent', borderRadius: '50%', animation: 'sk-spin 0.6s linear infinite' }} />}
              Check Availability
            </button>
          )}
        </section>
      ))}

      <div style={{ display: 'flex', gap: '12px' }}>
        <button style={btnStyle} onClick={handleSubmit}>Submit All</button>
        <button style={{ ...btnStyle, background: 'transparent', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)' }} onClick={handleReset}>Reset All</button>
      </div>
    </div>
  );
}
