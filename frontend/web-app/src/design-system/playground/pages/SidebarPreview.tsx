import { useState } from 'react';

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const PreviewBox = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const sidebarBase: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  background: 'var(--color-bg-surface-default)',
  borderRight: '1px solid var(--color-border-default)',
  fontFamily: 'var(--font-family)',
  overflow: 'hidden',
};

const itemBase: React.CSSProperties = {
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

const activeItem: React.CSSProperties = {
  ...itemBase,
  background: 'var(--color-bg-subtle)',
  fontWeight: 'var(--weight-semibold)',
};

const badgeStyle: React.CSSProperties = {
  marginLeft: 'auto',
  fontSize: 'var(--text-xs)',
  padding: '2px 8px',
  borderRadius: 'var(--radius-full)',
  background: 'var(--color-bg-primary-default)',
  color: '#fff',
};

const groupLabel: React.CSSProperties = {
  fontSize: 'var(--text-xs)',
  color: 'var(--color-text-tertiary)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
  padding: '12px 12px 4px',
};

const PinIcon = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
    <path d="M7 1l4 4-2 2-4 4-2-2 4-4 2-2z" stroke="currentColor" strokeWidth="1.2" strokeLinejoin="round"/>
  </svg>
);

const items = [
  { label: 'Dashboard', icon: '📊' },
  { label: 'Products', icon: '🧪' },
  { label: 'Orders', icon: '📦' },
  { label: 'Customers', icon: '👤' },
  { label: 'Analytics', icon: '📈' },
  { label: 'Settings', icon: '⚙️' },
];

function FullSidebar() {
  const [activeIdx, setActiveIdx] = useState(0);
  return (
    <div style={{ ...sidebarBase, width: '240px', height: '360px', padding: '8px' }}>
      <div style={{ padding: '12px', fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-base)', borderBottom: '1px solid var(--color-border-default)', marginBottom: '8px' }}>SporeKart</div>
      <span style={groupLabel}>Main</span>
      {items.slice(0, 4).map((item, i) => (
        <button key={item.label} style={i === activeIdx ? activeItem : itemBase} onClick={() => setActiveIdx(i)}>
          <span>{item.icon}</span>
          <span>{item.label}</span>
        </button>
      ))}
      <span style={groupLabel}>System</span>
      {items.slice(4).map((item, i) => (
        <button key={item.label} style={i + 4 === activeIdx ? activeItem : itemBase} onClick={() => setActiveIdx(i + 4)}>
          <span>{item.icon}</span>
          <span>{item.label}</span>
        </button>
      ))}
    </div>
  );
}

function MiniSidebar() {
  const [active, setActive] = useState(0);
  return (
    <div style={{ ...sidebarBase, width: '56px', height: '360px', padding: '8px', alignItems: 'center', gap: '4px' }}>
      <div style={{ width: '32px', height: '32px', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-subtle)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '12px' }}>SK</div>
      {items.map((item, i) => (
        <button
          key={item.label}
          onClick={() => setActive(i)}
          style={{
            ...itemBase,
            justifyContent: 'center',
            width: '40px',
            height: '40px',
            padding: '0',
            borderRadius: 'var(--radius-md)',
            background: i === active ? 'var(--color-bg-subtle)' : 'transparent',
          }}
          title={item.label}
        >
          <span style={{ fontSize: '18px' }}>{item.icon}</span>
        </button>
      ))}
    </div>
  );
}

function CollapsibleSidebar() {
  const [collapsed, setCollapsed] = useState(false);
  const [active, setActive] = useState(0);
  const w = collapsed ? '56px' : '240px';
  return (
    <div style={{ ...sidebarBase, width: w, height: '360px', padding: '8px', transition: 'width 0.25s' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: collapsed ? 'center' : 'space-between', padding: collapsed ? '8px 0' : '12px', borderBottom: '1px solid var(--color-border-default)', marginBottom: '8px' }}>
        {!collapsed && <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-base)' }}>SporeKart</span>}
        {collapsed && <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-sm)' }}>SK</span>}
        <button
          onClick={() => setCollapsed(!collapsed)}
          style={{ ...itemBase, width: '28px', height: '28px', justifyContent: 'center', padding: '0', borderRadius: 'var(--radius-sm)' }}
        >
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style={{ transform: collapsed ? 'rotate(180deg)' : 'rotate(0deg)' }}>
            <path d="M9 3L5 7l4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </button>
      </div>
      {items.map((item, i) => (
        <button
          key={item.label}
          onClick={() => setActive(i)}
          style={{
            ...itemBase,
            justifyContent: collapsed ? 'center' : 'flex-start',
            padding: collapsed ? '8px 0' : '8px 12px',
            background: i === active ? 'var(--color-bg-subtle)' : 'transparent',
          }}
          title={collapsed ? item.label : undefined}
        >
          <span style={{ fontSize: '16px' }}>{item.icon}</span>
          {!collapsed && <span>{item.label}</span>}
        </button>
      ))}
    </div>
  );
}

function SidebarWithPinned() {
  const [active, setActive] = useState(0);
  return (
    <div style={{ ...sidebarBase, width: '240px', height: '360px', padding: '8px' }}>
      <div style={{ padding: '12px', fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-base)', borderBottom: '1px solid var(--color-border-default)', marginBottom: '8px' }}>SporeKart</div>
      <span style={groupLabel}>Pinned</span>
      {['Dashboard', 'Reports'].map((label, i) => (
        <button key={label} style={i === active ? activeItem : { ...itemBase, color: 'var(--color-text-secondary)' }} onClick={() => setActive(i)}>
          <PinIcon />
          <span>{label}</span>
        </button>
      ))}
      <span style={groupLabel}>Main</span>
      {items.slice(1, 4).map((item, i) => (
        <button key={item.label} style={i + 2 === active ? activeItem : itemBase} onClick={() => setActive(i + 2)}>
          <span>{item.icon}</span>
          <span>{item.label}</span>
        </button>
      ))}
    </div>
  );
}

function MobileSidebar() {
  const [open, setOpen] = useState(false);
  return (
    <div style={{ position: 'relative', height: '360px' }}>
      <button onClick={() => setOpen(!open)} style={{ padding: '8px 16px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>
        {open ? 'Close Overlay' : 'Open Mobile Sidebar'}
      </button>
      {open && (
        <div style={{ position: 'absolute', top: '40px', left: 0, width: '280px', height: '300px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', boxShadow: 'var(--shadow-3)', padding: '8px', zIndex: 20, border: '1px solid var(--color-border-default)' }}>
          <div style={{ padding: '12px', fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-base)', borderBottom: '1px solid var(--color-border-default)', marginBottom: '8px' }}>SporeKart</div>
          {items.map((item) => (
            <button key={item.label} style={{ ...itemBase, padding: '10px 12px' }}>
              <span>{item.icon}</span>
              <span>{item.label}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

function SidebarWithBadges() {
  const itemsWithBadges = [
    { label: 'Inbox', icon: '📬', badge: '12' },
    { label: 'Orders', icon: '📦', badge: '3' },
    { label: 'Returns', icon: '↩️', badge: '1' },
    { label: 'Messages', icon: '💬', badge: '5' },
    { label: 'Reports', icon: '📊' },
  ];
  const [active, setActive] = useState(0);
  return (
    <div style={{ ...sidebarBase, width: '240px', height: '360px', padding: '8px' }}>
      <div style={{ padding: '12px', fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-base)', borderBottom: '1px solid var(--color-border-default)', marginBottom: '8px' }}>SporeKart</div>
      {itemsWithBadges.map((item, i) => (
        <button key={item.label} style={i === active ? activeItem : itemBase} onClick={() => setActive(i)}>
          <span>{item.icon}</span>
          <span style={{ flex: 1 }}>{item.label}</span>
          {item.badge && <span style={badgeStyle}>{item.badge}</span>}
        </button>
      ))}
    </div>
  );
}

export default function SidebarPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Sidebar Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All sidebar variants and states</p>
      </div>

      <Section label="Variants">
        <PreviewBox label="Full sidebar with nested navigation">
          <FullSidebar />
        </PreviewBox>
        <PreviewBox label="Mini sidebar (icons only)">
          <MiniSidebar />
        </PreviewBox>
        <PreviewBox label="Collapsible sidebar">
          <CollapsibleSidebar />
        </PreviewBox>
        <PreviewBox label="With pinned items">
          <SidebarWithPinned />
        </PreviewBox>
        <PreviewBox label="Mobile overlay mode">
          <MobileSidebar />
        </PreviewBox>
        <PreviewBox label="With groups and badges">
          <SidebarWithBadges />
        </PreviewBox>
      </Section>

      <Section label="States">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: '16px' }}>
          <PreviewBox label="Default">
            <FullSidebar />
          </PreviewBox>
          <PreviewBox label="Collapsed">
            <MiniSidebar />
          </PreviewBox>
          <PreviewBox label="With active item">
            <FullSidebar />
          </PreviewBox>
        </div>
      </Section>
    </div>
  );
}
