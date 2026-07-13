import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';
import { Reveal } from '../Reveal';
import { AnimatedCounter } from '../AnimatedCounter';

const TRUST_STATS = [
  { icon: 'clock', value: 12, suffix: '+', label: 'Years of experience' },
  { icon: 'users', value: 12, suffix: 'k+', label: 'Farmers served' },
  { icon: 'book-open', value: 40, suffix: '+', label: 'Training programs' },
  { icon: 'package', value: 500, suffix: 'k+', label: 'Products delivered' },
  { icon: 'check-circle', value: 99.2, suffix: '%', decimals: 1, label: 'Quality commitment' },
  { icon: 'shield', text: 'ISO', label: 'Quality certifications' },
];

export function TrustStrip() {
  const style: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)',
    borderTop: '1px solid var(--color-border-default, #e5e7eb)',
    borderBottom: '1px solid var(--color-border-default, #e5e7eb)',
  };

  return (
    <section className="sk-home-trust" aria-labelledby="sk-home-trust-title" style={style}>
      <PublicContentContainer>
        <Reveal>
          <p style={{ margin: 0, textAlign: 'center', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
            Why cultivators trust SporeKart
          </p>
          <h2 id="sk-home-trust-title" style={{ margin: 'var(--space-2, 8px) auto 0', textAlign: 'center', fontSize: 'var(--text-title-md, 22px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Proven where it matters most
          </h2>
        </Reveal>
        <ul
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))',
            gap: 'var(--space-4, 16px)',
            listStyle: 'none',
            margin: 'var(--space-5, 20px) 0 0',
            padding: 0,
          }}
        >
          {TRUST_STATS.map((stat) => (
            <li key={stat.label} style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)' }}>
              <span
                aria-hidden="true"
                style={{ display: 'inline-flex', width: 40, height: 40, flexShrink: 0, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}
              >
                <Icon name={stat.icon} size={20} aria-label={stat.label} />
              </span>
              <AnimatedCounter
                value={stat.value}
                text={stat.text}
                suffix={stat.suffix}
                decimals={stat.decimals}
                label={stat.label}
                placeholder
              />
            </li>
          ))}
        </ul>
      </PublicContentContainer>
    </section>
  );
}
