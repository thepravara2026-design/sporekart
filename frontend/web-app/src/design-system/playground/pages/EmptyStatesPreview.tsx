const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const EmptyStateCard = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const EmptyState = ({ icon, title, description, primaryAction, secondaryAction, compact }: {
  icon: React.ReactNode; title: string; description?: string; primaryAction?: string; secondaryAction?: string; compact?: boolean;
}) => (
  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: compact ? '8px' : '12px', padding: compact ? '24px 16px' : '40px 24px', textAlign: 'center' }}>
    <div style={{ color: 'var(--color-text-tertiary)', width: compact ? 32 : 48, height: compact ? 32 : 48 }}>{icon}</div>
    <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: compact ? 'var(--text-sm)' : 'var(--text-base)' }}>{title}</div>
    {description && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', maxWidth: 240 }}>{description}</span>}
    {primaryAction && <button style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-sm)', padding: '8px 16px', cursor: 'pointer', fontSize: 'var(--text-sm)', fontWeight: 'var(--weight-medium)' }}>{primaryAction}</button>}
    {secondaryAction && <button style={{ background: 'transparent', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', padding: '8px 16px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>{secondaryAction}</button>}
  </div>
);

export default function EmptyStatesPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Empty States</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All 8 empty state variants</p>
      </div>

      <Section label="Empty State Types">
        <EmptyStateCard label="No Data">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>} title="No data available" description="There are no records to display yet." />
        </EmptyStateCard>
        <EmptyStateCard label="No Results">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>} title="No results found" description="Try adjusting your search or filter criteria." />
        </EmptyStateCard>
        <EmptyStateCard label="Coming Soon">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>} title="Coming soon" description="This feature is under development and will be available shortly." />
        </EmptyStateCard>
        <EmptyStateCard label="Access Denied">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>} title="Access denied" description="You do not have permission to view this page." />
        </EmptyStateCard>
        <EmptyStateCard label="Offline">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><line x1="1" y1="1" x2="23" y2="23"/><path d="M16.72 11.06A10.94 10.94 0 0119 12.55"/><path d="M5 12.55a10.94 10.94 0 015.17-2.39"/><path d="M10.71 5.05A16 16 0 0122.56 9"/><path d="M1.42 9a15.91 15.91 0 014.7-2.88"/><path d="M8.53 16.11a6 6 0 016.95 0"/><line x1="12" y1="20" x2="12.01" y2="20"/></svg>} title="You are offline" description="Check your internet connection and try again." />
        </EmptyStateCard>
        <EmptyStateCard label="Maintenance">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg>} title="Under maintenance" description="We are performing scheduled maintenance. Please check back later." />
        </EmptyStateCard>
        <EmptyStateCard label="Search Empty">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>} title="No matching results" description="Try different keywords or adjust your filters." />
        </EmptyStateCard>
        <EmptyStateCard label="Filter Empty">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></svg>} title="No items match your filters" description="Try clearing or changing your filters." />
        </EmptyStateCard>
      </Section>

      <Section label="With Actions">
        <EmptyStateCard label="Primary action only">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>} title="No products yet" description="Start adding products to your catalog." primaryAction="Add Product" />
        </EmptyStateCard>
        <EmptyStateCard label="Primary + secondary actions">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>} title="No orders found" description="Create your first order to get started." primaryAction="New Order" secondaryAction="Learn More" />
        </EmptyStateCard>
      </Section>

      <Section label="Compact Mode">
        <EmptyStateCard label="Compact variant">
          <EmptyState icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>} title="No results" compact />
        </EmptyStateCard>
      </Section>
    </div>
  );
}
