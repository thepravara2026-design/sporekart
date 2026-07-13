import { useState } from 'react';
import { PublicContentContainer } from '../../PublicContentContainer';
import { Button } from '../../../design-system/components/core/Button';
import { Icon } from '../../../design-system/icons/Icon';

const BENEFITS = [
  { icon: 'book-open', label: 'Cultivation guides & playbooks' },
  { icon: 'tag', label: 'Early access to new strains' },
  { icon: 'users', label: 'Invites to grower community events' },
];

export function NewsletterCta() {
  const [email, setEmail] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const sectionStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-accent-default, #1d4ed8)',
    color: 'var(--color-text-inverse, #ffffff)',
  };

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if (!email) {
      return;
    }
    setSubmitted(true);
  };

  return (
    <section className="sk-home-newsletter" aria-labelledby="sk-home-newsletter-title" style={sectionStyle}>
      <PublicContentContainer>
        <div
          style={{
            padding: 'var(--space-9, 56px) var(--space-5, 24px)',
            display: 'grid',
            gridTemplateColumns: 'minmax(0, 1.2fr) minmax(0, 0.8fr)',
            gap: 'var(--space-8, 48px)',
            alignItems: 'center',
          }}
        >
          <div>
            <h2 id="sk-home-newsletter-title" style={{ margin: 0, fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800 }}>
              Join the SporeKart community
            </h2>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-md, 16px)', opacity: 0.92, maxWidth: 520 }}>
              Get cultivation tips, new-strain drops, and grower event invites — straight to your inbox.
            </p>

            {submitted ? (
              <p role="status" style={{ marginTop: 'var(--space-5, 20px)', display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontWeight: 600 }}>
                <Icon name="check-circle" size={20} aria-label="Subscribed" /> Thanks! You’re on the list.
              </p>
            ) : (
              <form onSubmit={handleSubmit} style={{ marginTop: 'var(--space-5, 20px)', display: 'flex', flexWrap: 'wrap', gap: 'var(--space-3, 12px)' }}>
                <label className="sk-visually-hidden" htmlFor="sk-newsletter-email">Email address</label>
                <input
                  id="sk-newsletter-email"
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="you@farm.example"
                  style={{
                    flex: '1 1 260px',
                    padding: 'var(--space-3, 12px) var(--space-4, 16px)',
                    borderRadius: 'var(--radius-md, 8px)',
                    border: '1px solid var(--color-border-inverse, rgba(255,255,255,0.5))',
                    fontSize: 'var(--text-body-md, 16px)',
                    backgroundColor: 'var(--color-bg-inverse, #ffffff)',
                    color: 'var(--color-text-primary, #1f2933)',
                  }}
                />
                <Button type="submit" variant="secondary" size="lg" rightIcon={<Icon name="arrow-right" size={18} aria-label="Subscribe" />}>
                  Subscribe
                </Button>
              </form>
            )}
          </div>

          <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
            {BENEFITS.map((benefit) => (
              <li key={benefit.label} style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)', fontSize: 'var(--text-body-md, 16px)' }}>
                <Icon name={benefit.icon} size={20} aria-label={benefit.label} />
                {benefit.label}
              </li>
            ))}
          </ul>
        </div>
      </PublicContentContainer>
    </section>
  );
}
