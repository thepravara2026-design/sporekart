const cards: { title: string; description: string; path: string }[] = [
  { title: 'Form Layouts', description: 'Single column, two column, sections, footer, actions.', path: '/design-system/forms/layouts' },
  { title: 'Form Validation', description: 'Required, email, phone, password, length, pattern, cross-field, async validation.', path: '/design-system/forms/validation' },
  { title: 'Address Form', description: 'Address form with compact mode, validation, responsive view.', path: '/design-system/forms/address' },
  { title: 'File Upload', description: 'Single, multiple, drag-and-drop, progress, error states.', path: '/design-system/forms/upload' },
  { title: 'Select Preview', description: 'Single, multi, searchable, async select with all states.', path: '/design-system/forms/select' },
];

const linkStyle: React.CSSProperties = {
  color: 'var(--color-text-link)',
  textDecoration: 'none',
  fontWeight: 'var(--weight-medium)',
  display: 'inline-flex',
  alignItems: 'center',
  gap: '4px',
  marginTop: 'auto',
};

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  padding: '24px',
  boxShadow: 'var(--shadow-1)',
  display: 'flex',
  flexDirection: 'column',
  gap: '8px',
  textDecoration: 'none',
  color: 'inherit',
};

export default function FormsIndex() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Form System</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Sprint 20 Part 3 — Form system showcase</p>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
        {cards.map((card) => (
          <a key={card.path} href={card.path} style={cardStyle}>
            <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{card.title}</h2>
            <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: 1.5 }}>{card.description}</p>
            <span style={linkStyle}>
              Open preview
              <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
                <path d="M3 7h8M8 3l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </span>
          </a>
        ))}
      </div>
    </div>
  );
}
