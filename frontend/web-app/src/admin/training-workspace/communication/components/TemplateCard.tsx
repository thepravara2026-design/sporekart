import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { MessageTemplate } from '../data/communicationTypes';
import { TEMPLATE_KIND_LABELS } from '../data/communicationTypes';
import { formatCount, formatDate } from '../data/communicationFormatters';
import ChannelChip from './ChannelChip';

export interface TemplateCardProps {
  template: MessageTemplate;
  onPreview?: (template: MessageTemplate) => void;
}

const TemplateCard = memo(function TemplateCard({ template, onPreview }: TemplateCardProps) {
  const { name, kind, subject, variables, channels, updatedAt, usageCountPlaceholder, future } = template;
  return (
    <article
      aria-label={name}
      style={{
        display: 'flex', flexDirection: 'column', gap: 'var(--space-3)',
        padding: 'var(--space-4)',
        background: future ? 'var(--color-bg-surface-muted)' : 'var(--color-bg-surface-default)',
        border: `1px ${future ? 'dashed' : 'solid'} var(--color-border-default)`,
        borderRadius: 'var(--radius-lg)', boxShadow: future ? 'none' : 'var(--shadow-1)',
      }}
    >
      <header style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 'var(--space-2)' }}>
        <div style={{ minWidth: 0 }}>
          <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{name}</h3>
          <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
            {TEMPLATE_KIND_LABELS[kind]}
          </p>
        </div>
        {future && (
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)', padding: '2px 8px', border: '1px dashed var(--color-border-default)', borderRadius: 'var(--radius-sm)' }}>
            Future
          </span>
        )}
      </header>

      {subject && subject !== '—' && (
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          <strong style={{ color: 'var(--color-text-primary)' }}>Subject:</strong> {subject}
        </p>
      )}

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-1)' }}>
        {variables.map((v) => (
          <code
            key={v}
            style={{
              fontSize: 'var(--text-caption)', padding: '2px 6px', borderRadius: 'var(--radius-sm)',
              background: 'var(--color-bg-primary-weak)', color: 'var(--color-primary)',
            }}
          >
            {`{{${v}}}`}
          </code>
        ))}
      </div>

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)' }}>
        {channels.map((ch) => <ChannelChip key={ch} channel={ch} future={future} />)}
      </div>

      <footer style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-2)', paddingTop: 'var(--space-2)', borderTop: '1px solid var(--color-border-default)' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
          {future ? 'Not active' : `${formatCount(usageCountPlaceholder)} uses`} · Updated {formatDate(updatedAt)}
        </span>
        {onPreview && !future && (
          <button
            type="button"
            onClick={() => onPreview(template)}
            style={{
              display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)',
              padding: '4px 10px', borderRadius: 'var(--radius-sm)',
              border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)',
              color: 'var(--color-primary)', fontSize: 'var(--text-body-sm)', fontWeight: 500, cursor: 'pointer',
            }}
          >
            <Icon name="eye" size={14} /> Preview
          </button>
        )}
      </footer>
    </article>
  );
});

export default TemplateCard;
