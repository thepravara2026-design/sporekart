import { Icon } from '../../../design-system/icons/Icon';

export interface CatalogPaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export function CatalogPagination({ currentPage, totalPages, onPageChange }: CatalogPaginationProps) {
  if (totalPages <= 1) return null;

  const pages = buildPageRange(currentPage, totalPages);

  return (
    <nav aria-label="Course catalog pagination" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: 'var(--space-2, 8px)', marginTop: 'var(--space-5, 24px)', flexWrap: 'wrap' }}>
      <button
        type="button"
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage <= 1}
        aria-label="Previous page"
        style={pageBtnStyle(currentPage > 1)}
      >
        <Icon name="chevron-left" size={16} color="currentColor" />
      </button>

      {pages.map((p, idx) =>
        p === '…' ? (
          <span key={`gap-${idx}`} style={{ color: 'var(--color-text-tertiary)', padding: '0 4px' }} aria-hidden="true">…</span>
        ) : (
          <button
            key={p}
            type="button"
            onClick={() => onPageChange(p as number)}
            aria-label={`Page ${p}`}
            aria-current={p === currentPage ? 'page' : undefined}
            style={pageBtnStyle(true, p === currentPage)}
          >
            {p}
          </button>
        ),
      )}

      <button
        type="button"
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage >= totalPages}
        aria-label="Next page"
        style={pageBtnStyle(currentPage < totalPages)}
      >
        <Icon name="chevron-right" size={16} color="currentColor" />
      </button>
    </nav>
  );
}

function buildPageRange(current: number, total: number): (number | '…')[] {
  const delta = 1;
  const range: (number | '…')[] = [];
  const left = Math.max(2, current - delta);
  const right = Math.min(total - 1, current + delta);

  range.push(1);
  if (left > 2) range.push('…');
  for (let i = left; i <= right; i++) range.push(i);
  if (right < total - 1) range.push('…');
  if (total > 1) range.push(total);
  return range;
}

function pageBtnStyle(enabled: boolean, active = false): React.CSSProperties {
  return {
    minWidth: 36,
    height: 36,
    padding: '0 10px',
    borderRadius: 'var(--radius-input)',
    border: `1px solid ${active ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-border-default)'}`,
    background: active ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-bg-background)',
    color: !enabled ? 'var(--color-text-tertiary)' : active ? '#fff' : 'var(--color-text-secondary)',
    cursor: enabled ? 'pointer' : 'default',
    fontSize: 'var(--text-body-sm)',
    fontWeight: active ? 700 : 500,
  };
}

export default CatalogPagination;
