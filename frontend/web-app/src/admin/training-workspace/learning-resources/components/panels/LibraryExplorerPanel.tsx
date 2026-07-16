import { useMemo } from 'react';
import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useResourceContext } from '../../state/ResourceContext';
import { ResourceViews } from '../visualization/ResourceViews';
import {
  CATEGORY_OPTIONS,
  DEPARTMENT_OPTIONS,
  LANGUAGE_OPTIONS,
  STATUS_OPTIONS,
  RESOURCE_TYPE_LABELS,
  type ResourceType,
} from '../../data/resourceMockData';

const VIEW_OPTIONS = [
  { value: 'grid', label: '\uD83D\uDDD8 Grid' },
  { value: 'list', label: '\u2630 List' },
  { value: 'compact', label: '\u25A6 Compact' },
  { value: 'table', label: '\u2632 Table' },
];

const TYPE_OPTIONS = [
  { value: 'all', label: 'All Types' },
  ...Object.keys(RESOURCE_TYPE_LABELS).map((t) => ({ value: t, label: RESOURCE_TYPE_LABELS[t as ResourceType] })),
];

export function LibraryExplorerPanel() {
  const { state, setSearch, setFilters, setView, openPreview, toggleFavorite, togglePin } = useResourceContext();

  const filtered = useMemo(() => {
    const { search, filters } = state;
    const q = search.toLowerCase();
    return state.resources.filter((r) => {
      if (filters.type !== 'all' && r.type !== filters.type) return false;
      if (filters.category !== 'all' && r.category !== filters.category) return false;
      if (filters.department !== 'all' && r.department !== filters.department) return false;
      if (filters.language !== 'all' && r.language !== filters.language) return false;
      if (filters.status !== 'all' && r.status !== filters.status) return false;
      if (filters.visibility !== 'all' && r.visibility !== filters.visibility) return false;
      if (q && !`${r.name} ${r.code} ${r.description} ${r.tags.join(' ')}`.toLowerCase().includes(q)) return false;
      return true;
    });
  }, [state.resources, state.search, state.filters]);

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3)', flexWrap: 'wrap', gap: 8 }}>
        <h2 style={{ margin: 0 }}>Library Explorer</h2>
        <Badge variant="info" size="md">{filtered.length} resources</Badge>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr 1fr 1fr 1fr 1fr 1fr', gap: 8, marginBottom: 'var(--space-3)', alignItems: 'end' }}>
        <Input type="search" placeholder="Search resources..." value={state.search} onChange={(e) => setSearch(e.target.value)} fullWidth />
        <Select options={TYPE_OPTIONS} value={state.filters.type} onChange={(v) => setFilters({ type: v })} />
        <Select options={[{ value: 'all', label: 'All Categories' }, ...CATEGORY_OPTIONS]} value={state.filters.category} onChange={(v) => setFilters({ category: v })} />
        <Select options={[{ value: 'all', label: 'All Departments' }, ...DEPARTMENT_OPTIONS]} value={state.filters.department} onChange={(v) => setFilters({ department: v })} />
        <Select options={[{ value: 'all', label: 'All Languages' }, ...LANGUAGE_OPTIONS]} value={state.filters.language} onChange={(v) => setFilters({ language: v })} />
        <Select options={[{ value: 'all', label: 'All Status' }, ...STATUS_OPTIONS]} value={state.filters.status} onChange={(v) => setFilters({ status: v })} />
        <Select options={VIEW_OPTIONS} value={state.view} onChange={(v) => setView(v as never)} />
      </div>

      <ResourceViews resources={filtered} view={state.view} onOpen={openPreview} onFavorite={toggleFavorite} onPin={togglePin} />
    </div>
  );
}
