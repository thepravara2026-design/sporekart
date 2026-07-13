import { useState } from 'react';

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '16px',
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  padding: '24px',
  boxShadow: 'var(--shadow-1)',
};

const labelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  marginBottom: '4px',
};

const inputStyle: React.CSSProperties = {
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

const btnSecStyle: React.CSSProperties = {
  ...btnStyle,
  background: 'transparent',
  color: 'var(--color-text-primary)',
  border: '1px solid var(--color-border-default)',
};

function FormField({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
      <label style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)' }}>{label}</label>
      {children}
    </div>
  );
}

function FormRow({ children }: { children: React.ReactNode }) {
  return <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>{children}</div>;
}

function FormLayout({ columns, children }: { columns: number; children: React.ReactNode }) {
  return (
    <div style={{
      display: 'grid',
      gridTemplateColumns: columns === 1 ? '1fr' : '1fr 1fr',
      gap: '16px',
    }}>{children}</div>
  );
}

function FormSection({ title, collapsible, children }: { title: string; collapsible?: boolean; children: React.ReactNode }) {
  const [open, setOpen] = useState(!collapsible);
  return (
    <div style={{ border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', overflow: 'hidden' }}>
      <button
        onClick={collapsible ? () => setOpen((v) => !v) : undefined}
        style={{
          width: '100%',
          textAlign: 'left',
          padding: '12px 16px',
          background: 'var(--color-bg-surface-raised)',
          border: 'none',
          borderBottom: open ? '1px solid var(--color-border-default)' : 'none',
          fontSize: 'var(--text-h3)',
          fontWeight: 'var(--weight-semibold)',
          color: 'var(--color-text-primary)',
          cursor: collapsible ? 'pointer' : 'default',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
        }}
      >
        <span>{title}</span>
        {collapsible && (
          <span style={{ transform: open ? 'rotate(180deg)' : 'none', transition: 'transform 0.2s' }}>
            <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
              <path d="M3 5l3 3 3-3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </span>
        )}
      </button>
      {open && <div style={{ padding: '16px', display: 'flex', flexDirection: 'column', gap: '12px' }}>{children}</div>}
    </div>
  );
}

function FormFooter({ children }: { children: React.ReactNode }) {
  return (
    <div style={{
      position: 'sticky',
      bottom: 0,
      background: 'var(--color-bg-surface-default)',
      borderTop: '1px solid var(--color-border-default)',
      padding: '16px 24px',
      display: 'flex',
      justifyContent: 'flex-end',
      gap: '12px',
    }}>{children}</div>
  );
}

function FormActions({ state }: { state: 'idle' | 'submitting' | 'disabled' }) {
  return (
    <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end', padding: '16px 0' }}>
      <button style={{ ...btnSecStyle, opacity: state === 'disabled' ? 0.5 : 1, cursor: state === 'disabled' ? 'not-allowed' : 'pointer' }} disabled={state === 'disabled'}>Cancel</button>
      <button
        style={{
          ...btnStyle,
          opacity: state === 'disabled' ? 0.5 : 1,
          cursor: state === 'disabled' ? 'not-allowed' : 'pointer',
          display: 'inline-flex',
          alignItems: 'center',
          gap: '8px',
        }}
        disabled={state === 'disabled'}
      >
        {state === 'submitting' && <span style={{ width: 14, height: 14, border: '2px solid #fff', borderTopColor: 'transparent', borderRadius: '50%', animation: 'sk-spin 0.6s linear infinite' }} />}
        {state === 'submitting' ? 'Saving...' : 'Save'}
      </button>
    </div>
  );
}

export default function FormLayoutsPreview() {
  const [actionState, setActionState] = useState<'idle' | 'submitting' | 'disabled'>('idle');
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Form Layouts</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All form layout variants</p>
      </div>

      <section style={sectionStyle}>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Single Column Form</h2>
        <FormLayout columns={1}>
          <FormField label="Full Name"><input style={inputStyle} placeholder="Enter your name" /></FormField>
          <FormField label="Email"><input style={inputStyle} type="email" placeholder="email@example.com" /></FormField>
          <FormField label="Message"><textarea style={{ ...inputStyle, minHeight: '80px', resize: 'vertical' }} placeholder="Your message" /></FormField>
        </FormLayout>
        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}><button style={btnStyle}>Submit</button></div>
      </section>

      <section style={sectionStyle}>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Two Column Form</h2>
        <FormRow>
          <FormField label="First Name"><input style={inputStyle} placeholder="John" /></FormField>
          <FormField label="Last Name"><input style={inputStyle} placeholder="Doe" /></FormField>
        </FormRow>
        <FormRow>
          <FormField label="Phone"><input style={inputStyle} type="tel" placeholder="+1 234 567 890" /></FormField>
          <FormField label="Department">
            <select style={{ ...inputStyle, appearance: 'none' }}>
              <option>Engineering</option>
              <option>Design</option>
              <option>Marketing</option>
            </select>
          </FormField>
        </FormRow>
        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}><button style={btnStyle}>Submit</button></div>
      </section>

      <section style={sectionStyle}>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Form Sections</h2>
        <FormSection title="Personal Information (Collapsible)" collapsible>
          <FormField label="Full Name"><input style={inputStyle} placeholder="Enter name" /></FormField>
          <FormField label="Email"><input style={inputStyle} type="email" placeholder="email@example.com" /></FormField>
        </FormSection>
        <FormSection title="Address">
          <FormField label="Street"><input style={inputStyle} placeholder="123 Main St" /></FormField>
          <FormRow>
            <FormField label="City"><input style={inputStyle} placeholder="City" /></FormField>
            <FormField label="ZIP"><input style={inputStyle} placeholder="12345" /></FormField>
          </FormRow>
        </FormSection>
        <FormSection title="Preferences (Collapsible)" collapsible>
          <FormField label="Newsletter">
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: 'var(--text-body)' }}>
              <input type="checkbox" /> Subscribe to newsletter
            </label>
          </FormField>
        </FormSection>
      </section>

      <section style={{ ...sectionStyle, padding: 0 }}>
        <div style={{ padding: '24px' }}>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Sticky Footer</h2>
          <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Footer sticks at the bottom on scroll</p>
          <FormField label="Name"><input style={inputStyle} placeholder="Enter name" /></FormField>
        </div>
        <FormFooter>
          <button style={btnSecStyle}>Cancel</button>
          <button style={btnStyle}>Save</button>
        </FormFooter>
      </section>

      <section style={sectionStyle}>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Form Actions</h2>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
          <span style={labelStyle}>Idle</span>
          <FormActions state="idle" />
          <span style={labelStyle}>Submitting</span>
          <FormActions state="submitting" />
          <span style={labelStyle}>Disabled</span>
          <FormActions state="disabled" />
        </div>
        <div style={{ display: 'flex', gap: '8px' }}>
          <button style={{ ...btnStyle, background: 'var(--color-bg-secondary-default)' }} onClick={() => setActionState('idle')}>Idle</button>
          <button style={{ ...btnStyle, background: 'var(--color-bg-secondary-default)' }} onClick={() => setActionState('submitting')}>Submit</button>
          <button style={{ ...btnStyle, background: 'var(--color-bg-secondary-default)' }} onClick={() => setActionState('disabled')}>Disable</button>
        </div>
        <FormActions state={actionState} />
      </section>
    </div>
  );
}
