import React, { memo, useMemo, useState } from 'react';
import { Checkbox } from '../../../../design-system/components/core/Checkbox';
import { Icon } from '../../../../design-system/icons/Icon';
import { Button } from '../../../../design-system/components/core/Button';
import { StatusBadge } from '../../../components/status';
import { LifecycleBadge } from '../components/LifecycleBadge';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import {
  STOCK_LABELS,
  brandNameById,
  categoryNameById,
  type CatalogProduct,
} from '../mock/catalogMock';
import { formatDate, formatPrice, productTableColumns } from './catalogColumns';
import type { SortOption } from './types';

interface ProductTableViewProps {
  products: CatalogProduct[];
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onToggleSelectAll: () => void;
  sort: SortOption;
  onSort: (sort: SortOption) => void;
  onPreview: (product: CatalogProduct) => void;
}

const thBase: React.CSSProperties = {
  position: 'sticky',
  top: 0,
  zIndex: 3,
  background: 'var(--color-bg-surface-raised)',
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-semibold)',
  textTransform: 'uppercase',
  letterSpacing: '0.04em',
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border)',
  whiteSpace: 'nowrap',
  textAlign: 'left',
};

const tdBase: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border)',
  fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-primary)',
  verticalAlign: 'middle',
  whiteSpace: 'nowrap',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
};

const Thumb = memo(function Thumb({ product }: { product: CatalogProduct }) {
  if (product.hasImages && product.thumbUrl) {
    return (
      <img
        src={product.thumbUrl}
        alt={product.name}
        loading="lazy"
        style={{ width: 40, height: 40, borderRadius: 'var(--radius-sm)', objectFit: 'cover', display: 'block' }}
        onError={(e) => {
          e.currentTarget.style.display = 'none';
        }}
      />
    );
  }
  return <Skeleton variant="rounded" width={40} height={40} />;
});

const stockVariant: Record<CatalogProduct['stockStatus'], 'success' | 'warning' | 'danger' | 'info'> = {
  in_stock: 'success',
  low_stock: 'warning',
  out_of_stock: 'danger',
  preorder: 'info',
};

export const ProductTableView: React.FC<ProductTableViewProps> = memo(function ProductTableView({
  products,
  selectedIds,
  onToggleSelect,
  onToggleSelectAll,
  sort,
  onSort,
  onPreview,
}) {
  const [hidden, setHidden] = useState<Set<string>>(new Set());
  const [columnsMenu, setColumnsMenu] = useState(false);
  const [rowMenu, setRowMenu] = useState<string | null>(null);

  const visibleColumns = useMemo(
    () => productTableColumns.filter((c) => !hidden.has(c.key)),
    [hidden],
  );

  const allSelected = products.length > 0 && products.every((p) => selectedIds.has(p.id));
  const someSelected = products.some((p) => selectedIds.has(p.id)) && !allSelected;

  const toggleColumn = (key: string) => {
    setHidden((prev) => {
      const next = new Set(prev);
      if (next.has(key)) next.delete(key);
      else next.add(key);
      return next;
    });
  };

  const handleHeaderSort = (col: (typeof productTableColumns)[number]) => {
    if (!col.sortable) return;
    if (col.sortAsc && sort === col.sortAsc && col.sortDesc) onSort(col.sortDesc);
    else if (col.sortAsc) onSort(col.sortAsc);
  };

  const sortIndicator = (col: (typeof productTableColumns)[number]) => {
    if (!col.sortable) return null;
    const isAsc = sort === col.sortAsc;
    const isDesc = sort === col.sortDesc && col.sortDesc !== col.sortAsc;
    if (!isAsc && !isDesc) return <span style={{ opacity: 0.3, marginLeft: 4 }}><Icon name="chevron-right" size={12} /></span>;
    return (
      <span style={{ marginLeft: 4, display: 'inline-flex', transform: isDesc ? 'rotate(90deg)' : 'rotate(-90deg)' }}>
        <Icon name="chevron-right" size={12} />
      </span>
    );
  };

  return (
    <div>
      <style>{`
        .sk-cat-table-desktop { display: block; }
        .sk-cat-table-mobile { display: none; }
        @media (max-width: 767px) {
          .sk-cat-table-desktop { display: none; }
          .sk-cat-table-mobile { display: flex; flex-direction: column; gap: var(--space-component-gap); }
        }
      `}</style>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 'var(--space-stack-xs)', position: 'relative' }}>
        <Button variant="ghost" size="sm" leftIcon={<Icon name="sliders" size={14} />} onClick={() => setColumnsMenu((v) => !v)}>
          Columns
        </Button>
        {columnsMenu && (
          <div
            role="menu"
            aria-label="Toggle columns"
            style={{
              position: 'absolute',
              top: '100%',
              right: 0,
              marginTop: 4,
              zIndex: 20,
              minWidth: 180,
              background: 'var(--color-surface)',
              border: '1px solid var(--color-border)',
              borderRadius: 'var(--radius-md)',
              boxShadow: 'var(--shadow-3)',
              padding: 8,
              display: 'flex',
              flexDirection: 'column',
              gap: 4,
            }}
          >
            {productTableColumns
              .filter((c) => c.header)
              .map((c) => (
                <Checkbox
                  key={c.key}
                  size="sm"
                  checked={!hidden.has(c.key)}
                  onChange={() => toggleColumn(c.key)}
                  label={c.header}
                />
              ))}
          </div>
        )}
      </div>

      <div
        className="sk-cat-table-desktop"
        style={{
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-md)',
          overflow: 'auto',
          maxHeight: '70vh',
        }}
      >
        <table style={{ width: '100%', borderCollapse: 'collapse', tableLayout: 'fixed', minWidth: 900 }}>
          <colgroup>
            {visibleColumns.map((c) => (
              <col key={c.key} style={{ width: c.width ? `${c.width}px` : undefined }} />
            ))}
          </colgroup>
          <thead>
            <tr>
              {visibleColumns.map((col, i) => {
                const isFirst = i === 0;
                const style: React.CSSProperties = {
                  ...thBase,
                  textAlign: col.align ?? 'left',
                  ...(isFirst ? { left: 0, zIndex: 4 } : {}),
                  cursor: col.sortable ? 'pointer' : 'default',
                };
                if (col.key === 'thumb') {
                  return (
                    <th key={col.key} style={style}>
                      <Checkbox
                        size="sm"
                        checked={allSelected}
                        indeterminate={someSelected}
                        onChange={onToggleSelectAll}
                        aria-label="Select all rows"
                      />
                    </th>
                  );
                }
                return (
                  <th
                    key={col.key}
                    style={style}
                    onClick={col.sortable ? () => handleHeaderSort(col) : undefined}
                    aria-sort={col.sortable ? (sort === col.sortAsc ? 'ascending' : sort === col.sortDesc ? 'descending' : undefined) : undefined}
                  >
                    <span style={{ display: 'inline-flex', alignItems: 'center' }}>
                      {col.header}
                      {sortIndicator(col)}
                    </span>
                  </th>
                );
              })}
            </tr>
          </thead>
          <tbody>
            {products.map((p) => {
              const selected = selectedIds.has(p.id);
              return (
                <tr
                  key={p.id}
                  onClick={() => onPreview(p)}
                  style={{
                    cursor: 'pointer',
                    background: selected ? 'var(--color-primary-alpha)' : 'transparent',
                  }}
                >
                  {visibleColumns.map((col, i) => {
                    const isFirst = i === 0;
                    const cellStyle: React.CSSProperties = {
                      ...tdBase,
                      textAlign: col.align ?? 'left',
                      ...(isFirst
                        ? { position: 'sticky', left: 0, zIndex: 2, background: selected ? 'var(--color-primary-alpha)' : 'var(--color-bg-surface-default)' }
                        : {}),
                    };
                    if (col.key === 'thumb') {
                      return (
                        <td key={col.key} style={cellStyle} onClick={(e) => e.stopPropagation()}>
                          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                            <Checkbox
                              size="sm"
                              checked={selected}
                              onChange={() => onToggleSelect(p.id)}
                              aria-label={`Select ${p.name}`}
                            />
                            <Thumb product={p} />
                          </div>
                        </td>
                      );
                    }
                    if (col.key === 'actions') {
                      return (
                        <td key={col.key} style={cellStyle} onClick={(e) => e.stopPropagation()}>
                          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: 2, position: 'relative' }}>
                            <Button variant="ghost" size="sm" aria-label={`Preview ${p.name}`} onClick={() => onPreview(p)}>
                              <Icon name="eye" size={16} />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              aria-label={`More actions for ${p.name}`}
                              onClick={() => setRowMenu((cur) => (cur === p.id ? null : p.id))}
                            >
                              <Icon name="more-horizontal" size={16} />
                            </Button>
                            {rowMenu === p.id && (
                              <div
                                role="menu"
                                style={{
                                  position: 'absolute',
                                  top: '100%',
                                  right: 0,
                                  zIndex: 30,
                                  minWidth: 150,
                                  background: 'var(--color-surface)',
                                  border: '1px solid var(--color-border)',
                                  borderRadius: 'var(--radius-md)',
                                  boxShadow: 'var(--shadow-3)',
                                  padding: 4,
                                  textAlign: 'left',
                                }}
                              >
                                {['View details', 'Duplicate', 'Archive', 'Export'].map((item) => (
                                  <button
                                    key={item}
                                    type="button"
                                    role="menuitem"
                                    onClick={() => setRowMenu(null)}
                                    style={{
                                      display: 'block',
                                      width: '100%',
                                      textAlign: 'left',
                                      padding: '6px 8px',
                                      background: 'transparent',
                                      border: 'none',
                                      cursor: 'pointer',
                                      fontSize: 'var(--text-body-sm)',
                                      color: 'var(--color-text-primary)',
                                      borderRadius: 'var(--radius-sm)',
                                    }}
                                  >
                                    {item}
                                  </button>
                                ))}
                              </div>
                            )}
                          </div>
                        </td>
                      );
                    }
                    return (
                      <td key={col.key} style={cellStyle}>
                        {col.render ? col.render(p) : null}
                      </td>
                    );
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <div className="sk-cat-table-mobile">
        {products.map((p) => {
          const selected = selectedIds.has(p.id);
          return (
            <div
              key={p.id}
              onClick={() => onPreview(p)}
              style={{
                display: 'flex',
                gap: 'var(--space-inline-xs)',
                padding: 12,
                border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)',
                background: selected ? 'var(--color-primary-alpha)' : 'var(--color-bg-surface-default)',
                cursor: 'pointer',
              }}
            >
              <div onClick={(e) => e.stopPropagation()} style={{ display: 'flex', alignItems: 'flex-start' }}>
                <Checkbox size="sm" checked={selected} onChange={() => onToggleSelect(p.id)} aria-label={`Select ${p.name}`} />
              </div>
              <Thumb product={p} />
              <div style={{ flex: 1, minWidth: 0, display: 'flex', flexDirection: 'column', gap: 4 }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                  {p.name}
                </span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{p.sku}</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: 6, flexWrap: 'wrap' }}>
                  <LifecycleBadge state={p.lifecycleState} />
                  <StatusBadge status={STOCK_LABELS[p.stockStatus]} variant={stockVariant[p.stockStatus]} />
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  <span>{categoryNameById[p.categoryId]} · {brandNameById[p.brandId]}</span>
                  <span style={{ fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{formatPrice(p.pricing.sellingPrice)}</span>
                </div>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Updated {formatDate(p.updatedAt)}</span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
});

export default ProductTableView;
