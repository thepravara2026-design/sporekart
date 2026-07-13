import { useState } from 'react';

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const PreviewBox = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const overlayStyle: React.CSSProperties = {
  position: 'fixed',
  inset: 0,
  background: 'rgba(0,0,0,0.5)',
  display: 'flex',
  alignItems: 'flex-start',
  justifyContent: 'center',
  paddingTop: '80px',
  zIndex: 100,
  fontFamily: 'var(--font-family)',
};

const paletteStyle: React.CSSProperties = {
  width: '560px',
  maxWidth: '90vw',
  maxHeight: '60vh',
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  boxShadow: 'var(--shadow-3)',
  display: 'flex',
  flexDirection: 'column',
  overflow: 'hidden',
};

const searchInputStyle: React.CSSProperties = {
  width: '100%',
  padding: '16px',
  border: 'none',
  borderBottom: '1px solid var(--color-border-default)',
  fontSize: 'var(--text-base)',
  background: 'transparent',
  color: 'var(--color-text-primary)',
  outline: 'none',
  fontFamily: 'var(--font-family)',
};

const groupLabelStyle: React.CSSProperties = {
  padding: '8px 16px 4px',
  fontSize: 'var(--text-xs)',
  color: 'var(--color-text-tertiary)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
};

const commandItemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '12px',
  padding: '10px 16px',
  cursor: 'pointer',
  fontSize: 'var(--text-sm)',
  color: 'var(--color-text-primary)',
  border: 'none',
  background: 'transparent',
  width: '100%',
  textAlign: 'left',
};

const shortcutHintStyle: React.CSSProperties = {
  marginLeft: 'auto',
  fontSize: 'var(--text-xs)',
  color: 'var(--color-text-tertiary)',
  background: 'var(--color-bg-subtle)',
  padding: '2px 6px',
  borderRadius: 'var(--radius-sm)',
};

const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="7" cy="7" r="5" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M11 11l3 3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const CommandIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M4 4h8v8H4z" stroke="currentColor" strokeWidth="1.3" strokeLinejoin="round"/>
    <path d="M2 6h2v4H2M12 6h2v4h-2" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
  </svg>
);

const NavigateIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M3 7h8M7 3l4 4-4 4" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const commandGroups: { label: string; items: { label: string; shortcut?: string; icon?: React.ReactNode }[] }[] = [
  {
    label: 'Navigate',
    items: [
      { label: 'Go to Dashboard', shortcut: 'G D', icon: <NavigateIcon /> },
      { label: 'Go to Products', shortcut: 'G P', icon: <NavigateIcon /> },
      { label: 'Go to Orders', shortcut: 'G O', icon: <NavigateIcon /> },
      { label: 'Go to Analytics', shortcut: 'G A', icon: <NavigateIcon /> },
    ],
  },
  {
    label: 'Actions',
    items: [
      { label: 'New Product', shortcut: 'N P', icon: <CommandIcon /> },
      { label: 'New Order', shortcut: 'N O', icon: <CommandIcon /> },
      { label: 'Export Report', shortcut: 'E R', icon: <CommandIcon /> },
    ],
  },
  {
    label: 'Settings',
    items: [
      { label: 'Open Settings', shortcut: 'S', icon: <CommandIcon /> },
      { label: 'Toggle Dark Mode', shortcut: 'D M', icon: <CommandIcon /> },
    ],
  },
];

function DefaultPalette() {
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState('');
  const [activeIdx, setActiveIdx] = useState(0);

  const allItems = commandGroups.flatMap((g) => g.items);
  const filtered = query.trim()
    ? allItems.filter((item) => item.label.toLowerCase().includes(query.toLowerCase()))
    : allItems;

  return (
    <div>
      <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
        <button
          onClick={() => { setOpen(true); setQuery(''); setActiveIdx(0); }}
          style={{
            padding: '8px 16px',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: 'var(--color-bg-surface-default)',
            cursor: 'pointer',
            fontSize: 'var(--text-sm)',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            color: 'var(--color-text-primary)',
          }}
        >
          <SearchIcon />
          <span>Open command palette</span>
          <span style={{ marginLeft: '12px', fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)', background: 'var(--color-bg-subtle)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>Ctrl+K</span>
        </button>
      </div>

      {open && (
        <div style={overlayStyle} onClick={() => setOpen(false)}>
          <div style={paletteStyle} onClick={(e) => e.stopPropagation()}>
            <div style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
              <span style={{ position: 'absolute', left: '16px', color: 'var(--color-text-tertiary)' }}><SearchIcon /></span>
              <input
                autoFocus
                type="text"
                value={query}
                onChange={(e) => { setQuery(e.target.value); setActiveIdx(0); }}
                placeholder="Type a command or search..."
                style={{ ...searchInputStyle, paddingLeft: '44px' }}
              />
            </div>
            <div style={{ overflowY: 'auto', flex: 1 }}>
              {filtered.length === 0 ? (
                <div style={{ padding: '32px 16px', textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-sm)' }}>
                  No results for "{query}"
                </div>
              ) : (
                commandGroups.map((group) => {
                  const groupFiltered = group.items.filter((item) =>
                    query.trim() ? item.label.toLowerCase().includes(query.toLowerCase()) : true
                  );
                  if (groupFiltered.length === 0) return null;
                  return (
                    <div key={group.label}>
                      <div style={groupLabelStyle}>{group.label}</div>
                      {groupFiltered.map((item) => {
                        const globalIdx = allItems.indexOf(item);
                        return (
                          <div
                            key={item.label}
                            style={{
                              ...commandItemStyle,
                              background: globalIdx === activeIdx ? 'var(--color-bg-subtle)' : 'transparent',
                            }}
                            onClick={() => setOpen(false)}
                            onMouseEnter={() => setActiveIdx(globalIdx)}
                          >
                            {item.icon}
                            <span>{item.label}</span>
                            {item.shortcut && <span style={shortcutHintStyle}>{item.shortcut}</span>}
                          </div>
                        );
                      })}
                    </div>
                  );
                })
              )}
            </div>
            <div style={{ padding: '8px 16px', borderTop: '1px solid var(--color-border-default)', display: 'flex', gap: '12px', fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)' }}>
              <span>↑↓ Navigate</span>
              <span>↵ Open</span>
              <span>Esc Close</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default function CommandPalettePreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Command Palette Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Command palette with search, groups, filtered results, keyboard hints</p>
      </div>

      <Section label="Command Palette Demo">
        <PreviewBox label="Click the button to open the palette overlay">
          <DefaultPalette />
        </PreviewBox>
      </Section>
    </div>
  );
}
