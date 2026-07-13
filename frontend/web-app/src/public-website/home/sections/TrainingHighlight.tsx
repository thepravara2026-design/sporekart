import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';
import { NavButton } from '../NavButton';
import { MediaPlaceholder } from '../MediaPlaceholder';

const BENEFITS = [
  { icon: 'users', label: 'Live, expert-led sessions' },
  { icon: 'check-circle', label: 'Hands-on cultivation labs' },
  { icon: 'trending-up', label: 'Measurable yield outcomes' },
];

export function TrainingHighlight() {
  const sectionStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)',
  };

  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'minmax(0, 1fr) minmax(0, 1fr)',
    gap: 'var(--space-8, 48px)',
    alignItems: 'center',
    padding: 'var(--space-10, 64px) 0',
  };

  const visualStyle: React.CSSProperties = {
    borderRadius: 'var(--radius-xl, 20px)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
    background: 'linear-gradient(150deg, var(--color-bg-accent-subtle, #eef2ff), var(--color-bg-success-subtle, #ecfdf5))',
    minHeight: 280,
    padding: 'var(--space-6, 32px)',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    gap: 'var(--space-3, 12px)',
  };

  return (
    <section className="sk-home-training" aria-labelledby="sk-home-training-title" style={sectionStyle}>
      <PublicContentContainer>
        <div style={gridStyle}>
          <div>
            <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Training</span>
            <h2 id="sk-home-training-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
              Learn to grow — with farmers who’ve done it
            </h2>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6, maxWidth: 520 }}>
              Our upcoming training programs take you from fundamentals to commercial cultivation — built with working farmers and mycologists.
            </p>
            <ul style={{ listStyle: 'none', margin: 'var(--space-4, 16px) 0 0', padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
              {BENEFITS.map((benefit) => (
                <li key={benefit.label} style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-primary, #1f2933)' }}>
                  <Icon name={benefit.icon} size={18} aria-label={benefit.label} color="var(--color-text-accent, #1d4ed8)" />
                  {benefit.label}
                </li>
              ))}
            </ul>
            <div style={{ marginTop: 'var(--space-5, 20px)' }}>
              <NavButton to="/training" size="lg" rightIcon={<Icon name="arrow-right" size={18} aria-label="Register" />}>
                Browse training
              </NavButton>
            </div>
          </div>

          <div style={visualStyle} aria-hidden="true">
            <MediaPlaceholder label="Training session" icon="users" aspectRatio="16 / 9" caption="Training photography placeholder" />
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>
              <Icon name="book-open" size={22} aria-label="Training" /> Upcoming: Oyster Mushroom Masterclass
            </span>
            <p style={{ margin: 0, color: 'var(--color-text-secondary, #4b5563)', fontSize: 'var(--text-body-sm, 14px)' }}>
              A 4-week practical program covering substrate prep, inoculation, fruiting, and harvest.
            </p>
            <span style={{ marginTop: 'auto', alignSelf: 'flex-start', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', border: '1px solid var(--color-border-default, #e5e7eb)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600 }}>
              Enrolling now
            </span>
          </div>
        </div>
      </PublicContentContainer>
    </section>
  );
}
