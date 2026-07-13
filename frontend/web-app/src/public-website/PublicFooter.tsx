import { Link as RouterLink } from 'react-router-dom';
import { Icon } from '../design-system/icons/Icon';
import { PUBLIC_FOOTER_NAV, SITE_NAME, SITE_TAGLINE } from './config';

export function PublicFooter() {
  const columns: { title: string; items: { label: string; href: string }[] }[] = [
    {
      title: 'Explore',
      items: PUBLIC_FOOTER_NAV.filter((item) =>
        ['Products', 'Training', 'Blog', 'About', 'FAQ', 'Certifications'].includes(item.label),
      ),
    },
    {
      title: 'Company',
      items: PUBLIC_FOOTER_NAV.filter((item) =>
        ['Contact', 'Privacy Policy', 'Terms & Conditions'].includes(item.label),
      ),
    },
    {
      title: 'Policies',
      items: PUBLIC_FOOTER_NAV.filter((item) =>
        ['Refund Policy', 'Shipping Policy', 'Sign In'].includes(item.label),
      ),
    },
  ];

  const footerStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-green-900, #153a26)',
    borderTop: '1px solid rgba(255,255,255,0.14)',
    color: 'rgba(255,255,255,0.78)',
    padding: 'var(--space-8, 48px) var(--space-5, 24px) var(--space-5, 24px)',
    fontFamily: 'var(--font-family-sans, system-ui)',
  };

  const linkStyle: React.CSSProperties = {
    color: 'rgba(255,255,255,0.72)',
    textDecoration: 'none',
    fontSize: 'var(--text-body-sm, 14px)',
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-1, 4px)',
  };

  return (
    <footer className="sk-public-footer" role="contentinfo" style={footerStyle}>
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))',
          gap: 'var(--space-6, 32px)',
          maxWidth: 'var(--container-xl, 1200px)',
          margin: '0 auto',
        }}
      >
        <div style={{ minWidth: 220 }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontWeight: 700, color: 'var(--color-text-inverse, #ffffff)', fontSize: 'var(--text-title-md, 18px)' }}>
            <span aria-hidden="true" style={{ display: 'inline-flex', width: 28, height: 28, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', background: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}>
              ❖
            </span>
            {SITE_NAME}
          </div>
          <p style={{ marginTop: 'var(--space-3, 12px)', fontSize: 'var(--text-body-sm, 14px)', maxWidth: 260 }}>{SITE_TAGLINE}</p>
          <div style={{ display: 'flex', gap: 'var(--space-3, 12px)', marginTop: 'var(--space-4, 16px)' }}>
            <a href="https://example.com" aria-label="Email" style={linkStyle}><Icon name="mail" size={18} aria-label="Email" /></a>
            <a href="https://example.com" aria-label="Phone" style={linkStyle}><Icon name="phone" size={18} aria-label="Phone" /></a>
            <a href="https://example.com" aria-label="Location" style={linkStyle}><Icon name="map-pin" size={18} aria-label="Location" /></a>
          </div>
        </div>

        {columns.map((column) => (
          <nav key={column.title} aria-label={column.title} style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
            <h2 style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 700, color: 'var(--color-text-inverse, #ffffff)', margin: '0 0 var(--space-2, 8px)' }}>{column.title}</h2>
            {column.items.map((item) => (
              <RouterLink key={item.href} to={item.href} style={linkStyle}>
                {item.label}
              </RouterLink>
            ))}
          </nav>
        ))}
      </div>

      <div
        style={{
          maxWidth: 'var(--container-xl, 1200px)',
          margin: 'var(--space-6, 32px) auto 0',
          paddingTop: 'var(--space-4, 16px)',
          borderTop: '1px solid rgba(255,255,255,0.14)',
          display: 'flex',
          flexWrap: 'wrap',
          gap: 'var(--space-3, 12px)',
          justifyContent: 'space-between',
          fontSize: 'var(--text-body-xs, 12px)',
        }}
      >
        <span>© {new Date().getFullYear()} {SITE_NAME}. All rights reserved.</span>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)' }}>
          <Icon name="shield" size={16} aria-label="Secure" /> Secure & compliant
        </span>
      </div>
    </footer>
  );
}
