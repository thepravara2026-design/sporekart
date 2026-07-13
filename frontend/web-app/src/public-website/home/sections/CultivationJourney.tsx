import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';

const STEPS = [
  { icon: 'book-open', title: 'Learn', description: 'Explore training and playbooks to master the basics.' },
  { icon: 'package', title: 'Buy Spawn', description: 'Order lab-verified spawn and starter kits.' },
  { icon: 'sun', title: 'Cultivate', description: 'Follow guided steps from inoculation to fruiting.' },
  { icon: 'box', title: 'Harvest', description: 'Pick at peak with our freshness and drying guidance.' },
  { icon: 'tag', title: 'Sell', description: 'List and supply through local and partner channels.' },
  { icon: 'trending-up', title: 'Grow Business', description: 'Scale beds and yields with ongoing support.' },
];

export function CultivationJourney() {
  const headerStyle: React.CSSProperties = { marginBottom: 'var(--space-6, 32px)', maxWidth: 640 };
  const listStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-2, 8px)',
    maxWidth: 720,
    margin: '0 auto',
  };
  const stepStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    gap: 'var(--space-4, 16px)',
    padding: 'var(--space-4, 16px)',
    borderRadius: 'var(--radius-lg, 12px)',
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
  };

  return (
    <section className="sk-home-journey" aria-labelledby="sk-home-journey-title" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
      <PublicContentContainer>
        <div style={headerStyle}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>The journey</span>
          <h2 id="sk-home-journey-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Your journey, from first spore to thriving business
          </h2>
        </div>

        <ol style={{ ...listStyle, listStyle: 'none', padding: 0 }}>
          {STEPS.map((step, i) => (
            <li key={step.title}>
              <div style={stepStyle}>
                <span
                  aria-hidden="true"
                  style={{ display: 'inline-flex', width: 48, height: 48, flexShrink: 0, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}
                >
                  <Icon name={step.icon} size={24} aria-label={step.title} />
                </span>
                <div>
                  <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>
                    <span style={{ color: 'var(--color-text-accent, #1d4ed8)', marginRight: 'var(--space-2, 8px)' }}>{i + 1}.</span>
                    {step.title}
                  </h3>
                  <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{step.description}</p>
                </div>
              </div>
              {i < STEPS.length - 1 && (
                <div style={{ display: 'flex', justifyContent: 'center', padding: 'var(--space-1, 4px) 0' }} aria-hidden="true">
                  <Icon name="chevron-down" size={20} aria-label="Next step" color="var(--color-text-muted, #9ca3af)" />
                </div>
              )}
            </li>
          ))}
        </ol>
      </PublicContentContainer>
    </section>
  );
}
