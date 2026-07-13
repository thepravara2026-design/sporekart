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

const crumbBase: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '4px',
  fontFamily: 'var(--font-family)',
  fontSize: 'var(--text-sm)',
};

const crumbLink: React.CSSProperties = {
  color: 'var(--color-text-link)',
  cursor: 'pointer',
  textDecoration: 'none',
};

const crumbCurrent: React.CSSProperties = {
  color: 'var(--color-text-primary)',
  fontWeight: 'var(--weight-semibold)',
};

const separatorStyle: React.CSSProperties = {
  color: 'var(--color-text-tertiary)',
  margin: '0 4px',
};

const ChevronSep = () => <span style={separatorStyle}>›</span>;
const SlashSep = () => <span style={separatorStyle}>/</span>;

interface Crumb {
  label: string;
  href?: string;
}

function Breadcrumb({ items, separator }: { items: Crumb[]; separator?: 'chevron' | 'slash' }) {
  const Sep = separator === 'slash' ? SlashSep : ChevronSep;
  return (
    <nav style={crumbBase}>
      {items.map((item, i) => {
        const isLast = i === items.length - 1;
        return (
          <span key={i} style={{ display: 'flex', alignItems: 'center' }}>
            {i > 0 && <Sep />}
            {item.href ? (
              <a href={item.href} style={crumbLink}>{item.label}</a>
            ) : (
              <span style={isLast ? crumbCurrent : { color: 'var(--color-text-secondary)' }}>{item.label}</span>
            )}
          </span>
        );
      })}
    </nav>
  );
}

const HomeIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style={{ marginRight: '4px' }}>
    <path d="M2 7l5-5 5 5M4 6v5h2V8h2v3h2V6" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const FolderIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M1 4.5A1.5 1.5 0 012.5 3h2.5l1 2h4.5A1.5 1.5 0 0112 6.5v4A1.5 1.5 0 0110.5 12h-8A1.5 1.5 0 011 10.5v-6z" stroke="currentColor" strokeWidth="1.3"/>
  </svg>
);

function IconBreadcrumb({ items }: { items: { icon?: React.ReactNode; label: string; href?: string }[] }) {
  return (
    <nav style={crumbBase}>
      {items.map((item, i) => {
        const isLast = i === items.length - 1;
        return (
          <span key={i} style={{ display: 'flex', alignItems: 'center' }}>
            {i > 0 && <ChevronSep />}
            {item.icon}
            {item.href ? (
              <a href={item.href} style={crumbLink}>{item.label}</a>
            ) : (
              <span style={isLast ? crumbCurrent : { color: 'var(--color-text-secondary)' }}>{item.label}</span>
            )}
          </span>
        );
      })}
    </nav>
  );
}

function ResponsiveBreadcrumb() {
  const [collapsed, setCollapsed] = useState(false);
  const allItems: Crumb[] = [
    { label: 'Home', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Electronics', href: '/products/electronics' },
    { label: 'Laptops', href: '/products/electronics/laptops' },
    { label: 'ThinkPad X1' },
  ];

  const displayItems = collapsed
    ? [allItems[0], allItems[allItems.length - 1]]
    : allItems;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <Breadcrumb items={displayItems} />
      <button
        onClick={() => setCollapsed(!collapsed)}
        style={{ alignSelf: 'flex-start', fontSize: 'var(--text-xs)', cursor: 'pointer', border: 'none', background: 'none', color: 'var(--color-text-link)', padding: 0 }}
      >
        {collapsed ? 'Show all (expand)' : 'Collapse (responsive)'}
      </button>
    </div>
  );
}

export default function BreadcrumbPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Breadcrumb Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All breadcrumb variants</p>
      </div>

      <Section label="Simple (2 levels)">
        <PreviewBox label="Chevron separator">
          <Breadcrumb items={[{ label: 'Home', href: '/' }, { label: 'Settings' }]} />
        </PreviewBox>
        <PreviewBox label="Slash separator">
          <Breadcrumb items={[{ label: 'Home', href: '/' }, { label: 'Settings' }]} separator="slash" />
        </PreviewBox>
      </Section>

      <Section label="Deep (5+ levels with ellipsis)">
        <PreviewBox label="Full depth navigation">
          <Breadcrumb items={[
            { label: 'Home', href: '/' },
            { label: 'Catalog', href: '/catalog' },
            { label: 'Categories', href: '/catalog/categories' },
            { label: 'Electronics', href: '/catalog/categories/electronics' },
            { label: 'Laptops' },
          ]} />
        </PreviewBox>
        <PreviewBox label="With ellipsis (intermediate collapsed)">
          <Breadcrumb items={[
            { label: 'Home', href: '/' },
            { label: '...' },
            { label: 'Laptops' },
          ]} />
        </PreviewBox>
      </Section>

      <Section label="With Icons">
        <PreviewBox label="Icons before each label">
          <IconBreadcrumb items={[
            { icon: <HomeIcon />, label: 'Home', href: '/' },
            { icon: <FolderIcon />, label: 'Documents', href: '/documents' },
            { icon: <FolderIcon />, label: 'Reports', href: '/documents/reports' },
            { label: 'Q4 Summary' },
          ]} />
        </PreviewBox>
      </Section>

      <Section label="With Current Page">
        <PreviewBox label="Last item bold, no link">
          <Breadcrumb items={[
            { label: 'Account', href: '/account' },
            { label: 'Orders', href: '/account/orders' },
            { label: 'Order #12345' },
          ]} />
        </PreviewBox>
      </Section>

      <Section label="Responsive Collapse">
        <PreviewBox label="Toggle show all / collapse">
          <ResponsiveBreadcrumb />
        </PreviewBox>
      </Section>
    </div>
  );
}
