import { Icon } from '../../../design-system/icons/Icon';
import { Link } from 'react-router-dom';

export function EmptyState({
  icon = 'search',
  title,
  message,
  suggestions,
}: {
  icon?: string;
  title: string;
  message?: string;
  suggestions?: { label: string; to: string }[];
}) {
  return (
    <div
      style={{
        textAlign: 'center',
        padding: 'var(--space-9, 56px) var(--space-5, 24px)',
        border: '1px dashed var(--color-border-default, #e5e7eb)',
        borderRadius: 'var(--radius-lg, 12px)',
        backgroundColor: 'var(--color-bg-surface-muted, #f1f5f9)',
      }}
    >
      <span style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 56, height: 56, borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-muted, #9ca3af)', marginBottom: 'var(--space-3, 12px)' }}>
        <Icon name={icon} size={26} aria-label={title} />
      </span>
      <h3 style={{ margin: 0, fontSize: 'var(--text-title-md, 22px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>{title}</h3>
      {message && <p style={{ margin: 'var(--space-2, 8px) auto 0', maxWidth: 460, color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{message}</p>}
      {suggestions && suggestions.length > 0 && (
        <div style={{ marginTop: 'var(--space-4, 16px)', display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)', justifyContent: 'center' }}>
          {suggestions.map((s) => (
            <Link key={s.label} to={s.to} className="sk-blog-tag" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
              {s.label}
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
