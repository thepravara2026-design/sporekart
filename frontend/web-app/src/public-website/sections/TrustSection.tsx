import { Icon } from '../../design-system/icons/Icon';
import { PublicContentContainer } from '../PublicContentContainer';

const TRUST_ITEMS = [
  { icon: 'truck', label: 'Fast, tracked shipping', description: 'Cultivation kits delivered with care.' },
  { icon: 'shield', label: 'Lab-verified quality', description: 'Sterile, contamination-controlled spawn.' },
  { icon: 'headphone', label: 'Grower support', description: 'Guidance from our cultivation team.' },
  { icon: 'star', label: 'Loved by growers', description: 'Trusted by hobbyists and farms.' },
];

export function TrustSection() {
  const sectionStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
    borderTop: '1px solid var(--color-border-default, #e5e7eb)',
    borderBottom: '1px solid var(--color-border-default, #e5e7eb)',
  };

  return (
    <section className="sk-public-trust" aria-label="Why SporeKart" style={sectionStyle}>
      <PublicContentContainer>
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
            gap: 'var(--space-5, 24px)',
          }}
        >
          {TRUST_ITEMS.map((item) => (
            <div key={item.label} style={{ display: 'flex', alignItems: 'flex-start', gap: 'var(--space-3, 12px)' }}>
              <span
                aria-hidden="true"
                style={{ display: 'inline-flex', flexShrink: 0, width: 40, height: 40, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', background: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}
              >
                <Icon name={item.icon} size={22} aria-label={item.label} />
              </span>
              <div>
                <h3 style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>{item.label}</h3>
                <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{item.description}</p>
              </div>
            </div>
          ))}
        </div>
      </PublicContentContainer>
    </section>
  );
}
