import { Link } from 'react-router-dom';

interface NavCardProps {
  path: string;
  label: string;
  description: string;
  icon: React.ReactNode;
}

const cardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '8px',
  padding: '20px',
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  boxShadow: 'var(--shadow-1)',
  textDecoration: 'none',
  color: 'inherit',
  border: '1px solid var(--color-border-default)',
  transition: 'box-shadow 0.2s, transform 0.2s',
};

const iconBoxStyle: React.CSSProperties = {
  width: '40px',
  height: '40px',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  borderRadius: 'var(--radius-md)',
  background: 'var(--color-bg-subtle)',
  color: 'var(--color-text-primary)',
};

function NavCard({ path, label, description, icon }: NavCardProps) {
  const linkStyle: React.CSSProperties = {
    ...cardStyle,
    cursor: 'pointer',
  };
  return (
    <Link to={path} style={linkStyle}>
      <div style={iconBoxStyle}>{icon}</div>
      <span style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-semibold)' }}>{label}</span>
      <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', lineHeight: '1.5' }}>{description}</span>
    </Link>
  );
}

const HeaderIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <rect x="2" y="4" width="16" height="12" rx="2" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M2 8h16M7 4v12" stroke="currentColor" strokeWidth="1.5"/>
  </svg>
);

const SidebarIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <rect x="2" y="2" width="16" height="16" rx="2" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M8 2v16" stroke="currentColor" strokeWidth="1.5"/>
  </svg>
);

const BreadcrumbIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <path d="M2 10h12M10 6l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const MenuIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <circle cx="10" cy="5" r="1.5" fill="currentColor"/>
    <circle cx="10" cy="10" r="1.5" fill="currentColor"/>
    <circle cx="10" cy="15" r="1.5" fill="currentColor"/>
  </svg>
);

const TabsIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <path d="M2 14V6a2 2 0 012-2h12a2 2 0 012 2v8a2 2 0 01-2 2H4a2 2 0 01-2-2z" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M2 10h16" stroke="currentColor" strokeWidth="1.5"/>
  </svg>
);

const PaginationIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <path d="M12 5l-5 5 5 5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const StepperIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <circle cx="4" cy="10" r="2" fill="currentColor"/>
    <circle cx="10" cy="10" r="2" fill="currentColor" opacity="0.5"/>
    <circle cx="16" cy="10" r="2" fill="currentColor" opacity="0.3"/>
    <path d="M6 10h4M12 10h4" stroke="currentColor" strokeWidth="1" opacity="0.3"/>
  </svg>
);

const LayoutsIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <rect x="2" y="2" width="16" height="16" rx="2" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M2 6h16M2 14h16M7 6v12" stroke="currentColor" strokeWidth="1.5"/>
  </svg>
);

const PaletteIcon = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
    <path d="M10 2a8 8 0 100 16 4 4 0 014-4h1a3 3 0 000-6h-1a4 4 0 01-4-4z" stroke="currentColor" strokeWidth="1.5"/>
    <circle cx="6.5" cy="7.5" r="1" fill="currentColor"/>
    <circle cx="13.5" cy="7.5" r="1" fill="currentColor"/>
  </svg>
);

export default function NavigationIndex() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Navigation &amp; Layout System</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Sprint 20 Part 5 — preview pages for all navigation components and layout templates</p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '16px' }}>
        <NavCard path="/design-system/header" label="Header Preview" description="Primary, secondary, compact, transparent, and sticky header variants with responsive behavior." icon={<HeaderIcon />} />
        <NavCard path="/design-system/sidebar" label="Sidebar Preview" description="Full, mini, collapsible sidebar with pinned items, mobile overlay, groups, and badges." icon={<SidebarIcon />} />
        <NavCard path="/design-system/breadcrumb" label="Breadcrumb Preview" description="Simple, deep, icon-enhanced breadcrumbs with responsive collapse." icon={<BreadcrumbIcon />} />
        <NavCard path="/design-system/menu" label="Menu Preview" description="Dropdown, context, overflow, user, and action menus with all states." icon={<MenuIcon />} />
        <NavCard path="/design-system/tabs" label="Tabs Preview" description="Standard, scrollable, vertical, segmented, closable tabs with badges and panels." icon={<TabsIcon />} />
        <NavCard path="/design-system/pagination" label="Pagination Preview" description="Standard, compact pagination with page size selector, jump input, and responsive collapse." icon={<PaginationIcon />} />
        <NavCard path="/design-system/stepper" label="Stepper Preview" description="Horizontal, vertical, progress bar, clickable steppers with completed/active/pending states." icon={<StepperIcon />} />
        <NavCard path="/design-system/layouts" label="Layouts Preview" description="Public, authenticated, dashboard, content, split, centered, blank, error layout templates." icon={<LayoutsIcon />} />
        <NavCard path="/design-system/command-palette" label="Command Palette Preview" description="Modal overlay with search, command groups, filtered results, keyboard shortcut hint." icon={<PaletteIcon />} />
      </div>
    </div>
  );
}
