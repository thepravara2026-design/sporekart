import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';
import { Reveal } from '../Reveal';

const STORY_POINTS = [
  {
    icon: 'users',
    title: 'Who we are',
    body: 'SporeKart is a mushroom cultivation ecosystem built by growers, for growers — pairing science with practical farm know-how.',
  },
  {
    icon: 'sun',
    title: 'Why mushrooms matter',
    body: 'Mushrooms are nutritious, resource-light, and climate-friendly. They turn agricultural waste into livelihood.',
  },
  {
    icon: 'zap',
    title: 'Why we exist',
    body: 'We make premium spawn, training, and support accessible so any cultivator can grow with confidence.',
  },
];

export function StoryBand() {
  const style: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)',
    borderTop: '1px solid var(--color-border-default, #e5e7eb)',
  };
  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
    gap: 'var(--space-5, 24px)',
    padding: 'var(--space-9, 56px) 0',
  };

  return (
    <section className="sk-home-story" aria-labelledby="sk-home-story-title" style={style}>
      <PublicContentContainer>
        <Reveal>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Our story</span>
          <h2 id="sk-home-story-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Cultivation is a craft — and a cause
          </h2>
          <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', maxWidth: 640 }}>
            We started SporeKart to remove the guesswork from mushroom farming. Here is the belief that drives everything we build.
          </p>
        </Reveal>
        <div style={gridStyle}>
          {STORY_POINTS.map((point, i) => (
            <Reveal key={point.title} delay={(i % 3) as 0 | 1 | 2 | 3}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
                <span aria-hidden="true" style={{ display: 'inline-flex', width: 44, height: 44, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}>
                  <Icon name={point.icon} size={22} aria-label={point.title} />
                </span>
                <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{point.title}</h3>
                <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{point.body}</p>
              </div>
            </Reveal>
          ))}
        </div>
      </PublicContentContainer>
    </section>
  );
}
