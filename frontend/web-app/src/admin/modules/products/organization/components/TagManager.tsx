import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { OrgTag } from '../types';

interface TagManagerProps {
  tags: OrgTag[];
}

const sectionStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', gap: 4,
};

export const TagManager = React.memo(function TagManager({ tags }: TagManagerProps) {
  const grouped = React.useMemo(() => {
    const map = new Map<string, OrgTag[]>();
    tags.forEach((t) => {
      const list = map.get(t.tagType) ?? [];
      list.push(t);
      map.set(t.tagType, list);
    });
    return Array.from(map.entries()).sort(([a], [b]) => a.localeCompare(b));
  }, [tags]);

  const [activeTag, setActiveTag] = React.useState<string | null>(null);
  const active = activeTag ? tags.find((t) => t.id === activeTag) ?? null : null;

  if (tags.length === 0) {
    return (
      <div style={{ padding: 'var(--space-12)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>
        <Icon name="pricetag" size={48} style={{ marginBottom: 8 }} />
        <p>No tags yet.</p>
      </div>
    );
  }

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 300px', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        {grouped.map(([type, typeTags]) => (
          <div key={type} style={sectionStyle}>
            <h4 style={{ margin: '0 0 4px', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', textTransform: 'capitalize' }}>{type} ({typeTags.length})</h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
              {typeTags.map((t) => (
                <button
                  key={t.id}
                  type="button"
                  onClick={() => setActiveTag(t.id)}
                  style={{
                    display: 'inline-flex', alignItems: 'center', gap: 4,
                    padding: '4px 10px', borderRadius: 'var(--radius-full)',
                    border: activeTag === t.id ? '2px solid var(--color-primary)' : '1px solid var(--color-border-default)',
                    background: activeTag === t.id ? 'var(--color-primary-alpha)' : 'var(--color-bg-surface-default)',
                    cursor: 'pointer', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
                    color: 'var(--color-text-primary)', transition: 'all var(--duration-fast) var(--easing-standard)',
                  }}
                >
                  {t.color && <span style={{ width: 8, height: 8, borderRadius: 'var(--radius-full)', background: t.color, display: 'inline-block' }} />}
                  <span>{t.name}</span>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>({t.usageCount})</span>
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
      {active && (
        <aside style={{ position: 'sticky', top: 'var(--space-component-gap)', padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h4 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'flex', alignItems: 'center', gap: 8 }}>
            {active.color && <span style={{ width: 12, height: 12, borderRadius: 'var(--radius-full)', background: active.color, display: 'inline-block' }} />}
            {active.name}
          </h4>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
            <div style={detailRow}><span style={detailLabel}>Type</span><span style={{ textTransform: 'capitalize' }}>{active.tagType}</span></div>
            <div style={detailRow}><span style={detailLabel}>Slug</span><span>{active.slug}</span></div>
            <div style={detailRow}><span style={detailLabel}>Usage</span><span>{active.usageCount} products</span></div>
            <div style={detailRow}><span style={detailLabel}>System</span><span>{active.isSystem ? 'Yes' : 'No'}</span></div>
            <div style={detailRow}><span style={detailLabel}>Created</span><span>{new Date(active.createdAt).toLocaleDateString()}</span></div>
            {active.description && <p style={{ margin: '8px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{active.description}</p>}
          </div>
        </aside>
      )}
    </div>
  );
});

const detailRow: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', padding: '4px 0' };
const detailLabel: React.CSSProperties = { color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' };

export default TagManager;
