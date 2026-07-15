import { useRef, useEffect, useState } from 'react';
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
          setRevealCount(Math.min(STEPS.length, Math.ceil(clamped * STEPS.length)));
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
    <section ref={sectionRef} className="sk-home-journey" aria-labelledby="sk-home-journey-title" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)', padding: 'var(--space-12, 48px) 0' }}>
      <PublicContentContainer>
        <div style={{ marginBottom: 'var(--space-7, 28px)', maxWidth: 640 }}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #2f6f4f)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>The journey</span>
          <h2 id="sk-home-journey-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            From first spore to thriving business
          </h2>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', maxWidth: 640 }}>
          {STEPS.map((step, i) => {
            const isRevealed = i < revealCount;
            return (
              <div key={step.title} style={{
                transition: 'opacity 500ms ease, transform 500ms ease',
                opacity: isRevealed ? 1 : 0,
                transform: isRevealed ? 'translateY(0)' : 'translateY(12px)',
              }}>
                <div style={{
                  display: 'flex',
                  alignItems: 'flex-start',
                  gap: 'var(--space-4, 16px)',
                  padding: 'var(--space-4, 16px)',
                  borderRadius: 'var(--radius-lg, 12px)',
                  backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
                  border: '1px solid var(--color-border-default, #e5e7eb)',
                }}>
                  <span aria-hidden="true" style={{ display: 'inline-flex', width: 44, height: 44, flexShrink: 0, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-accent-subtle, #c8e6c9)', color: 'var(--color-text-accent, #2f6f4f)' }}>
                    <Icon name={step.icon} size={22} aria-label={step.title} />
                  </span>
                  <div>
                    <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>
                      <span style={{ color: 'var(--color-text-accent, #2f6f4f)', marginRight: 'var(--space-2, 8px)' }}>{i + 1}.</span>
                      {step.title}
                    </h3>
                    <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.55 }}>{step.description}</p>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </PublicContentContainer>
    </section>
  );
}
