import { Icon } from '../../design-system/icons/Icon';

export interface MediaPlaceholderProps {
  label: string;
  alt?: string;
  icon?: string;
  aspectRatio?: string;
  rounded?: boolean;
  caption?: string;
}

export function MediaPlaceholder({
  label,
  alt,
  icon = 'image',
  aspectRatio = '16 / 9',
  rounded = true,
  caption,
}: MediaPlaceholderProps) {
  const style: React.CSSProperties = {
    position: 'relative',
    width: '100%',
    aspectRatio,
    borderRadius: rounded ? 'var(--radius-lg, 12px)' : 0,
    border: '1px dashed var(--color-border-default, #e5e7eb)',
    backgroundColor: 'var(--color-bg-surface-muted, #f1f5f9)',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 'var(--space-2, 8px)',
    color: 'var(--color-text-muted, #9ca3af)',
    overflow: 'hidden',
  };

  return (
    <figure style={{ margin: 0 }} aria-label={alt ?? `Image placeholder: ${label}`}>
      <div style={style}>
        <Icon name={icon} size={28} aria-label={label} />
        <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 500 }}>{label}</span>
        <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontStyle: 'italic' }}>Image placeholder</span>
      </div>
      {caption && (
        <figcaption style={{ marginTop: 'var(--space-1, 4px)', fontSize: 'var(--text-body-xs, 12px)', color: 'var(--color-text-muted, #9ca3af)' }}>
          {caption}
        </figcaption>
      )}
    </figure>
  );
}
