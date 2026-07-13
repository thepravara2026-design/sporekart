import { useState } from 'react';
import { Icon } from '../design-system/icons/Icon';

export interface AnnouncementRegionProps {
  message?: string;
  ctaLabel?: string;
  ctaHref?: string;
  dismissible?: boolean;
}

export function AnnouncementRegion({
  message = 'Free shipping on your first cultivation kit — shop the catalog today.',
  ctaLabel = 'Shop now',
  ctaHref = '/products',
  dismissible = true,
}: AnnouncementRegionProps) {
  const [dismissed, setDismissed] = useState(false);

  if (dismissed) {
    return null;
  }

  const style: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 'var(--space-3, 12px)',
    padding: 'var(--space-2, 8px) var(--space-5, 24px)',
    backgroundColor: 'var(--color-bg-accent-default, #1d4ed8)',
    color: 'var(--color-text-inverse, #ffffff)',
    fontFamily: 'var(--font-family-sans, system-ui)',
    fontSize: 'var(--text-body-sm, 14px)',
    textAlign: 'center',
  };

  return (
    <div className="sk-public-announcement" role="region" aria-label="Announcement">
      <div style={style}>
        <span>{message}</span>
        {ctaHref && (
          <a
            href={ctaHref}
            style={{ fontWeight: 700, color: 'inherit', textDecoration: 'underline' }}
          >
            {ctaLabel}
          </a>
        )}
        {dismissible && (
          <button
            type="button"
            aria-label="Dismiss announcement"
            onClick={() => setDismissed(true)}
            style={{ marginLeft: 'auto', border: 'none', background: 'transparent', color: 'inherit', cursor: 'pointer', display: 'inline-flex' }}
          >
            <Icon name="x" size={18} aria-label="Dismiss announcement" />
          </button>
        )}
      </div>
    </div>
  );
}
