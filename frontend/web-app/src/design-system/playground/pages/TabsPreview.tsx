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

const tabsRow: React.CSSProperties = {
  display: 'flex',
  borderBottom: '1px solid var(--color-border-default)',
  fontFamily: 'var(--font-family)',
};

const tabBase: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '6px',
  padding: '10px 16px',
  fontSize: 'var(--text-sm)',
  fontWeight: 'var(--weight-medium)',
  cursor: 'pointer',
  border: 'none',
  background: 'transparent',
  color: 'var(--color-text-secondary)',
  position: 'relative',
  whiteSpace: 'nowrap',
};

const tabActive: React.CSSProperties = {
  ...tabBase,
  color: 'var(--color-text-primary)',
  fontWeight: 'var(--weight-semibold)',
};

const tabIndicator: React.CSSProperties = {
  position: 'absolute',
  bottom: '-1px',
  left: 0,
  right: 0,
  height: '2px',
  background: 'var(--color-bg-primary-default)',
  borderRadius: '1px 1px 0 0',
};

const tabPanel: React.CSSProperties = {
  padding: '16px 0',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-secondary)',
};

interface Tab {
  label: string;
  badge?: string | number;
  disabled?: boolean;
}

const standardTabs: Tab[] = [
  { label: 'Overview' },
  { label: 'Details' },
  { label: 'Settings' },
  { label: 'Activity' },
];

function StandardTabs() {
  const [active, setActive] = useState(0);
  return (
    <div>
      <div style={tabsRow}>
        {standardTabs.map((tab, i) => (
          <button
            key={tab.label}
            onClick={() => setActive(i)}
            style={i === active ? tabActive : tabBase}
          >
            {tab.label}
            {i === active && <div style={tabIndicator} />}
          </button>
        ))}
      </div>
      <div style={tabPanel}>{standardTabs[active].label} content panel</div>
    </div>
  );
}

function ScrollableTabs() {
  const [active, setActive] = useState(0);
  const manyTabs = ['Tab 1', 'Tab 2', 'Tab 3', 'Tab 4', 'Tab 5', 'Tab 6', 'Tab 7', 'Tab 8'];
  return (
    <div style={{ overflowX: 'auto' }}>
      <div style={{ ...tabsRow, width: 'max-content' }}>
        {manyTabs.map((tab, i) => (
          <button
            key={tab}
            onClick={() => setActive(i)}
            style={i === active ? tabActive : tabBase}
          >
            {tab}
            {i === active && <div style={tabIndicator} />}
          </button>
        ))}
      </div>
      <div style={tabPanel}>{manyTabs[active]} content</div>
    </div>
  );
}

function VerticalTabs() {
  const [active, setActive] = useState(0);
  return (
    <div style={{ display: 'flex', gap: '16px', minHeight: '160px' }}>
      <div style={{ display: 'flex', flexDirection: 'column', borderRight: '1px solid var(--color-border-default)', paddingRight: '4px' }}>
        {standardTabs.map((tab, i) => (
          <button
            key={tab.label}
            onClick={() => setActive(i)}
            style={{
              ...tabBase,
              borderRadius: 'var(--radius-sm)',
              padding: '8px 16px',
              background: i === active ? 'var(--color-bg-subtle)' : 'transparent',
              color: i === active ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
              fontWeight: i === active ? 'var(--weight-semibold)' : 'var(--weight-medium)',
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>
      <div style={tabPanel}>{standardTabs[active].label} content</div>
    </div>
  );
}

function SegmentedTabs() {
  const [active, setActive] = useState(0);
  const items = ['Day', 'Week', 'Month', 'Year'];
  return (
    <div style={{ display: 'flex', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-md)', padding: '2px', fontFamily: 'var(--font-family)' }}>
      {items.map((item, i) => (
        <button
          key={item}
          onClick={() => setActive(i)}
          style={{
            flex: 1,
            padding: '6px 16px',
            fontSize: 'var(--text-sm)',
            fontWeight: 'var(--weight-medium)',
            border: 'none',
            borderRadius: 'var(--radius-sm)',
            cursor: 'pointer',
            background: i === active ? 'var(--color-bg-surface-default)' : 'transparent',
            color: i === active ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
            boxShadow: i === active ? 'var(--shadow-1)' : 'none',
            transition: 'all 0.15s',
          }}
        >
          {item}
        </button>
      ))}
    </div>
  );
}

function ClosableTabs() {
  const [tabs, setTabs] = useState<Tab[]>([
    { label: 'Document 1' },
    { label: 'Document 2' },
    { label: 'Document 3' },
  ]);
  const [active, setActive] = useState(0);

  const closeTab = (idx: number) => {
    const newTabs = tabs.filter((_, i) => i !== idx);
    setTabs(newTabs);
    if (active >= newTabs.length) setActive(newTabs.length - 1);
  };

  return (
    <div>
      <div style={tabsRow}>
        {tabs.map((tab, i) => (
          <div
            key={tab.label}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
              padding: '8px 12px',
              fontSize: 'var(--text-sm)',
              cursor: 'pointer',
              borderBottom: i === active ? '2px solid var(--color-bg-primary-default)' : '2px solid transparent',
              color: i === active ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
              fontWeight: i === active ? 'var(--weight-semibold)' : 'var(--weight-medium)',
            }}
            onClick={() => setActive(i)}
          >
            {tab.label}
            <button
              onClick={(e) => { e.stopPropagation(); closeTab(i); }}
              style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 0, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-xs)', lineHeight: 1 }}
            >
              ✕
            </button>
          </div>
        ))}
      </div>
      <div style={tabPanel}>{tabs[active]?.label} content</div>
    </div>
  );
}

function TabsWithBadges() {
  const [active, setActive] = useState(0);
  const tabsWithBadges: Tab[] = [
    { label: 'Inbox', badge: 12 },
    { label: 'Sent', badge: 3 },
    { label: 'Drafts', badge: 2 },
    { label: 'Spam' },
  ];
  return (
    <div>
      <div style={tabsRow}>
        {tabsWithBadges.map((tab, i) => (
          <button
            key={tab.label}
            onClick={() => setActive(i)}
            style={i === active ? tabActive : tabBase}
          >
            {tab.label}
            {tab.badge !== undefined && (
              <span style={{
                fontSize: 'var(--text-xs)',
                padding: '0 6px',
                height: '18px',
                borderRadius: 'var(--radius-full)',
                background: 'var(--color-bg-primary-default)',
                color: '#fff',
                display: 'flex',
                alignItems: 'center',
                fontWeight: 'var(--weight-medium)',
              }}>
                {tab.badge}
              </span>
            )}
            {i === active && <div style={tabIndicator} />}
          </button>
        ))}
      </div>
      <div style={tabPanel}>{tabsWithBadges[active].label} content</div>
    </div>
  );
}

function DisabledTabs() {
  const [active, setActive] = useState(0);
  const tabs: Tab[] = [
    { label: 'Active' },
    { label: 'Disabled' },
    { label: 'Active 2' },
    { label: 'Locked' },
  ];
  return (
    <div>
      <div style={tabsRow}>
        {tabs.map((tab, i) => (
          <button
            key={tab.label}
            disabled={tab.disabled}
            onClick={() => setActive(i)}
            style={{
              ...(i === active ? tabActive : tabBase),
              opacity: tab.disabled ? 0.4 : 1,
              cursor: tab.disabled ? 'not-allowed' : 'pointer',
            }}
          >
            {tab.label}
            {i === active && !tab.disabled && <div style={tabIndicator} />}
          </button>
        ))}
      </div>
      <div style={tabPanel}>{tabs[active].label} content</div>
    </div>
  );
}

export default function TabsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Tabs Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All tab variants</p>
      </div>

      <Section label="Standard Tabs">
        <PreviewBox label="3-5 tabs with active indicator">
          <StandardTabs />
        </PreviewBox>
      </Section>

      <Section label="Scrollable Tabs">
        <PreviewBox label="Many tabs showing scroll behavior">
          <ScrollableTabs />
        </PreviewBox>
      </Section>

      <Section label="Vertical Tabs">
        <PreviewBox label="Stacked tabs">
          <VerticalTabs />
        </PreviewBox>
      </Section>

      <Section label="Segmented Tabs">
        <PreviewBox label="Button-like toggle">
          <SegmentedTabs />
        </PreviewBox>
      </Section>

      <Section label="Closable Tabs">
        <PreviewBox label="With X buttons">
          <ClosableTabs />
        </PreviewBox>
      </Section>

      <Section label="With Badges">
        <PreviewBox label="Count badges on tabs">
          <TabsWithBadges />
        </PreviewBox>
      </Section>

      <Section label="Disabled Tabs">
        <PreviewBox label="Disabled state">
          <DisabledTabs />
        </PreviewBox>
      </Section>
    </div>
  );
}
