import { useState } from 'react';

interface SectionProps {
  label: string;
  children: React.ReactNode;
}

const Section = ({ label, children }: SectionProps) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

interface PreviewBoxProps {
  label: string;
  children: React.ReactNode;
}

const boxStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '8px',
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  padding: '16px',
  boxShadow: 'var(--shadow-1)',
};

const PreviewBox = ({ label, children }: PreviewBoxProps) => (
  <div style={boxStyle}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const headerBase: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  width: '100%',
  padding: '0 16px',
  fontFamily: 'var(--font-family)',
};

const logoStyle: React.CSSProperties = {
  fontWeight: 'var(--weight-bold)',
  fontSize: 'var(--text-lg)',
  whiteSpace: 'nowrap',
};

const navItemStyle: React.CSSProperties = {
  fontSize: 'var(--text-sm)',
  color: 'var(--color-text-secondary)',
  cursor: 'pointer',
  padding: '4px 8px',
  borderRadius: 'var(--radius-sm)',
};

const actionBtnStyle: React.CSSProperties = {
  width: '32px',
  height: '32px',
  border: 'none',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-subtle)',
  cursor: 'pointer',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: 'var(--color-text-primary)',
};

const BurgerIcon = () => (
  <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
    <path d="M3 5h12M3 9h12M3 13h12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="7" cy="7" r="5" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M11 11l3 3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

const BellIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M8 14a2 2 0 01-2-2h4a2 2 0 01-2 2zm4-4V6a4 4 0 00-8 0v4l-1 2h10l-1-2z" stroke="currentColor" strokeWidth="1.5"/>
  </svg>
);

const UserIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <circle cx="8" cy="5" r="3" stroke="currentColor" strokeWidth="1.5"/>
    <path d="M2 14c0-3.314 2.686-6 6-6s6 2.686 6 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
  </svg>
);

function PrimaryHeader() {
  const navItems = ['Dashboard', 'Products', 'Orders', 'Analytics'];
  return (
    <div style={{ ...headerBase, height: '56px', background: 'var(--color-bg-primary-default)', color: '#fff', justifyContent: 'space-between' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
        <span style={logoStyle}>SporeKart</span>
        <div style={{ display: 'flex', gap: '4px' }}>
          {navItems.map((item) => (
            <span key={item} style={{ ...navItemStyle, color: 'hsla(0,0%,100%,0.8)' }}>{item}</span>
          ))}
        </div>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
        <button style={{ ...actionBtnStyle, background: 'hsla(0,0%,100%,0.15)', color: '#fff' }}><SearchIcon /></button>
        <button style={{ ...actionBtnStyle, background: 'hsla(0,0%,100%,0.15)', color: '#fff' }}><BellIcon /></button>
        <button style={{ ...actionBtnStyle, background: 'hsla(0,0%,100%,0.15)', color: '#fff' }}><UserIcon /></button>
      </div>
    </div>
  );
}

function SecondaryHeader() {
  return (
    <div style={{ ...headerBase, height: '48px', background: 'var(--color-bg-surface-default)', borderBottom: '1px solid var(--color-border-default)', justifyContent: 'space-between' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-base)' }}>Section Title</span>
        <span style={navItemStyle}>Subsection A</span>
        <span style={navItemStyle}>Subsection B</span>
      </div>
      <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Last updated: today</span>
      </div>
    </div>
  );
}

function CompactHeader() {
  return (
    <div style={{ ...headerBase, height: '40px', background: 'var(--color-bg-surface-default)', borderBottom: '1px solid var(--color-border-default)', justifyContent: 'space-between' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
        <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-sm)' }}>SK</span>
        <span style={{ ...navItemStyle, fontSize: 'var(--text-xs)' }}>Nav</span>
        <span style={{ ...navItemStyle, fontSize: 'var(--text-xs)' }}>Search</span>
      </div>
      <button style={{ ...actionBtnStyle, width: '28px', height: '28px' }}><UserIcon /></button>
    </div>
  );
}

function TransparentHeader() {
  const navItems = ['Home', 'About', 'Contact'];
  return (
    <div style={{ ...headerBase, height: '56px', background: 'transparent', justifyContent: 'space-between', borderBottom: '1px solid var(--color-border-default)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
        <span style={logoStyle}>SporeKart</span>
        <div style={{ display: 'flex', gap: '4px' }}>
          {navItems.map((item) => (
            <span key={item} style={navItemStyle}>{item}</span>
          ))}
        </div>
      </div>
      <button style={actionBtnStyle}><BurgerIcon /></button>
    </div>
  );
}

function StickyHeader() {
  return (
    <div style={{ ...headerBase, height: '48px', background: 'var(--color-bg-surface-default)', borderBottom: '1px solid var(--color-border-default)', justifyContent: 'space-between', position: 'sticky', top: '0', zIndex: 10 }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-base)' }}>Sticky Header</span>
        <span style={navItemStyle}>Scroll down ↓</span>
      </div>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>position: sticky</span>
    </div>
  );
}

function MobileHeader() {
  return (
    <div style={{ ...headerBase, height: '48px', background: 'var(--color-bg-primary-default)', color: '#fff', justifyContent: 'space-between', maxWidth: '375px' }}>
      <button style={{ ...actionBtnStyle, background: 'transparent', color: '#fff' }}><BurgerIcon /></button>
      <span style={{ ...logoStyle, fontSize: 'var(--text-base)' }}>SporeKart</span>
      <button style={{ ...actionBtnStyle, background: 'transparent', color: '#fff' }}><UserIcon /></button>
    </div>
  );
}

export default function HeaderPreview() {
  const [scrolled, setScrolled] = useState(false);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Header Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All header variants and states</p>
      </div>

      <Section label="Primary Header">
        <PreviewBox label="Default brand header with nav items and actions">
          <PrimaryHeader />
        </PreviewBox>
      </Section>

      <Section label="Secondary Header">
        <PreviewBox label="Section header with subsections">
          <SecondaryHeader />
        </PreviewBox>
      </Section>

      <Section label="Compact Header">
        <PreviewBox label="Condensed header for tight layouts">
          <CompactHeader />
        </PreviewBox>
      </Section>

      <Section label="Transparent Header">
        <PreviewBox label="No background, border separator">
          <TransparentHeader />
        </PreviewBox>
      </Section>

      <Section label="Sticky Header">
        <div style={boxStyle}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Scroll container to see sticky behavior</span>
            <button
              onClick={() => setScrolled(!scrolled)}
              style={{ fontSize: 'var(--text-sm)', cursor: 'pointer', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', padding: '4px 12px', background: 'var(--color-bg-surface-default)' }}
            >
              {scrolled ? 'Reset' : 'Scroll Down'}
            </button>
          </div>
          <div style={{ position: 'relative', height: '200px', overflow: 'auto', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)' }}>
            <div style={{ height: scrolled ? '300px' : '200px', transition: 'height 0.3s' }}>
              <StickyHeader />
              <div style={{ padding: '16px', fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>
                {scrolled
                  ? 'Header is now sticky at the top of this container. Keep scrolling to see it remain fixed.'
                  : 'Click "Scroll Down" to expand content and see the sticky header behavior.'}
              </div>
            </div>
          </div>
        </div>
      </Section>

      <Section label="Mobile Header">
        <PreviewBox label="Responsive compact view (375px)">
          <MobileHeader />
        </PreviewBox>
      </Section>

      <Section label="States">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '16px' }}>
          <PreviewBox label="Default state">
            <PrimaryHeader />
          </PreviewBox>
          <PreviewBox label="Scrolled state (shadow + condensed)">
            <div style={{ ...headerBase, height: '48px', background: 'var(--color-bg-surface-default)', boxShadow: 'var(--shadow-2)', justifyContent: 'space-between' }}>
              <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-base)' }}>SporeKart</span>
              <button style={actionBtnStyle}><UserIcon /></button>
            </div>
          </PreviewBox>
          <PreviewBox label="Mobile view (compact)">
            <MobileHeader />
          </PreviewBox>
        </div>
      </Section>
    </div>
  );
}
