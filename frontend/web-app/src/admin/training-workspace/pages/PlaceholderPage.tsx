import { memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

interface PlaceholderPageProps {
  title: string;
  description: string;
  icon: string;
  future?: boolean;
}

const PlaceholderPage = memo(function PlaceholderPage({
  title,
  description,
  icon,
  future = false,
}: PlaceholderPageProps) {
  return (
    <div style={{
      display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
      padding: 'var(--space-stack-xl) var(--space-page-x)',
      textAlign: 'center', minHeight: 400,
    }}>
      <div style={{
        width: 80, height: 80, borderRadius: 'var(--radius-full)',
        background: future ? 'var(--color-bg-skeleton-base)' : 'var(--color-bg-primary-subtle)',
        color: future ? 'var(--color-text-tertiary)' : 'var(--color-primary)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        marginBottom: 'var(--space-stack-md)',
      }}>
        <Icon name={icon} size={36} color="currentColor" />
      </div>
      <h2 style={{
        margin: '0 0 var(--space-stack-xs)',
        fontSize: 'var(--text-h3)',
        color: 'var(--color-text-primary)',
        fontWeight: 'var(--weight-semibold)',
      }}>
        {title}
      </h2>
      <p style={{
        margin: '0 0 var(--space-stack-md)',
        fontSize: 'var(--text-body)',
        color: 'var(--color-text-secondary)',
        maxWidth: 480, lineHeight: 'var(--leading-relaxed)',
      }}>
        {description}
      </p>
      {future && (
        <span style={{
          display: 'inline-flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
          padding: '4px 12px', borderRadius: 'var(--radius-badge)',
          background: 'var(--color-bg-skeleton-base)',
          color: 'var(--color-text-tertiary)',
          fontSize: 'var(--text-caption)',
          fontWeight: 'var(--weight-medium)',
        }}>
          <Icon name="clock" size={12} color="currentColor" />
          Coming in a future sprint
        </span>
      )}
    </div>
  );
});

export default PlaceholderPage;
