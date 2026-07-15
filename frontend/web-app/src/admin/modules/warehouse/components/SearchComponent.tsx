import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { SearchBar } from '../../../components/search/SearchBar';
import { WAREHOUSE_SEARCH_FIELDS } from '../constants';

interface SearchComponentProps {
  query: string;
  onQueryChange: (q: string) => void;
  activeFields: string[];
  onToggleField: (field: string) => void;
  placeholder?: string;
}

export const SearchComponent = memo(function SearchComponent({
  query, onQueryChange, activeFields, onToggleField, placeholder = 'Search warehouses...',
}: SearchComponentProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8, width: '100%', maxWidth: 560 }}>
      <SearchBar value={query} onChange={onQueryChange} placeholder={placeholder} debounceMs={200} />
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }} aria-label="Search fields">
        {WAREHOUSE_SEARCH_FIELDS.map((f) => {
          const active = activeFields.includes(f.id);
          const disabled = f.placeholder;
          return (
            <button
              key={f.id}
              type="button"
              disabled={disabled}
              aria-pressed={active}
              onClick={() => !disabled && onToggleField(f.id)}
              title={disabled ? 'Coming soon' : `Search by ${f.label}`}
              style={{
                display: 'inline-flex', alignItems: 'center', gap: 4, padding: '3px 10px',
                fontSize: 'var(--text-caption)', borderRadius: 'var(--radius-badge)',
                border: `1px solid ${active ? 'var(--color-primary)' : 'var(--color-border)'}`,
                background: active ? 'var(--color-primary-alpha)' : 'var(--color-surface)',
                color: disabled ? 'var(--color-text-tertiary)' : active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                cursor: disabled ? 'not-allowed' : 'pointer', opacity: disabled ? 0.6 : 1,
              }}
            >
              {f.label}
              {disabled && <Icon name="lock" size={11} />}
            </button>
          );
        })}
      </div>
    </div>
  );
});


