import { Link as RouterLink } from 'react-router-dom';
import { Icon } from '../../design-system/icons/Icon';
import { PublicContentContainer } from '../PublicContentContainer';

export interface FutureCtaProps {
  title?: string;
  description?: string;
  primaryLabel?: string;
  primaryHref?: string;
  secondaryLabel?: string;
  secondaryHref?: string;
}

export function FutureCta({
  title = 'Start your cultivation journey',
  description = 'Explore SporeKart products and training built for growers of every level.',
  primaryLabel = 'Browse products',
  primaryHref = '/products',
  secondaryLabel = 'Explore training',
  secondaryHref = '/training',
}: FutureCtaProps) {
  const style: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-accent-default, #1d4ed8)',
    color: 'var(--color-text-inverse, #ffffff)',
    borderRadius: 'var(--radius-lg, 12px)',
  };

  return (
    <section className="sk-public-cta" aria-label="Call to action" style={{ padding: 'var(--space-6, 32px) 0' }}>
      <PublicContentContainer>
        <div
          style={{
            ...style,
            padding: 'var(--space-8, 48px) var(--space-6, 32px)',
            display: 'flex',
            flexWrap: 'wrap',
            gap: 'var(--space-5, 24px)',
            alignItems: 'center',
            justifyContent: 'space-between',
          }}
        >
          <div style={{ flex: '1 1 280px' }}>
            <h2 style={{ margin: 0, fontSize: 'var(--text-title-lg, 26px)', fontWeight: 700 }}>{title}</h2>
            <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-md, 16px)', opacity: 0.92 }}>{description}</p>
          </div>
          <div style={{ display: 'flex', gap: 'var(--space-3, 12px)', flexWrap: 'wrap' }}>
            <RouterLink
              to={primaryHref}
              style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontWeight: 600, textDecoration: 'none', backgroundColor: 'var(--color-bg-inverse, #ffffff)', color: 'var(--color-text-accent, #1d4ed8)', padding: 'var(--space-3, 12px) var(--space-5, 24px)', borderRadius: 'var(--radius-md, 8px)' }}
            >
              {primaryLabel} <Icon name="arrow-right" size={18} aria-label="Continue" />
            </RouterLink>
            <RouterLink
              to={secondaryHref}
              style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontWeight: 600, textDecoration: 'none', backgroundColor: 'transparent', color: 'var(--color-text-inverse, #ffffff)', border: '1px solid var(--color-border-inverse, rgba(255,255,255,0.6))', padding: 'var(--space-3, 12px) var(--space-5, 24px)', borderRadius: 'var(--radius-md, 8px)' }}
            >
              {secondaryLabel}
            </RouterLink>
          </div>
        </div>
      </PublicContentContainer>
    </section>
  );
}
