const inputTypes = ['text', 'email', 'tel', 'url', 'number', 'search', 'password'] as const;
const sizes = ['sm', 'md', 'lg'] as const;

const sizeInputStyle: Record<string, React.CSSProperties> = {
  sm: { padding: '4px 8px', fontSize: 'var(--text-sm)', borderRadius: 'var(--radius-sm)' },
  md: { padding: '8px 12px', fontSize: 'var(--text-base)', borderRadius: 'var(--radius-md)' },
  lg: { padding: '12px 16px', fontSize: 'var(--text-lg)', borderRadius: 'var(--radius-lg)' },
};

const stateStyles: Record<string, React.CSSProperties> = {
  default: { border: '1px solid var(--color-border-default)', background: 'var(--color-bg-input, #fff)' },
  focus: { border: '1px solid var(--color-focus-ring, #3b82f6)', background: 'var(--color-bg-input, #fff)', outline: '2px solid var(--color-focus-ring, #3b82f6)', outlineOffset: '-1px' },
  disabled: { border: '1px solid var(--color-border-disabled, #e5e7eb)', background: 'var(--color-bg-disabled, #f3f4f6)', color: 'var(--color-text-disabled, #9ca3af)', cursor: 'not-allowed' },
  error: { border: '1px solid var(--color-border-danger, #dc2626)', background: 'var(--color-bg-input, #fff)' },
  success: { border: '1px solid var(--color-border-success, #16a34a)', background: 'var(--color-bg-input, #fff)' },
  warning: { border: '1px solid var(--color-border-warning, #f59e0b)', background: 'var(--color-bg-input, #fff)' },
};

function InputCard({ type, size, state, label }: { type: string; size: string; state: string; label: string }) {
  const inputId = `${type}-${size}-${state}`;
  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '6px' }}>
      <label htmlFor={inputId} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</label>
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', ...sizeInputStyle[size as keyof typeof sizeInputStyle], ...stateStyles[state] }}>
        {state === 'prefix' && <span style={{ color: 'var(--color-text-secondary)', fontSize: 'inherit' }}>$</span>}
        <input
          id={inputId}
          type={type}
          placeholder={state === 'disabled' ? '' : `Placeholder`}
          disabled={state === 'disabled'}
          defaultValue={state === 'success' ? 'Valid value' : state === 'error' ? 'Bad value' : ''}
          style={{ border: 'none', background: 'transparent', outline: 'none', flex: 1, color: 'inherit', fontSize: 'inherit', width: '100%' }}
        />
        {state === 'suffix' && <span style={{ color: 'var(--color-text-secondary)', fontSize: 'inherit' }}>.00</span>}
      </div>
      {(state === 'error' || state === 'success' || state === 'warning' || state === 'helper' || state === 'counter' || state === 'required' || state === 'optional') && (
        <span style={{ fontSize: 'var(--text-caption)', color: state === 'error' ? 'var(--color-text-danger, #dc2626)' : state === 'success' ? 'var(--color-text-success, #16a34a)' : state === 'warning' ? 'var(--color-text-warning, #f59e0b)' : 'var(--color-text-secondary)' }}>
          {state === 'error' && 'This field has an error'}
          {state === 'success' && 'Looks good!'}
          {state === 'warning' && 'This value is unusual'}
          {state === 'helper' && 'This is helper text'}
          {state === 'counter' && '12 / 100'}
          {state === 'required' && 'Required'}
          {state === 'optional' && 'Optional'}
        </span>
      )}
    </div>
  );
}

const states = [
  { id: 'default', label: 'Default' },
  { id: 'focus', label: 'Focus' },
  { id: 'disabled', label: 'Disabled' },
  { id: 'error', label: 'Error' },
  { id: 'success', label: 'Success' },
  { id: 'warning', label: 'Warning' },
  { id: 'helper', label: 'Helper Text' },
  { id: 'counter', label: 'Char Counter' },
  { id: 'prefix', label: 'Prefix ($)' },
  { id: 'suffix', label: 'Suffix (.00)' },
  { id: 'required', label: 'Required' },
  { id: 'optional', label: 'Optional' },
];

export default function InputsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Inputs</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All input types, sizes, and states</p>
      </div>

      {inputTypes.map((type) => (
        <section key={type} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0, textTransform: 'capitalize' }}>{type}</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '16px' }}>
            {states.map((state) => (
              <InputCard key={`${type}-${state.id}`} type={type} size="md" state={state.id} label={`${state.label}`} />
            ))}
          </div>
          <h3 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-medium)', margin: '8px 0 0' }}>Sizes</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
            {sizes.map((size) => (
              <InputCard key={`${type}-size-${size}`} type={type} size={size} state="default" label={`Size: ${size}`} />
            ))}
          </div>
        </section>
      ))}
    </div>
  );
}
