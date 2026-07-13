import { useState } from 'react';

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const PreviewBox = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const menuPanel: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-md)',
  boxShadow: 'var(--shadow-2)',
  padding: '4px',
  minWidth: '200px',
  fontFamily: 'var(--font-family)',
};

const menuItem: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '8px',
  padding: '8px 12px',
  borderRadius: 'var(--radius-sm)',
  cursor: 'pointer',
  fontSize: 'var(--text-sm)',
  color: 'var(--color-text-primary)',
  border: 'none',
  background: 'transparent',
  width: '100%',
  textAlign: 'left',
};

const menuDivider: React.CSSProperties = {
  height: '1px',
  background: 'var(--color-border-default)',
  margin: '4px 8px',
};

const shortcutStyle: React.CSSProperties = {
  marginLeft: 'auto',
  fontSize: 'var(--text-xs)',
  color: 'var(--color-text-tertiary)',
};

const dangerItem: React.CSSProperties = {
  ...menuItem,
  color: 'var(--color-text-danger, #dc2626)',
};

const KebabIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="8" cy="4" r="1.5" fill="currentColor"/>
    <circle cx="8" cy="8" r="1.5" fill="currentColor"/>
    <circle cx="8" cy="12" r="1.5" fill="currentColor"/>
  </svg>
);

const EditIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M10 1l3 3-8 8H2v-3l8-8z" stroke="currentColor" strokeWidth="1.3" strokeLinejoin="round"/>
  </svg>
);

const CopyIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <rect x="4" y="4" width="9" height="9" rx="1" stroke="currentColor" strokeWidth="1.3"/>
    <path d="M1 10V2a1 1 0 011-1h8" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
  </svg>
);

const TrashIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M2 4h10M5 4V2.5A.5.5 0 015.5 2h3a.5.5 0 01.5.5V4M11 4v7.5a1 1 0 01-1 1H4a1 1 0 01-1-1V4" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const ShareIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <circle cx="11" cy="3" r="2" stroke="currentColor" strokeWidth="1.3"/>
    <circle cx="4" cy="7" r="2" stroke="currentColor" strokeWidth="1.3"/>
    <circle cx="11" cy="11" r="2" stroke="currentColor" strokeWidth="1.3"/>
    <path d="M6 8.5l3 1.5M6 5.5l3-1.5" stroke="currentColor" strokeWidth="1.3"/>
  </svg>
);

const UserMenuIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <circle cx="7" cy="4" r="2.5" stroke="currentColor" strokeWidth="1.3"/>
    <path d="M2 12c0-2.8 2.2-5 5-5s5 2.2 5 5" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
  </svg>
);

const SettingsIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <circle cx="7" cy="7" r="2.5" stroke="currentColor" strokeWidth="1.3"/>
    <path d="M7 1v2M7 11v2M1 7h2M11 7h2M3.5 3.5l1.5 1.5M9 9l1.5 1.5M3.5 10.5l1.5-1.5M9 5l1.5-1.5" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
  </svg>
);

const LogoutIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M5 13H3a1 1 0 01-1-1V2a1 1 0 011-1h2M9 10l3-3-3-3M12 7H5" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

function DropdownMenuDemo() {
  const [open, setOpen] = useState(false);
  return (
    <div style={{ position: 'relative', display: 'inline-block' }}>
      <button
        onClick={() => setOpen(!open)}
        style={{
          padding: '8px 16px',
          border: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-sm)',
          background: 'var(--color-bg-surface-default)',
          cursor: 'pointer',
          fontSize: 'var(--text-sm)',
          fontWeight: 'var(--weight-medium)',
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
        }}
      >
        Actions
        <svg width="12" height="12" viewBox="0 0 12 12" fill="none" style={{ transform: open ? 'rotate(180deg)' : undefined, transition: 'transform 0.2s' }}>
          <path d="M3 5l3 3 3-3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      </button>
      {open && (
        <div style={{ ...menuPanel, position: 'absolute', top: 'calc(100% + 4px)', left: 0, zIndex: 10 }}>
          <button style={menuItem}><EditIcon /> Edit <span style={shortcutStyle}>Ctrl+E</span></button>
          <button style={menuItem}><CopyIcon /> Duplicate <span style={shortcutStyle}>Ctrl+D</span></button>
          <button style={menuItem}><ShareIcon /> Share</button>
          <div style={menuDivider} />
          <button style={dangerItem}><TrashIcon /> Delete <span style={shortcutStyle}>Del</span></button>
        </div>
      )}
    </div>
  );
}

function ContextMenuDemo() {
  const [open, setOpen] = useState(false);
  const [pos, setPos] = useState({ x: 0, y: 0 });
  return (
    <div
      onContextMenu={(e) => { e.preventDefault(); setPos({ x: e.clientX, y: e.clientY }); setOpen(true); }}
      onClick={() => setOpen(false)}
      style={{
        height: '120px',
        border: '2px dashed var(--color-border-default)',
        borderRadius: 'var(--radius-md)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: 'var(--text-sm)',
        color: 'var(--color-text-tertiary)',
        cursor: 'context-menu',
        userSelect: 'none',
        position: 'relative',
      }}
    >
      Right-click this area
      {open && (
        <div style={{ ...menuPanel, position: 'fixed', top: pos.y, left: pos.x, zIndex: 100 }}>
          <button style={menuItem}>Cut <span style={shortcutStyle}>Ctrl+X</span></button>
          <button style={menuItem}>Copy <span style={shortcutStyle}>Ctrl+C</span></button>
          <button style={menuItem}>Paste <span style={shortcutStyle}>Ctrl+V</span></button>
          <div style={menuDivider} />
          <button style={menuItem}>Delete</button>
        </div>
      )}
    </div>
  );
}

function OverflowMenuDemo() {
  const [open, setOpen] = useState(false);
  return (
    <div style={{ position: 'relative', display: 'inline-block' }}>
      <button
        onClick={() => setOpen(!open)}
        style={{ ...menuItem, width: '36px', height: '36px', justifyContent: 'center', borderRadius: 'var(--radius-md)' }}
      >
        <KebabIcon />
      </button>
      {open && (
        <div style={{ ...menuPanel, position: 'absolute', top: '100%', right: 0, zIndex: 10 }}>
          <button style={menuItem}>Rename</button>
          <button style={menuItem}>Move to</button>
          <button style={menuItem}>Archive</button>
          <div style={menuDivider} />
          <button style={dangerItem}>Remove</button>
        </div>
      )}
    </div>
  );
}

function UserMenuDemo() {
  const [open, setOpen] = useState(false);
  return (
    <div style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
      <button
        onClick={() => setOpen(!open)}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          cursor: 'pointer',
          border: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-md)',
          padding: '4px 12px 4px 4px',
          background: 'var(--color-bg-surface-default)',
        }}
      >
        <div style={{ width: '28px', height: '28px', borderRadius: '50%', background: 'var(--color-bg-primary-default)', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 'var(--text-xs)', fontWeight: 'var(--weight-bold)' }}>JD</div>
        <span style={{ fontSize: 'var(--text-sm)' }}>John Doe</span>
      </button>
      {open && (
        <div style={{ ...menuPanel, position: 'absolute', top: 'calc(100% + 4px)', right: 0, zIndex: 10, minWidth: '220px' }}>
          <div style={{ padding: '8px 12px', borderBottom: '1px solid var(--color-border-default)', marginBottom: '4px' }}>
            <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-sm)' }}>John Doe</div>
            <div style={{ fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)' }}>john@example.com</div>
          </div>
          <button style={menuItem}><UserMenuIcon /> Profile</button>
          <button style={menuItem}><SettingsIcon /> Settings</button>
          <div style={menuDivider} />
          <button style={dangerItem}><LogoutIcon /> Sign out</button>
        </div>
      )}
    </div>
  );
}

function ActionMenuDemo() {
  const items = [
    { label: 'Edit', icon: <EditIcon /> },
    { label: 'Duplicate', icon: <CopyIcon /> },
    { label: 'Share', icon: <ShareIcon /> },
    { label: 'Delete', icon: <TrashIcon />, danger: true },
  ];
  return (
    <div style={{ ...menuPanel, width: '100%', maxWidth: '240px' }}>
      {items.map((item, i) => (
        <button key={i} style={item.danger ? dangerItem : menuItem}>
          {item.icon}
          {item.label}
        </button>
      ))}
    </div>
  );
}

export default function MenuPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Menu Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All menu types and states</p>
      </div>

      <Section label="Dropdown Menu">
        <PreviewBox label="Trigger button with items, shortcuts, dividers, danger">
          <DropdownMenuDemo />
        </PreviewBox>
      </Section>

      <Section label="Context Menu">
        <PreviewBox label="Right-click area to open">
          <ContextMenuDemo />
        </PreviewBox>
      </Section>

      <Section label="Overflow Menu">
        <PreviewBox label="Kebab button">
          <OverflowMenuDemo />
        </PreviewBox>
      </Section>

      <Section label="User Menu">
        <PreviewBox label="Avatar with dropdown">
          <UserMenuDemo />
        </PreviewBox>
      </Section>

      <Section label="Action Menu">
        <PreviewBox label="Vertical action list">
          <ActionMenuDemo />
        </PreviewBox>
      </Section>
    </div>
  );
}
