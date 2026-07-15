import React from 'react';
import type { MediaFilters, AssetType, AssetStatus } from '../types';
import { ASSET_TYPE_LABELS, ASSET_STATUS_LABELS } from '../types';

interface MediaFilterPanelProps {
  filters: MediaFilters;
  onFiltersChange: (filters: MediaFilters) => void;
  allTags: string[];
  activeFilterCount: number;
  onClearAll: () => void;
}

const sectionStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)',
};

const labelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)',
  color: 'var(--color-text-primary)', textTransform: 'uppercase',
  letterSpacing: 'var(--tracking-wide)',
};

const chipGroupStyle: React.CSSProperties = {
  display: 'flex', flexWrap: 'wrap', gap: 4,
};

function Chip({
  label, active, onClick,
}: {
  label: string; active: boolean; onClick: () => void;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      style={{
        padding: '4px 10px', borderRadius: 'var(--radius-full)', border: 'none',
        fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
        cursor: 'pointer', fontWeight: active ? 'var(--weight-semibold)' : 'var(--weight-normal)',
        background: active ? 'var(--color-primary)' : 'var(--color-bg-surface-raised)',
        color: active ? '#fff' : 'var(--color-text-secondary)',
        transition: 'all var(--duration-fast) var(--easing-standard)',
      }}
    >
      {label}
    </button>
  );
}

export const MediaFilterPanel = React.memo(function MediaFilterPanel({
  filters, onFiltersChange, allTags, activeFilterCount, onClearAll,
}: MediaFilterPanelProps) {
  const toggleType = (t: AssetType) => {
    const next = filters.types.includes(t)
      ? filters.types.filter((x) => x !== t)
      : [...filters.types, t];
    onFiltersChange({ ...filters, types: next });
  };

  const toggleStatus = (s: AssetStatus) => {
    const next = filters.statuses.includes(s)
      ? filters.statuses.filter((x) => x !== s)
      : [...filters.statuses, s];
    onFiltersChange({ ...filters, statuses: next });
  };

  const toggleTag = (tag: string) => {
    const next = filters.tags.includes(tag)
      ? filters.tags.filter((x) => x !== tag)
      : [...filters.tags, tag];
    onFiltersChange({ ...filters, tags: next });
  };

  const setDateRange = (field: 'dateFrom' | 'dateTo', value: string) => {
    onFiltersChange({ ...filters, [field]: value || null });
  };

  const setFileSizeRange = (field: 'fileSizeMin' | 'fileSizeMax', value: string) => {
    onFiltersChange({ ...filters, [field]: value ? Number(value) : null });
  };

  const types: AssetType[] = ['image', 'video', 'document', '3d_model', 'audio'];
  const statuses: AssetStatus[] = ['published', 'processing', 'archived', 'failed'];

  return (
    <div
      style={{
        display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)',
        padding: 'var(--space-component-gap)',
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
          Filters {activeFilterCount > 0 && <span style={{ color: 'var(--color-primary)' }}>({activeFilterCount})</span>}
        </span>
        {activeFilterCount > 0 && (
          <button
            type="button"
            onClick={onClearAll}
            style={{
              background: 'none', border: 'none', cursor: 'pointer',
              fontSize: 'var(--text-caption)', color: 'var(--color-primary)',
              fontFamily: 'var(--font-family-sans)',
            }}
          >
            Clear all
          </button>
        )}
      </div>

      <div style={sectionStyle}>
        <span style={labelStyle}>Type</span>
        <div style={chipGroupStyle}>
          {types.map((t) => (
            <Chip key={t} label={ASSET_TYPE_LABELS[t]} active={filters.types.includes(t)} onClick={() => toggleType(t)} />
          ))}
        </div>
      </div>

      <div style={sectionStyle}>
        <span style={labelStyle}>Status</span>
        <div style={chipGroupStyle}>
          {statuses.map((s) => (
            <Chip key={s} label={ASSET_STATUS_LABELS[s]} active={filters.statuses.includes(s)} onClick={() => toggleStatus(s)} />
          ))}
        </div>
      </div>

      {allTags.length > 0 && (
        <div style={sectionStyle}>
          <span style={labelStyle}>Tags</span>
          <div style={{ ...chipGroupStyle, maxHeight: 200, overflowY: 'auto' }}>
            {allTags.map((tag) => (
              <Chip key={tag} label={tag} active={filters.tags.includes(tag)} onClick={() => toggleTag(tag)} />
            ))}
          </div>
        </div>
      )}

      <div style={sectionStyle}>
        <span style={labelStyle}>Date Range</span>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
          <input
            type="date"
            value={filters.dateFrom ?? ''}
            onChange={(e) => setDateRange('dateFrom', e.target.value)}
            aria-label="From date"
            style={{
              padding: '6px 8px', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
              border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-background)', color: 'var(--color-text-primary)',
            }}
          />
          <input
            type="date"
            value={filters.dateTo ?? ''}
            onChange={(e) => setDateRange('dateTo', e.target.value)}
            aria-label="To date"
            style={{
              padding: '6px 8px', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
              border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-background)', color: 'var(--color-text-primary)',
            }}
          />
        </div>
      </div>

      <div style={sectionStyle}>
        <span style={labelStyle}>File Size</span>
        <div style={{ display: 'flex', gap: 4, alignItems: 'center' }}>
          <input
            type="number"
            placeholder="Min MB"
            value={filters.fileSizeMin != null ? filters.fileSizeMin / (1024 * 1024) : ''}
            onChange={(e) => setFileSizeRange('fileSizeMin', e.target.value ? String(Number(e.target.value) * 1024 * 1024) : '')}
            aria-label="Minimum file size in MB"
            style={{
              padding: '6px 8px', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
              border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-background)', color: 'var(--color-text-primary)', width: '100%',
            }}
          />
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>–</span>
          <input
            type="number"
            placeholder="Max MB"
            value={filters.fileSizeMax != null ? filters.fileSizeMax / (1024 * 1024) : ''}
            onChange={(e) => setFileSizeRange('fileSizeMax', e.target.value ? String(Number(e.target.value) * 1024 * 1024) : '')}
            aria-label="Maximum file size in MB"
            style={{
              padding: '6px 8px', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
              border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-background)', color: 'var(--color-text-primary)', width: '100%',
            }}
          />
        </div>
      </div>
    </div>
  );
});

export default MediaFilterPanel;
