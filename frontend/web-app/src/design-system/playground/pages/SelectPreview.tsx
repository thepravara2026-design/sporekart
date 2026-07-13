import { useState } from 'react';

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

const labelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  marginBottom: '4px',
};

const selectBase: React.CSSProperties = {
  appearance: 'none',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-md)',
  padding: '8px 36px 8px 12px',
  fontSize: 'var(--text-base)',
  background: 'var(--color-bg-input)',
  color: 'var(--color-text-primary)',
  outline: 'none',
  width: '100%',
  boxSizing: 'border-box',
  cursor: 'pointer',
  backgroundImage: `url("data:image/svg+xml,%3Csvg width='10' height='6' viewBox='0 0 10 6' fill='none' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M1 1l4 4 4-4' stroke='currentColor' stroke-width='1.5' stroke-linecap='round' stroke-linejoin='round'/%3E%3C/svg%3E")`,
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'right 12px center',
};

const chipStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: '4px',
  padding: '2px 8px',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-primary-subtle)',
  color: 'var(--color-text-link)',
  fontSize: 'var(--text-caption)',
};

const optionRow: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '8px',
  padding: '8px 12px',
  borderRadius: 'var(--radius-sm)',
  cursor: 'pointer',
};

const sizes = ['sm', 'md', 'lg'] as const;

const sizeSelect: Record<string, React.CSSProperties> = {
  sm: { ...selectBase, padding: '4px 28px 4px 8px', fontSize: 'var(--text-sm)', backgroundPosition: 'right 8px center' },
  md: { ...selectBase },
  lg: { ...selectBase, padding: '12px 36px 12px 16px', fontSize: 'var(--text-lg)' },
};

const options = [
  { value: '', label: 'Select option...' },
  { value: '1', label: 'Option One' },
  { value: '2', label: 'Option Two' },
  { value: '3', label: 'Option Three' },
  { value: '4', label: 'Option Four' },
];

const searchResults = [
  { value: 'apple', label: 'Apple' },
  { value: 'banana', label: 'Banana' },
  { value: 'cherry', label: 'Cherry' },
  { value: 'date', label: 'Date' },
  { value: 'elderberry', label: 'Elderberry' },
];

function SelectRow({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', padding: '12px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)' }}>
      <span style={labelStyle}>{label}</span>
      {children}
    </div>
  );
}

function DropdownPortal({ children, show }: { children: React.ReactNode; show: boolean }) {
  if (!show) return null;
  return (
    <div style={{
      position: 'absolute',
      top: '100%',
      left: 0,
      right: 0,
      marginTop: '4px',
      background: 'var(--color-bg-surface-default)',
      border: '1px solid var(--color-border-default)',
      borderRadius: 'var(--radius-md)',
      boxShadow: 'var(--shadow-2)',
      zIndex: 100,
      maxHeight: '200px',
      overflow: 'auto',
    }}>{children}</div>
  );
}

function SearchableSelect({ disabled }: { disabled?: boolean }) {
  const [query, setQuery] = useState('');
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState('');
  const filtered = searchResults.filter((o) => o.label.toLowerCase().includes(query.toLowerCase()));
  return (
    <div style={{ position: 'relative' }}>
      <div style={{ display: 'flex', alignItems: 'center', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', overflow: 'hidden', opacity: disabled ? 0.5 : 1 }}>
        <input
          style={{ border: 'none', outline: 'none', flex: 1, padding: '8px 12px', fontSize: 'var(--text-base)', background: 'var(--color-bg-input)', color: 'var(--color-text-primary)', cursor: disabled ? 'not-allowed' : 'text' }}
          value={query}
          onChange={(e) => { setQuery(e.target.value); setOpen(true); }}
          onFocus={() => setOpen(true)}
          onBlur={() => setTimeout(() => setOpen(false), 150)}
          placeholder="Search..."
          disabled={disabled}
        />
        {query && !disabled && (
          <button style={{ border: 'none', background: 'transparent', cursor: 'pointer', padding: '0 8px', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }} onClick={() => { setQuery(''); setSelected(''); }} tabIndex={-1}>
            &times;
          </button>
        )}
      </div>
      <DropdownPortal show={open && !disabled}>
        {filtered.length === 0 ? (
          <div style={{ padding: '12px', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', textAlign: 'center' }}>No results found</div>
        ) : (
          filtered.map((o) => (
            <div key={o.value} style={{ ...optionRow, background: selected === o.value ? 'var(--color-bg-primary-subtle)' : 'transparent' }}
              onMouseDown={() => { setSelected(o.value); setQuery(o.label); setOpen(false); }}>
              {o.label}
            </div>
          ))
        )}
      </DropdownPortal>
    </div>
  );
}

function AsyncSelect({ state }: { state: 'loading' | 'loaded' | 'error' }) {
  const [open, setOpen] = useState(false);
  return (
    <div style={{ position: 'relative' }}>
      <div style={{ display: 'flex', alignItems: 'center', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', padding: '8px 12px', cursor: 'pointer', background: 'var(--color-bg-input)' }}
        onClick={() => !['loading', 'error'].includes(state) && setOpen((v) => !v)}
        tabIndex={0}
        onKeyDown={(e) => { if (e.key === 'Enter') setOpen((v) => !v); }}
      >
        <span style={{ flex: 1, color: state === 'loading' ? 'var(--color-text-secondary)' : 'var(--color-text-primary)', fontSize: 'var(--text-base)' }}>
          {state === 'loading' && 'Loading options...'}
          {state === 'loaded' && 'Select country'}
          {state === 'error' && 'Failed to load options'}
        </span>
        {state === 'loading' && <span style={{ width: 14, height: 14, border: '2px solid var(--color-border-default)', borderTopColor: 'var(--color-text-link)', borderRadius: '50%', animation: 'sk-spin 0.6s linear infinite' }} />}
        {state === 'error' && <span style={{ color: 'var(--color-text-danger)', fontSize: 'var(--text-body)' }}>&#9888;</span>}
        {state === 'loaded' && <svg width="10" height="6" viewBox="0 0 10 6" fill="none"><path d="M1 1l4 4 4-4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>}
      </div>
      <DropdownPortal show={open && state === 'loaded'}>
        {searchResults.map((o) => (
          <div key={o.value} style={optionRow}>{o.label}</div>
        ))}
      </DropdownPortal>
    </div>
  );
}

function MultiSelect({ disabled }: { disabled?: boolean }) {
  const [selected, setSelected] = useState<string[]>(['1', '3']);
  return (
    <div style={{ position: 'relative' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', flexWrap: 'wrap', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', padding: '6px 12px', cursor: disabled ? 'not-allowed' : 'pointer', background: disabled ? 'var(--color-bg-disabled)' : 'var(--color-bg-input)', opacity: disabled ? 0.5 : 1, minHeight: '38px' }}
        tabIndex={disabled ? -1 : 0}
      >
        {selected.length === 0 && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-base)' }}>Select options...</span>}
        {selected.map((v) => {
          const opt = options.find((o) => o.value === v);
          return opt ? (
            <span key={v} style={chipStyle}>
              {opt.label}
              {!disabled && <span style={{ cursor: 'pointer', marginLeft: '2px' }} onClick={() => setSelected((prev) => prev.filter((x) => x !== v))}>&times;</span>}
            </span>
          ) : null;
        })}
        <svg width="10" height="6" viewBox="0 0 10 6" fill="none" style={{ marginLeft: 'auto' }}><path d="M1 1l4 4 4-4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>
      </div>
    </div>
  );
}

export default function SelectPreview() {
  const [selectVal, setSelectVal] = useState('');
  return (
    <div style={sectionStyle}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Select Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All select variants</p>
      </div>

      <section style={cardStyle}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Single Select</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '12px' }}>
          <SelectRow label="Default">
            <select style={selectBase} value={selectVal} onChange={(e) => setSelectVal(e.target.value)}>
              {options.map((o) => <option key={o.value} value={o.value} disabled={o.value === ''}>{o.label}</option>)}
            </select>
          </SelectRow>
          <SelectRow label="Selected">
            <select style={selectBase} value="2" onChange={() => {}}>
              {options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
            </select>
          </SelectRow>
          <SelectRow label="Disabled">
            <select style={{ ...selectBase, opacity: 0.5, cursor: 'not-allowed', background: 'var(--color-bg-disabled)', color: 'var(--color-text-disabled)' }} disabled>
              {options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
            </select>
          </SelectRow>
          <SelectRow label="Error">
            <select style={{ ...selectBase, borderColor: 'var(--color-border-danger)' }}>
              {options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
            </select>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger)' }}>Please select an option</span>
          </SelectRow>
        </div>
        <h3 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-medium)', margin: '8px 0 0' }}>Sizes</h3>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          {sizes.map((size) => (
            <SelectRow key={size} label={`Size: ${size}`}>
              <select style={sizeSelect[size]}>
                {options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
              </select>
            </SelectRow>
          ))}
        </div>
      </section>

      <section style={cardStyle}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>MultiSelect</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '12px' }}>
          <SelectRow label="Default"><MultiSelect /></SelectRow>
          <SelectRow label="Disabled"><MultiSelect disabled /></SelectRow>
        </div>
      </section>

      <section style={cardStyle}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Searchable Select</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '12px' }}>
          <SelectRow label="Default (searchable)"><SearchableSelect /></SelectRow>
          <SelectRow label="Disabled"><SearchableSelect disabled /></SelectRow>
        </div>
      </section>

      <section style={cardStyle}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Async Select</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '12px' }}>
          <SelectRow label="Loading"><AsyncSelect state="loading" /></SelectRow>
          <SelectRow label="Loaded"><AsyncSelect state="loaded" /></SelectRow>
          <SelectRow label="Error"><AsyncSelect state="error" /></SelectRow>
        </div>
      </section>
    </div>
  );
}
