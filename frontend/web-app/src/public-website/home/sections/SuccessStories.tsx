import { useState } from 'react';
import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';
import { NavButton } from '../NavButton';

const TESTIMONIALS = [
  { quote: 'SporeKart’s spawn gave us our first successful flush in weeks. The support team felt like part of our farm.', name: 'Meena R.', role: 'Smallholder farmer, Maharashtra', initials: 'MR' },
  { quote: 'The training program shortened our learning curve dramatically. We scaled from 2 to 20 beds confidently.', name: 'Arjun S.', role: 'Commercial grower, Karnataka', initials: 'AS' },
  { quote: 'Reliable cold-chain delivery means our kits arrive viable every time. That consistency changed our business.', name: 'Priya N.', role: 'Urban cultivator, Delhi', initials: 'PN' },
];

export function SuccessStories() {
  const [index, setIndex] = useState(0);
  const active = TESTIMONIALS[index];

  const sectionStyle: React.CSSProperties = { backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' };
  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'minmax(0, 1fr) minmax(0, 1fr)',
    gap: 'var(--space-8, 48px)',
    alignItems: 'center',
    padding: 'var(--space-12, 48px) 0',
  };
  const videoStyle: React.CSSProperties = {
    position: 'relative',
    borderRadius: 'var(--radius-xl, 20px)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
    background: 'linear-gradient(150deg, var(--color-bg-accent-subtle, #eef2ff), var(--color-bg-success-subtle, #ecfdf5))',
    aspectRatio: '16 / 9',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  };

  const go = (dir: number) => setIndex((i) => (i + dir + TESTIMONIALS.length) % TESTIMONIALS.length);

  return (
    <section className="sk-home-stories" aria-labelledby="sk-home-stories-title" style={sectionStyle}>
      <PublicContentContainer>
        <div style={gridStyle}>
          <div style={videoStyle} aria-hidden="true">
            <span style={{ display: 'inline-flex', width: 72, height: 72, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #1d4ed8)', boxShadow: 'var(--shadow-1, 0 1px 3px rgba(0,0,0,0.06))' }}>
              <Icon name="play-circle" size={40} aria-label="Play video" />
            </span>
            <span style={{ position: 'absolute', bottom: 'var(--space-3, 12px)', left: 'var(--space-4, 16px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>Customer story video placeholder</span>
          </div>

          <div>
            <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Success stories</span>
            <h2 id="sk-home-stories-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
              Farmers growing with SporeKart
            </h2>

            <figure
              aria-live="polite"
              style={{ margin: 'var(--space-4, 16px) 0 0', padding: 'var(--space-5, 24px)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', border: '1px solid var(--color-border-default, #e5e7eb)' }}
            >
              <blockquote style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', lineHeight: 1.6, color: 'var(--color-text-primary, #1f2933)' }}>
                “{active.quote}”
              </blockquote>
              <figcaption style={{ marginTop: 'var(--space-4, 16px)', display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)' }}>
                <span aria-hidden="true" style={{ display: 'inline-flex', width: 44, height: 44, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', fontWeight: 700 }}>{active.initials}</span>
                <span>
                  <span style={{ display: 'block', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{active.name}</span>
                  <span style={{ display: 'block', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{active.role}</span>
                </span>
              </figcaption>
            </figure>

            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)', marginTop: 'var(--space-4, 16px)' }}>
              <button type="button" aria-label="Previous testimonial" onClick={() => go(-1)} style={{ display: 'inline-flex', border: '1px solid var(--color-border-default, #e5e7eb)', background: 'var(--color-bg-surface-default, #ffffff)', borderRadius: 'var(--radius-md, 8px)', padding: 'var(--space-2, 8px)', cursor: 'pointer', color: 'var(--color-text-primary, #1f2933)' }}>
                <Icon name="chevron-left" size={20} aria-label="Previous" />
              </button>
              <button type="button" aria-label="Next testimonial" onClick={() => go(1)} style={{ display: 'inline-flex', border: '1px solid var(--color-border-default, #e5e7eb)', background: 'var(--color-bg-surface-default, #ffffff)', borderRadius: 'var(--radius-md, 8px)', padding: 'var(--space-2, 8px)', cursor: 'pointer', color: 'var(--color-text-primary, #1f2933)' }}>
                <Icon name="chevron-right" size={20} aria-label="Next" />
              </button>
              <span style={{ display: 'inline-flex', gap: 'var(--space-1, 4px)' }} aria-hidden="true">
                {TESTIMONIALS.map((t, i) => (
                  <span key={t.name} style={{ width: 8, height: 8, borderRadius: 'var(--radius-pill, 999px)', backgroundColor: i === index ? 'var(--color-bg-accent-default, #1d4ed8)' : 'var(--color-border-default, #e5e7eb)' }} />
                ))}
              </span>
            </div>

            <div style={{ marginTop: 'var(--space-6, 24px)' }}>
              <NavButton to="/blog" variant="outline" size="md">Read more stories</NavButton>
            </div>
          </div>
        </div>
      </PublicContentContainer>
    </section>
  );
}
