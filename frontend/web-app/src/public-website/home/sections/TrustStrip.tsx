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
  return (
    <section className="sk-home-trust" aria-labelledby="sk-home-trust-title" style={{ backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)', borderTop: '1px solid var(--color-border-default, #e5e7eb)', borderBottom: '1px solid var(--color-border-default, #e5e7eb)', padding: 'var(--space-10, 40px) 0 var(--space-8, 32px)' }}>
      <PublicContentContainer>
        <Reveal>
          <p style={{ margin: 0, textAlign: 'center', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #2f6f4f)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
            Why cultivators trust SporeKart
          </p>
          <h2 id="sk-home-trust-title" style={{ margin: 'var(--space-2, 8px) auto 0', textAlign: 'center', fontSize: 'var(--text-title-md, 22px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Proven where it matters most
          </h2>
        </Reveal>
      </PublicContentContainer>

      <div className="sk-trust-marquee" style={{ marginTop: 'var(--space-6, 24px)', overflow: 'hidden', width: '100%' }}>
        <div className="sk-trust-track">
          {[...TRUST_STATS, ...TRUST_STATS].map((stat, i) => (
            <span key={`${stat.label}-${i}`} className="sk-trust-item">
              <span aria-hidden="true" className="sk-trust-icon">
                <Icon name={stat.icon} size={18} aria-label={stat.label} />
              </span>
              <AnimatedCounter
                value={stat.value}
                text={stat.text}
                suffix={stat.suffix}
                decimals={stat.decimals}
                label={stat.label}
                placeholder
              />
            </span>
          ))}
        </div>
      </div>

      <style>{`
        .sk-trust-track {
          display: flex;
          align-items: center;
          gap: var(--space-8, 32px);
          width: fit-content;
          animation: sk-trust-scroll 40s linear infinite;
        }
        .sk-trust-marquee:hover .sk-trust-track {
          animation-play-state: paused;
        }
        .sk-trust-item {
          display: inline-flex;
          align-items: center;
          gap: var(--space-2, 8px);
          white-space: nowrap;
          font-size: var(--text-body-md, 16px);
          font-weight: 600;
          color: var(--color-text-primary, #1f2933);
        }
        .sk-trust-icon {
          display: inline-flex;
          width: 36px;
          height: 36px;
          flex-shrink: 0;
          align-items: center;
          justify-content: center;
          border-radius: var(--radius-md, 8px);
          background: var(--color-bg-accent-default, #2f6f4f);
          color: #ffffff;
        }
        @keyframes sk-trust-scroll {
          from { transform: translateX(-50%); }
          to { transform: translateX(0); }
        }
        @media (max-width: 768px) {
          .sk-home-trust { padding: var(--space-8, 32px) 0 var(--space-6, 24px); }
          .sk-trust-track { gap: var(--space-5, 20px); animation-duration: 30s; }
          .sk-trust-item { font-size: var(--text-body-sm, 14px); }
          .sk-trust-icon { width: 30px; height: 30px; }
        }
      `}</style>
    </section>
  );
}
