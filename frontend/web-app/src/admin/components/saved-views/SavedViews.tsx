import { useState, useRef, useEffect, useCallback, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import type { SavedView } from '../data-grid/types';

interface SavedViewsProps {
  views?: SavedView[];
  onSave?: (name: string) => void;
  onLoad?: (view: SavedView) => void;
  onDelete?: (id: string) => void;
}

export const SavedViews = memo(function SavedViews({ views = [], onSave, onLoad, onDelete }: SavedViewsProps) {
  const [open, setOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [viewName, setViewName] = useState('');
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleSave = useCallback(() => {
    if (!viewName.trim()) return;
    onSave?.(viewName.trim());
    setViewName('');
    setSaving(false);
  }, [viewName, onSave]);

  const handleLoad = useCallback(
    (view: SavedView) => {
      onLoad?.(view);
      setOpen(false);
    },
    [onLoad]
  );

  const defaultView = views.find((v) => v.default);
  const pinnedViews = views.filter((v) => v.pinned);

  return (
    <div ref={ref} style={{ position: 'relative' }}>
      <Button variant="outline" size="sm" onClick={() => setOpen(!open)} aria-haspopup="dialog" aria-expanded={open}>
        <Icon name="bookmark" size={14} /> Views
      </Button>
      {open && (
        <div
          role="dialog"
          aria-label="Saved views"
          style={{
            position: 'absolute',
            top: '100%',
            right: 0,
            marginTop: 4,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            boxShadow: 'var(--elevation-lg)',
            zIndex: 100,
            minWidth: 240,
            padding: 8,
          }}
        >
          <div
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '4px 8px 8px',
              borderBottom: '1px solid var(--color-border)',
              marginBottom: 4,
            }}
          >
            <span style={{ fontWeight: 500, fontSize: 'var(--text-body)' }}>Saved Views</span>
            <Button variant="ghost" size="sm" onClick={() => setSaving(!saving)}>
              <Icon name="plus" size={14} />
            </Button>
          </div>
          {saving && (
            <div style={{ display: 'flex', gap: 4, padding: '4px 8px', marginBottom: 8 }}>
              <input
                type="text"
                value={viewName}
                onChange={(e) => setViewName(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSave()}
                placeholder="View name..."
                autoFocus
                style={{
                  flex: 1,
                  padding: '4px 8px',
                  border: '1px solid var(--color-border)',
                  borderRadius: 'var(--radius-md)',
                  fontSize: 'var(--text-body)',
                  background: 'var(--color-surface)',
                  color: 'var(--color-text-primary)',
                }}
              />
              <Button size="sm" onClick={handleSave}>
                Save
              </Button>
            </div>
          )}
          {defaultView && (
            <ViewItem view={defaultView} onLoad={handleLoad} onDelete={onDelete} isDefault />
          )}
          {pinnedViews.length > 0 && (
            <>
              <div style={sectionHeaderStyle}>Pinned</div>
              {pinnedViews.map((v) => (
                <ViewItem key={v.id} view={v} onLoad={handleLoad} onDelete={onDelete} />
              ))}
            </>
          )}
          {views.filter((v) => !v.pinned && !v.default).length > 0 && (
            <>
              <div style={sectionHeaderStyle}>All Views</div>
              {views
                .filter((v) => !v.pinned && !v.default)
                .map((v) => (
                  <ViewItem key={v.id} view={v} onLoad={handleLoad} onDelete={onDelete} />
                ))}
            </>
          )}
          {views.length === 0 && (
            <div
              style={{
                padding: '16px 8px',
                textAlign: 'center',
                color: 'var(--color-text-tertiary)',
                fontSize: 'var(--text-body)',
              }}
            >
              No saved views yet
            </div>
          )}
        </div>
      )}
    </div>
  );
});

function ViewItem({
  view,
  onLoad,
  onDelete,
  isDefault,
}: {
  view: SavedView;
  onLoad: (view: SavedView) => void;
  onDelete?: (id: string) => void;
  isDefault?: boolean;
}) {
  return (
    <div
      role="button"
      tabIndex={0}
      onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); onLoad(view); } }}
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 4,
        padding: '6px 8px',
        borderRadius: 'var(--radius-sm)',
        cursor: 'pointer',
      }}
      onClick={() => onLoad(view)}
    >
      <Icon name={isDefault ? 'star' : 'bookmark'} size={14} />
      <span style={{ flex: 1, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>
        {view.name}
      </span>
      {onDelete && (
        <button
          onClick={(e) => { e.stopPropagation(); onDelete(view.id); }}
          aria-label={`Delete view ${view.name}`}
          style={{
            border: 'none',
            background: 'transparent',
            cursor: 'pointer',
            color: 'var(--color-text-tertiary)',
            padding: 2,
            display: 'flex',
          }}
        >
          <Icon name="trash" size={12} />
        </button>
      )}
    </div>
  );
}

const sectionHeaderStyle: React.CSSProperties = {
  padding: '4px 8px',
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-tertiary)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
};
