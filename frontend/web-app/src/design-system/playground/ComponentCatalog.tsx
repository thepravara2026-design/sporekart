import { useState, useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { componentManifest, ComponentEntry } from './catalog/componentManifest';

const categories = ['all', 'core', 'forms', 'display', 'navigation', 'feedback', 'charts', 'layout'] as const;
const statusFilters = ['all', 'stable', 'beta', 'deprecated'] as const;

const categoryColors: Record<string, string> = {
  core: 'var(--color-info-500, #2196F3)',
  forms: 'var(--color-success-500, #4CAF50)',
  display: 'var(--color-dataviz-categorical-2, #7B1FA2)',
  navigation: 'var(--color-warning-500, #FF9800)',
  feedback: 'var(--color-danger-500, #EF5350)',
  charts: 'var(--color-dataviz-categorical-1, #009688)',
  layout: 'var(--color-neutral-500, #6D6D6D)',
};

const statusBadgeStyles: Record<string, React.CSSProperties> = {
  stable: { background: 'var(--color-success-50, #E8F5E9)', color: 'var(--color-success-600, #2E7D32)', border: '1px solid var(--color-success-500, #4CAF50)' },
  beta: { background: 'var(--color-warning-50, #FFF8E1)', color: 'var(--color-warning-600, #F57F17)', border: '1px solid var(--color-warning-500, #FFB300)' },
  deprecated: { background: 'var(--color-danger-50, #FBE9E7)', color: 'var(--color-danger-600, #C62828)', border: '1px solid var(--color-danger-500, #EF5350)' },
};

const accessibilityIcons: Record<string, string> = {
  pass: '\u2705',
  partial: '\uD83D\uDD36',
  'needs-review': '\u274C',
};

function getCategoryBadgeStyle(category: string): React.CSSProperties {
  const bg = categoryColors[category] || 'var(--color-neutral-500, #6D6D6D)';
  return {
    display: 'inline-block',
    padding: '2px 8px',
    borderRadius: 'var(--radius-pill, 9999px)',
    fontSize: 'var(--text-caption, 0.75rem)',
    fontWeight: 500,
    color: '#fff',
    background: bg,
    textTransform: 'capitalize',
    lineHeight: '1.5',
  };
}

function getApprovalBadgeStyle(status: string): React.CSSProperties {
  const map: Record<string, React.CSSProperties> = {
    approved: { background: 'var(--color-success-50, #E8F5E9)', color: 'var(--color-success-600, #2E7D32)' },
    pending: { background: 'var(--color-warning-50, #FFF8E1)', color: 'var(--color-warning-600, #F57F17)' },
    'in-review': { background: 'var(--color-info-50, #E3F2FD)', color: 'var(--color-info-600, #1565C0)' },
  };
  return { ...map[status] || map.pending, display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-sm, 4px)', fontSize: 'var(--text-caption, 0.75rem)', fontWeight: 500, textTransform: 'capitalize' };
}

function getResponsiveStyle(status: string): React.CSSProperties {
  const map: Record<string, React.CSSProperties> = {
    pass: { color: 'var(--color-success-600, #2E7D32)' },
    partial: { color: 'var(--color-warning-600, #F57F17)' },
    'not-tested': { color: 'var(--color-text-disabled, #A8A8A8)' },
  };
  return { ...map[status] || map['not-tested'], fontSize: 'var(--text-caption, 0.75rem)', fontWeight: 500 };
}

function matchesSearch(entry: ComponentEntry, query: string): boolean {
  const q = query.toLowerCase().trim();
  if (!q) return true;
  return (
    entry.name.toLowerCase().includes(q) ||
    entry.description.toLowerCase().includes(q) ||
    entry.id.toLowerCase().includes(q)
  );
}

export default function ComponentCatalog() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const initialCategory = searchParams.get('category') || 'all';
  const [activeCategory, setActiveCategory] = useState<string>(
    categories.includes(initialCategory as typeof categories[number]) ? initialCategory : 'all'
  );
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');

  const filteredComponents = useMemo(() => {
    return componentManifest.filter((entry) => {
      if (activeCategory !== 'all' && entry.category !== activeCategory) return false;
      if (statusFilter !== 'all' && entry.status !== statusFilter) return false;
      if (!matchesSearch(entry, searchQuery)) return false;
      return true;
    });
  }, [activeCategory, statusFilter, searchQuery]);

  const handleCategoryChange = (cat: string) => {
    setActiveCategory(cat);
    setSearchParams(cat !== 'all' ? { category: cat } : {});
  };

  const tabStyle = (isActive: boolean): React.CSSProperties => ({
    padding: '8px 16px',
    fontSize: 'var(--text-body-sm, 0.875rem)',
    fontWeight: isActive ? 600 : 400,
    background: isActive ? 'var(--color-bg-primary-default, #2F6F4F)' : 'transparent',
    color: isActive ? '#fff' : 'var(--color-text-secondary, #6D6D6D)',
    border: 'none',
    borderRadius: 'var(--radius-md, 6px)',
    cursor: 'pointer',
    transition: 'all var(--transition-fast, 100ms)',
    textTransform: 'capitalize',
    whiteSpace: 'nowrap' as const,
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap, 32px)', padding: 'var(--space-page-y, 48px) var(--space-page-x, 48px)' }}>
      {/* Header */}
      <div>
        <h1 style={{ fontSize: 'var(--text-h1, 2.25rem)', fontWeight: 700, margin: 0, color: 'var(--color-text-primary, #1D2B22)' }}>
          Component Catalog
        </h1>
        <p style={{ color: 'var(--color-text-secondary, #6D6D6D)', margin: '8px 0 0', fontSize: 'var(--text-body, 1rem)' }}>
          Browse, search, and inspect all design system components
        </p>
        <div style={{ marginTop: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
          <span style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            minWidth: '24px',
            height: '24px',
            padding: '0 8px',
            borderRadius: 'var(--radius-pill, 9999px)',
            background: 'var(--color-bg-primary-default, #2F6F4F)',
            color: '#fff',
            fontSize: 'var(--text-caption, 0.75rem)',
            fontWeight: 600,
          }}>
            {componentManifest.length}
          </span>
          <span style={{ fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-secondary, #6D6D6D)' }}>
            components in catalog
          </span>
        </div>
      </div>

      {/* Filters */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        {/* Category Tabs */}
        <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => handleCategoryChange(cat)}
              style={tabStyle(activeCategory === cat)}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Search and Status */}
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', alignItems: 'center' }}>
          <input
            type="text"
            placeholder="Search components..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{
              flex: '1 1 280px',
              padding: '8px 12px',
              fontSize: 'var(--text-body, 1rem)',
              border: '1px solid var(--color-border-default, #E3E6E3)',
              borderRadius: 'var(--radius-input, 4px)',
              background: 'var(--color-bg-surface-default, #FFFFFF)',
              color: 'var(--color-text-primary, #1D2B22)',
              outline: 'none',
              minWidth: '200px',
            }}
            onFocus={(e) => {
              e.currentTarget.style.borderColor = 'var(--color-border-focus, #2F6F4F)';
            }}
            onBlur={(e) => {
              e.currentTarget.style.borderColor = 'var(--color-border-default, #E3E6E3)';
            }}
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            style={{
              padding: '8px 12px',
              fontSize: 'var(--text-body, 1rem)',
              border: '1px solid var(--color-border-default, #E3E6E3)',
              borderRadius: 'var(--radius-input, 4px)',
              background: 'var(--color-bg-surface-default, #FFFFFF)',
              color: 'var(--color-text-primary, #1D2B22)',
              cursor: 'pointer',
              textTransform: 'capitalize',
            }}
          >
            {statusFilters.map((s) => (
              <option key={s} value={s}>{s === 'all' ? 'All Status' : s}</option>
            ))}
          </select>
        </div>
      </div>

      {/* Results Count */}
      <div style={{ fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-secondary, #6D6D6D)' }}>
        Showing {filteredComponents.length} {filteredComponents.length === 1 ? 'component' : 'components'}
      </div>

      {/* Catalog Grid */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(3, 1fr)',
        gap: 'var(--spacing-lg, 24px)',
      }}>
        {/* Responsive breakpoints via style tags */}
        <style>{`
          @media (max-width: 1024px) {
            .catalog-grid { grid-template-columns: repeat(2, 1fr) !important; }
          }
          @media (max-width: 640px) {
            .catalog-grid { grid-template-columns: 1fr !important; }
          }
        `}</style>
        <div className="catalog-grid" style={{
          display: 'contents',
        }}>
          {filteredComponents.length === 0 ? (
            <div style={{
              gridColumn: '1 / -1',
              textAlign: 'center',
              padding: '64px 24px',
              color: 'var(--color-text-secondary, #6D6D6D)',
            }}>
              <p style={{ fontSize: 'var(--text-h3, 1.5rem)', fontWeight: 600, margin: '0 0 8px' }}>No components found</p>
              <p style={{ fontSize: 'var(--text-body, 1rem)', margin: 0 }}>Try adjusting your search or filter criteria</p>
            </div>
          ) : (
            filteredComponents.map((entry) => (
              <div
                key={entry.id}
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '12px',
                  background: 'var(--color-bg-surface-default, #FFFFFF)',
                  borderRadius: 'var(--radius-card, 8px)',
                  padding: 'var(--space-card-padding, 24px)',
                  boxShadow: 'var(--shadow-card, 0 1px 2px rgba(29,43,34,0.08))',
                  border: '1px solid var(--color-border-default, #E3E6E3)',
                  transition: 'box-shadow var(--transition-fast, 100ms)',
                }}
              >
                {/* Category Badge */}
                <div style={getCategoryBadgeStyle(entry.category)}>{entry.category}</div>

                {/* Name */}
                <h3 style={{
                  fontSize: 'var(--text-h3, 1.5rem)',
                  fontWeight: 600,
                  margin: 0,
                  color: 'var(--color-text-primary, #1D2B22)',
                }}>
                  {entry.name}
                </h3>

                {/* Description */}
                <p style={{
                  fontSize: 'var(--text-body-sm, 0.875rem)',
                  color: 'var(--color-text-secondary, #6D6D6D)',
                  margin: 0,
                  lineHeight: 1.5,
                }}>
                  {entry.description}
                </p>

                {/* Status Badges Row */}
                <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap', alignItems: 'center' }}>
                  <span style={statusBadgeStyles[entry.status] || statusBadgeStyles.beta}>
                    {entry.status}
                  </span>
                  <span style={getApprovalBadgeStyle(entry.approvalStatus)}>
                    {entry.approvalStatus}
                  </span>
                  <span style={{ fontSize: 'var(--text-caption, 0.75rem)', display: 'inline-flex', alignItems: 'center', gap: '2px' }}>
                    {accessibilityIcons[entry.accessibilityStatus] || '\u2753'} <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>a11y</span>
                  </span>
                  <span style={getResponsiveStyle(entry.responsiveStatus)}>
                    {entry.responsiveStatus === 'pass' ? '\u2713' : entry.responsiveStatus === 'partial' ? '\u25D0' : '\u2717'} resp
                  </span>
                </div>

                {/* Version */}
                <div style={{ fontSize: 'var(--text-caption, 0.75rem)', color: 'var(--color-text-disabled, #A8A8A8)' }}>
                  v{entry.version}
                </div>

                {/* Actions */}
                <div style={{ display: 'flex', gap: '8px', marginTop: 'auto', paddingTop: '8px' }}>
                  <button
                    onClick={() => navigate(`/design-system/component/${entry.id}`)}
                    style={{
                      padding: '6px 14px',
                      fontSize: 'var(--text-body-sm, 0.875rem)',
                      fontWeight: 500,
                      background: 'var(--color-bg-primary-default, #2F6F4F)',
                      color: '#fff',
                      border: 'none',
                      borderRadius: 'var(--radius-button, 4px)',
                      cursor: 'pointer',
                      transition: 'background var(--transition-fast, 100ms)',
                    }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.background = 'var(--color-bg-primary-hover, #265D3F)';
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.background = 'var(--color-bg-primary-default, #2F6F4F)';
                    }}
                  >
                    View Details
                  </button>
                  <button
                    onClick={() => navigate(entry.previewPath)}
                    style={{
                      padding: '6px 14px',
                      fontSize: 'var(--text-body-sm, 0.875rem)',
                      fontWeight: 500,
                      background: 'transparent',
                      color: 'var(--color-text-primary, #1D2B22)',
                      border: '1px solid var(--color-border-default, #E3E6E3)',
                      borderRadius: 'var(--radius-button, 4px)',
                      cursor: 'pointer',
                      transition: 'all var(--transition-fast, 100ms)',
                    }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.background = 'var(--color-bg-surface, #FFFFFF)';
                      e.currentTarget.style.borderColor = 'var(--color-border-hover, #A5D6A7)';
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.background = 'transparent';
                      e.currentTarget.style.borderColor = 'var(--color-border-default, #E3E6E3)';
                    }}
                  >
                    Preview
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
