import { NavLink } from 'react-router-dom';
import { PUBLIC_PRIMARY_NAV, type PublicNavItem } from './config';

export function PublicNav({ orientation = 'horizontal' }: { orientation?: 'horizontal' | 'vertical' }) {
  const listStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: orientation === 'vertical' ? 'column' : 'row',
    gap: orientation === 'vertical' ? 'var(--space-2, 8px)' : 'var(--space-5, 24px)',
    listStyle: 'none',
    margin: 0,
    padding: 0,
  };

  const linkStyle = (active: boolean): React.CSSProperties => ({
    display: 'inline-flex',
    alignItems: 'center',
    fontFamily: 'var(--font-family-sans, system-ui)',
    fontSize: 'var(--text-body-sm, 14px)',
    fontWeight: active ? 600 : 500,
    color: active ? 'var(--color-text-accent, #1d4ed8)' : 'var(--color-text-primary, #1f2933)',
    textDecoration: 'none',
    padding: 'var(--space-2, 8px) var(--space-1, 4px)',
    borderRadius: 'var(--radius-sm, 6px)',
    transition: 'color 120ms ease, background-color 120ms ease',
  });

  return (
    <nav className="sk-public-nav" aria-label="Primary">
      <ul style={listStyle}>
        {PUBLIC_PRIMARY_NAV.map((item: PublicNavItem) => (
          <li key={item.href}>
            <NavLink
              to={item.href}
              end={item.href === '/'}
              style={({ isActive }) => linkStyle(isActive)}
            >
              {item.label}
            </NavLink>
          </li>
        ))}
      </ul>
    </nav>
  );
}
