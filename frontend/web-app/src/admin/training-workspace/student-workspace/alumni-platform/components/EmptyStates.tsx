import { memo } from 'react';
import type { EmptyStateType } from '../types';

const CONFIG: Record<string, { icon: string; title: string; message: string }> = {
  noPlacements: { icon: '💼', title: 'No Placements', message: 'No placement drives or placement data found matching your criteria.' },
  noInternships: { icon: '📋', title: 'No Internships', message: 'No internship opportunities available at this time.' },
  noJobs: { icon: '💼', title: 'No Job Openings', message: 'No job opportunities found matching your filters.' },
  noAlumni: { icon: '👥', title: 'No Alumni Found', message: 'No alumni matching your search criteria.' },
  noEvents: { icon: '📅', title: 'No Events', message: 'No alumni events scheduled at this time.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
};

export const EmptyState = memo(function EmptyState({ type, onClearFilters }: { type: EmptyStateType; onClearFilters?: () => void }) {
  const cfg = CONFIG[type] || CONFIG.noSearchResults;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: 'var(--space-8)', gap: 8, textAlign: 'center' }}>
      <div style={{ fontSize: 40 }}>{cfg.icon}</div>
      <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{cfg.title}</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0, maxWidth: 360 }}>{cfg.message}</p>
      {onClearFilters && (
        <button onClick={onClearFilters} style={{ marginTop: 8, padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}>
          Clear Filters
        </button>
      )}
    </div>
  );
});
