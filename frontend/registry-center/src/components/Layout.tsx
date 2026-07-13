import { useState, type ReactNode } from 'react';
import { NavLink } from 'react-router-dom';

interface NavItem {
  to: string;
  label: string;
}

const NAV_ITEMS: NavItem[] = [
  { to: '/provider-registry', label: 'Providers' },
  { to: '/prompt-registry', label: 'Prompts' },
  { to: '/knowledge-registry', label: 'Knowledge' },
  { to: '/usage-tracking', label: 'Usage' },
  { to: '/config-registry', label: 'Config' },
  { to: '/event-catalog', label: 'Events' },
  { to: '/api-registry', label: 'APIs' },
  { to: '/capability-discovery', label: 'Capabilities' },
  { to: '/adr', label: 'ADRs' }
];

interface LayoutProps {
  children: ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const [open, setOpen] = useState(false);

  return (
    <div className="app-shell">
      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <div className="sidebar-brand">SporeKart Registry</div>
        <nav className="sidebar-nav">
          {NAV_ITEMS.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}
              onClick={() => setOpen(false)}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>
      <div
        className={open ? 'sidebar-overlay open' : 'sidebar-overlay'}
        onClick={() => setOpen(false)}
      />
      <div className="main">
        <header className="header">
          <button className="menu-btn" onClick={() => setOpen((v) => !v)} aria-label="Toggle menu">
            ☰
          </button>
          <span className="header-title">Registry Center</span>
        </header>
        <main className="content">{children}</main>
      </div>
    </div>
  );
}
