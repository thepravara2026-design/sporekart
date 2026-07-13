import { useState } from 'react';

const UserIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><circle cx="8" cy="5" r="3" stroke="currentColor" strokeWidth="1.5"/><path d="M2 14c0-3.31 2.69-6 6-6s6 2.69 6 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);
const BellIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 1a5 5 0 00-5 5c0 4-2 5-2 5h14s-2-1-2-5a5 5 0 00-5-5z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/><path d="M6.5 12a1.5 1.5 0 003 0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);
const ShieldIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 1l6 2v5c0 3.5-6 6-6 6s-6-2.5-6-6V3l6-2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const MailIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="1" y="3" width="14" height="10" rx="1" stroke="currentColor" strokeWidth="1.5"/><path d="M1 4l7 5 7-5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const ClockIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.5"/><path d="M8 4v4l3 2" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const SearchIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><circle cx="7" cy="7" r="5" stroke="currentColor" strokeWidth="1.5"/><path d="M11 11l3.5 3.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);
const FilterIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M1 3h14M3 8h10M6 13h4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);
const ShoppingCartIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M1 2h2l1 8h9l2-6H4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/><circle cx="6" cy="13" r="1" fill="currentColor"/><circle cx="12" cy="13" r="1" fill="currentColor"/></svg>);
const PackageIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 1L2 4v8l6 3 6-3V4L8 1z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/><path d="M2 4l6 3 6-3M8 7v8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const ZapIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M7 1L2 9h5l-1 6 6-8H7l2-6H7z" fill="currentColor"/></svg>);
const AlertIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.5"/><path d="M8 5v3M8 11h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);
const ImageIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="1" y="2" width="14" height="12" rx="1" stroke="currentColor" strokeWidth="1.5"/><circle cx="5" cy="6" r="1.5" fill="currentColor"/><path d="M1 12l4-3 3 2 3-4 4 5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const PlayIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M4 2l10 6-10 6V2z" fill="currentColor"/></svg>);

const listItems = [
  'Apple', 'Banana', 'Cherry', 'Date', 'Elderberry', 'Fig', 'Grape', 'Honeydew', 'Kiwi', 'Lemon',
];

const iconItems = [
  { icon: <UserIcon />, label: 'Profile Settings' },
  { icon: <BellIcon />, label: 'Notifications' },
  { icon: <ShieldIcon />, label: 'Security' },
  { icon: <MailIcon />, label: 'Messages' },
  { icon: <ClockIcon />, label: 'Activity Log' },
  { icon: <SearchIcon />, label: 'Search' },
  { icon: <FilterIcon />, label: 'Filters' },
  { icon: <ShoppingCartIcon />, label: 'Cart' },
  { icon: <PackageIcon />, label: 'Orders' },
  { icon: <ZapIcon />, label: 'Quick Actions' },
];

const descItems = [
  { term: 'Full Name', desc: 'John Michael Doe' },
  { term: 'Email', desc: 'john.doe@example.com' },
  { term: 'Phone', desc: '+1 (555) 123-4567' },
  { term: 'Department', desc: 'Engineering' },
  { term: 'Role', desc: 'Senior Developer' },
  { term: 'Location', desc: 'San Francisco, CA' },
  { term: 'Timezone', desc: 'America/Los_Angeles' },
  { term: 'Employee ID', desc: 'EMP-2024-0842' },
  { term: 'Start Date', desc: 'January 15, 2022' },
  { term: 'Manager', desc: 'Sarah Connor' },
];

const mediaItems = [
  { title: 'Sunset Over Mountains', subtitle: 'Landscape · 2.4 MB', media: <ImageIcon /> },
  { title: 'Product Demo Video', subtitle: 'MP4 · 24 MB', media: <PlayIcon /> },
  { title: 'Annual Report 2025', subtitle: 'PDF · 3.1 MB', media: <ImageIcon /> },
  { title: 'Team Photo', subtitle: 'JPEG · 1.8 MB', media: <ImageIcon /> },
  { title: 'Presentation Deck', subtitle: 'PPTX · 12 MB', media: <ImageIcon /> },
];

export default function ListsPreview() {
  const [selectedItems, setSelectedItems] = useState<Set<number>>(new Set());
  const [_loading, _setLoading] = useState(false);
  const [_error, _setError] = useState(false);
  const [interactiveSelected, setInteractiveSelected] = useState<number | null>(null);

  const toggleSelect = (i: number) => {
    const next = new Set(selectedItems);
    if (next.has(i)) next.delete(i); else next.add(i);
    setSelectedItems(next);
  };

  const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
    <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '16px' }}>{children}</div>
    </section>
  );

  const ListCard = ({ label, children }: { label: string; children: React.ReactNode }) => (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginBottom: '4px' }}>{label}</span>
      {children}
    </div>
  );

  const listItemStyle = (i: number): React.CSSProperties => ({
    display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 0',
    borderBottom: i < listItems.length - 1 ? '1px solid var(--color-border-subtle, #e5e7eb)' : 'none',
    cursor: 'pointer', fontSize: 'var(--text-sm)',
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Lists</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All list variants</p>
      </div>

      <Section label="Simple List">
        <ListCard label="10 items">
          {listItems.map((item, i) => (
            <div key={i} style={listItemStyle(i)}>{item}</div>
          ))}
        </ListCard>
      </Section>

      <Section label="Icon List">
        <ListCard label="With inline SVG icons">
          {iconItems.map((item, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '8px 0', borderBottom: i < iconItems.length - 1 ? '1px solid var(--color-border-subtle, #e5e7eb)' : 'none', fontSize: 'var(--text-sm)' }}>
              <div style={{ color: 'var(--color-text-secondary)', flexShrink: 0, width: 16, height: 16 }}>{item.icon}</div>
              <span>{item.label}</span>
            </div>
          ))}
        </ListCard>
      </Section>

      <Section label="Description List">
        <ListCard label="10 items with descriptions">
          {descItems.map((item, i) => (
            <div key={i} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: i < descItems.length - 1 ? '1px solid var(--color-border-subtle, #e5e7eb)' : 'none', fontSize: 'var(--text-sm)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{item.term}</span>
              <span>{item.desc}</span>
            </div>
          ))}
        </ListCard>
      </Section>

      <Section label="Media List">
        <ListCard label="With placeholder media">
          {mediaItems.map((item, i) => (
            <div key={i} style={{ display: 'flex', gap: '12px', alignItems: 'center', padding: '8px 0', borderBottom: i < mediaItems.length - 1 ? '1px solid var(--color-border-subtle, #e5e7eb)' : 'none' }}>
              <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-subtle, #f3f4f6)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)', flexShrink: 0 }}>{item.media}</div>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 'var(--text-sm)', fontWeight: 'var(--weight-semibold)' }}>{item.title}</div>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{item.subtitle}</span>
              </div>
            </div>
          ))}
        </ListCard>
      </Section>

      <Section label="Interactive List">
        <ListCard label="Clickable with selection">
          {listItems.map((item, i) => (
            <div key={i} onClick={() => setInteractiveSelected(i)} style={{ ...listItemStyle(i), cursor: 'pointer', background: interactiveSelected === i ? 'var(--color-bg-primary-subtle, #eff6ff)' : 'transparent', borderRadius: 'var(--radius-sm)', padding: '8px', margin: '0 -8px' }}>
              {item}
            </div>
          ))}
        </ListCard>
      </Section>

      <Section label="Divided List">
        <ListCard label="With dividers">
          {listItems.map((item, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 0', borderBottom: '1px solid var(--color-border-default)', fontSize: 'var(--text-sm)' }}>
              {item}
            </div>
          ))}
        </ListCard>
      </Section>

      <Section label="Loading State">
        <ListCard label="Skeleton items">
          {Array.from({ length: 5 }).map((_, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '8px 0' }}>
              <div style={{ width: 16, height: 16, borderRadius: '50%', background: 'var(--color-bg-subtle, #f3f4f6)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
              <div style={{ flex: 1, height: 14, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: '4px', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            </div>
          ))}
        </ListCard>
      </Section>

      <Section label="Empty State">
        <ListCard label="No items">
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px', padding: '24px', textAlign: 'center' }}>
            <div style={{ color: 'var(--color-text-tertiary)' }}><SearchIcon /></div>
            <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>No items found</span>
          </div>
        </ListCard>
      </Section>

      <Section label="Error State">
        <ListCard label="With retry">
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px', padding: '24px', textAlign: 'center' }}>
            <div style={{ color: 'var(--color-danger, #dc2626)' }}><AlertIcon /></div>
            <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>Failed to load list</span>
            <button onClick={() => {}} style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-sm)', padding: '6px 12px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>Retry</button>
          </div>
        </ListCard>
      </Section>

      <Section label="Selectable List">
        <ListCard label="Toggle selection">
          {listItems.map((item, i) => (
            <div key={i} onClick={() => toggleSelect(i)} style={{ ...listItemStyle(i), cursor: 'pointer', background: selectedItems.has(i) ? 'var(--color-bg-primary-subtle, #eff6ff)' : 'transparent', borderRadius: 'var(--radius-sm)', padding: '8px', margin: '0 -8px' }}>
              <input type="checkbox" checked={selectedItems.has(i)} onChange={() => toggleSelect(i)} style={{ cursor: 'pointer' }} />
              {item}
            </div>
          ))}
        </ListCard>
      </Section>
    </div>
  );
}
