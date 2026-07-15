import { useRef, useEffect, useState } from 'react';
import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';

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
  const sectionRef = useRef<HTMLElement>(null);
  const [revealCount, setRevealCount] = useState(0);

  useEffect(() => {
    const section = sectionRef.current;
    if (!section) return;
    let ticking = false;
    const handleScroll = () => {
      if (!ticking) {
        window.requestAnimationFrame(() => {
          const rect = section.getBoundingClientRect();
          const sectionHeight = rect.height;
          const viewportHeight = window.innerHeight;
          const scrollProgress = -rect.top / (sectionHeight - viewportHeight);
          const clamped = Math.max(0, Math.min(1, scrollProgress));
          const count = Math.min(STORY_POINTS.length, Math.ceil(clamped * STORY_POINTS.length));
          setRevealCount(count);
          ticking = false;
        });
        ticking = true;
      }
    };
    window.addEventListener('scroll', handleScroll, { passive: true });
    handleScroll();
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <section ref={sectionRef} className="sk-home-story" aria-labelledby="sk-home-story-title" style={{ backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' }}>
      <PublicContentContainer>
        <div style={{ padding: 'var(--space-12, 48px) 0' }}>
          <div style={{ marginBottom: 'var(--space-6, 24px)', maxWidth: 640 }}>
            <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #2f6f4f)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Our story</span>
            <h2 id="sk-home-story-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
              Cultivation is a craft — and a cause
            </h2>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>
              We started SporeKart to remove the guesswork from mushroom farming. Here is the belief that drives everything we build.
            </p>
          </div>

          <div className="sk-story-grid">
            {STORY_POINTS.map((point, i) => {
              const isRevealed = i < revealCount;
              return (
                <div key={point.title} style={{
                  transition: 'opacity 500ms ease, transform 500ms ease',
                  opacity: isRevealed ? 1 : 0,
                  transform: isRevealed ? 'translateY(0)' : 'translateY(12px)',
                }}>
                  <div className="sk-story-card">
                    <span aria-hidden="true" className="sk-story-icon">
                      <Icon name={point.icon} size={24} aria-label={point.title} />
                    </span>
                    <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{point.title}</h3>
                    <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.65 }}>{point.body}</p>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        <style>{`
          .sk-home-story .sk-story-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: var(--space-5, 24px);
          }
          .sk-home-story .sk-story-card {
            display: flex;
            flex-direction: column;
            gap: var(--space-3, 12px);
            padding: var(--space-6, 24px) var(--space-5, 20px);
            border-radius: var(--radius-lg, 12px);
            background: var(--color-bg-surface-default, #ffffff);
            border: 1px solid var(--color-border-default, #e5e7eb);
            transition: transform 300ms cubic-bezier(0.4, 0, 0.2, 1), box-shadow 300ms cubic-bezier(0.4, 0, 0.2, 1);
          }
          .sk-home-story .sk-story-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 24px rgba(0,0,0,0.07);
          }
          .sk-home-story .sk-story-icon {
            display: inline-flex;
            width: 52px;
            height: 52px;
            align-items: center;
            justify-content: center;
            border-radius: var(--radius-lg, 12px);
            background: linear-gradient(135deg, var(--color-bg-accent-default, #2f6f4f), var(--color-bg-primary-hover, #265d3f));
            color: #ffffff;
          }
          @media (max-width: 768px) {
            .sk-home-story .sk-story-grid { grid-template-columns: 1fr; gap: var(--space-3, 12px); }
            .sk-home-story .sk-story-card { padding: var(--space-4, 16px); gap: var(--space-2, 8px); }
            .sk-home-story .sk-story-icon { width: 40px; height: 40px; }
            .sk-home-story .sk-story-card p { font-size: 0.8125rem; }
          }
        `}</style>
      </PublicContentContainer>
    </section>
  );
}
