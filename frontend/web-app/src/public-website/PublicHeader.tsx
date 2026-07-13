import { useEffect, useRef, useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { Icon } from '../design-system/icons/Icon';
import { PublicNav } from './PublicNav';
import { PUBLIC_PRIMARY_NAV } from './config';

export function PublicHeader() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const drawerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!mobileOpen) {
      return;
    }
    const onKey = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        setMobileOpen(false);
      }
    };
    const onClick = (event: MouseEvent) => {
      if (drawerRef.current && !drawerRef.current.contains(event.target as Node)) {
        setMobileOpen(false);
      }
    };
    document.addEventListener('keydown', onKey);
    document.addEventListener('mousedown', onClick);
    return () => {
      document.removeEventListener('keydown', onKey);
      document.removeEventListener('mousedown', onClick);
    };
  }, [mobileOpen]);

  const headerStyle: React.CSSProperties = {
    position: 'sticky',
    top: 0,
    zIndex: 'var(--z-header, 100)' as unknown as number,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 'var(--space-4, 16px)',
    height: 'var(--header-height, 64px)',
    padding: '0 var(--space-5, 24px)',
    backgroundColor: 'var(--color-green-900, #153a26)',
    borderBottom: '1px solid rgba(255,255,255,0.14)',
    boxShadow: 'var(--shadow-sm, 0 1px 2px rgba(0,0,0,0.05))',
  };

  return (
    <header className="sk-public-header" role="banner" style={headerStyle}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-4, 16px)' }}>
        <button
          type="button"
          className="sk-icon-btn sk-public-header__menu"
          aria-label="Open navigation menu"
          aria-expanded={mobileOpen}
          onClick={() => setMobileOpen((value) => !value)}
          style={{ display: 'inline-flex', border: 'none', background: 'transparent', cursor: 'pointer', padding: 'var(--space-2, 8px)' }}
        >
          <Icon name="menu" size={22} aria-label="Open navigation menu" />
        </button>

        <RouterLink to="/" className="sk-brand" aria-label="SporeKart home"           style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', textDecoration: 'none', color: 'var(--color-text-inverse, #ffffff)' }}>
          <span aria-hidden="true" style={{ display: 'inline-flex', width: 28, height: 28, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', background: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', fontWeight: 700 }}>
            ❖
          </span>
          <span style={{ fontFamily: 'var(--font-family-sans, system-ui)', fontWeight: 700, fontSize: 'var(--text-title-md, 18px)' }}>SporeKart</span>
        </RouterLink>
      </div>

      <div className="sk-public-header__nav" style={{ display: 'flex', alignItems: 'center' }}>
        <PublicNav orientation="horizontal" theme="dark" />
      </div>

      <div className="sk-public-header__actions" style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)' }}>
        <button
          type="button"
          className="sk-icon-btn"
          aria-label="Search"
            style={{ display: 'inline-flex', border: 'none', background: 'transparent', cursor: 'pointer', padding: 'var(--space-2, 8px)', color: 'var(--color-text-inverse, #ffffff)' }}
        >
          <Icon name="search" size={20} aria-label="Search" />
        </button>
        <RouterLink
          to="/auth"
          className="sk-btn sk-btn--primary sk-public-header__signin"
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: 'var(--space-2, 8px)',
            fontFamily: 'var(--font-family-sans, system-ui)',
            fontSize: 'var(--text-body-sm, 14px)',
            fontWeight: 600,
            textDecoration: 'none',
            padding: 'var(--space-2, 8px) var(--space-4, 16px)',
            borderRadius: 'var(--radius-md, 8px)',
            backgroundColor: 'var(--color-bg-background, #f7f8f7)',
            color: 'var(--color-green-900, #153a26)',
          }}
        >
          <Icon name="log-in" size={18} aria-label="Sign in" />
          Sign In
        </RouterLink>
      </div>

      {mobileOpen && (
        <div
          ref={drawerRef}
          className="sk-public-header__drawer"
          role="dialog"
          aria-label="Navigation"
          style={{
            position: 'absolute',
            top: 'var(--header-height, 64px)',
            left: 0,
            right: 0,
            backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
            borderBottom: '1px solid var(--color-border-default, #e5e7eb)',
            boxShadow: 'var(--shadow-md, 0 4px 12px rgba(0,0,0,0.08))',
            padding: 'var(--space-4, 16px)',
          }}
        >
          <PublicNav orientation="vertical" />
          <div style={{ marginTop: 'var(--space-4, 16px)', display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
            {PUBLIC_PRIMARY_NAV.map((item) => (
              <RouterLink
                key={item.href}
                to={item.href}
                onClick={() => setMobileOpen(false)}
                style={{ textDecoration: 'none', color: 'var(--color-text-primary, #1f2933)', fontWeight: 500, padding: 'var(--space-2, 8px) 0' }}
              >
                {item.label}
              </RouterLink>
            ))}
          </div>
        </div>
      )}
    </header>
  );
}
