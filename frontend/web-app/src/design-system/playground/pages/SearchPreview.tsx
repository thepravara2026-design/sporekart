const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="11" cy="11" r="8"/><path d="M21 21l-4.35-4.35"/></svg>
);

const XIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M18 6L6 18M6 6l12 12"/></svg>
);

const spinner = (
  <span style={{ display: 'inline-block', width: 14, height: 14, border: '2px solid var(--color-text-secondary)', borderTopColor: 'transparent', borderRadius: '50%', animation: 'sk-spin 0.6s linear infinite' }} />
);

const searchStates = [
  {
    label: 'Default',
    props: { placeholder: 'Search...', value: '' },
  },
  {
    label: 'With Value (showing clear)',
    props: { placeholder: 'Search...', value: 'product name', hasClear: true },
  },
  {
    label: 'Loading',
    props: { placeholder: 'Search...', value: 'searching', loading: true },
  },
  {
    label: 'Disabled',
    props: { placeholder: 'Search...', value: '', disabled: true },
  },
  {
    label: 'Error',
    props: { placeholder: 'Search...', value: 'invalid query', error: true },
  },
];

export default function SearchPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Search</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Search input with all states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>States</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
          {searchStates.map((s) => {
            const borderColor = s.props.error ? 'var(--color-border-danger, #dc2626)' : 'var(--color-border-default)';
            const bgColor = s.props.disabled ? 'var(--color-bg-disabled, #f3f4f6)' : 'var(--color-bg-input, #fff)';
            return (
              <div key={s.label} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{s.label}</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 12px', border: `1px solid ${borderColor}`, borderRadius: 'var(--radius-md)', background: bgColor, cursor: s.props.disabled ? 'not-allowed' : 'text' }}>
                  {s.props.loading ? spinner : <SearchIcon />}
                  <input
                    type="search"
                    placeholder={s.props.placeholder}
                    defaultValue={s.props.value}
                    disabled={s.props.disabled}
                    style={{ border: 'none', background: 'transparent', outline: 'none', flex: 1, fontSize: 'var(--text-base)', color: s.props.disabled ? 'var(--color-text-disabled)' : 'inherit', width: '100%' }}
                  />
                  {s.props.hasClear && (
                    <button style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 0, color: 'var(--color-text-secondary)', display: 'flex' }}>
                      <XIcon />
                    </button>
                  )}
                </div>
                {s.props.error && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger, #dc2626)' }}>Search query contains invalid characters</span>}
              </div>
            );
          })}
        </div>
      </section>
    </div>
  );
}
