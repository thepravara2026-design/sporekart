import { useState } from 'react';

const ChevronUpIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M4 10l4-4 4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const ChevronDownIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M4 6l4 4 4-4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const ChevronLeftIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M10 4l-4 4 4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const ChevronRightIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M6 4l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const AlertIcon = () => (<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="1.5"/><path d="M12 7v5M12 16h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);

const sampleData = [
  { id: 1, name: 'Alice Johnson', email: 'alice@example.com', role: 'Admin', status: 'Active', lastLogin: '2026-07-10' },
  { id: 2, name: 'Bob Smith', email: 'bob@example.com', role: 'Editor', status: 'Active', lastLogin: '2026-07-09' },
  { id: 3, name: 'Carol White', email: 'carol@example.com', role: 'Viewer', status: 'Inactive', lastLogin: '2026-06-28' },
  { id: 4, name: 'David Brown', email: 'david@example.com', role: 'Admin', status: 'Active', lastLogin: '2026-07-11' },
  { id: 5, name: 'Eve Davis', email: 'eve@example.com', role: 'Editor', status: 'Active', lastLogin: '2026-07-10' },
  { id: 6, name: 'Frank Miller', email: 'frank@example.com', role: 'Viewer', status: 'Inactive', lastLogin: '2026-05-15' },
  { id: 7, name: 'Grace Wilson', email: 'grace@example.com', role: 'Editor', status: 'Active', lastLogin: '2026-07-08' },
  { id: 8, name: 'Hank Moore', email: 'hank@example.com', role: 'Admin', status: 'Active', lastLogin: '2026-07-11' },
  { id: 9, name: 'Ivy Taylor', email: 'ivy@example.com', role: 'Viewer', status: 'Inactive', lastLogin: '2026-04-20' },
  { id: 10, name: 'Jack Anderson', email: 'jack@example.com', role: 'Editor', status: 'Active', lastLogin: '2026-07-07' },
];

type SortKey = 'name' | 'email' | 'role' | 'status' | 'lastLogin';
type SortDir = 'asc' | 'desc';

const SortArrow = ({ dir }: { dir: 'asc' | 'desc' | null }) => (
  <span style={{ fontSize: '10px', marginLeft: '4px' }}>{dir === 'asc' ? ' ▲' : dir === 'desc' ? ' ▼' : ''}</span>
);

const Checkbox = ({ checked, onChange }: { checked: boolean; onChange: () => void }) => (
  <input type="checkbox" checked={checked} onChange={onChange} style={{ cursor: 'pointer' }} />
);

export default function TablesPreview() {
  const [sortKey, setSortKey] = useState<SortKey | null>(null);
  const [sortDir, setSortDir] = useState<SortDir>('asc');
  const [selected, setSelected] = useState<Set<number>>(new Set());
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const [expanded, setExpanded] = useState<Set<number>>(new Set());
  const [striped, setStriped] = useState(false);
  const [bordered, setBordered] = useState(false);

  const handleSort = (key: SortKey) => {
    if (sortKey === key) {
      setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'));
    } else {
      setSortKey(key);
      setSortDir('asc');
    }
  };

  const sorted = [...sampleData].sort((a, b) => {
    if (!sortKey) return 0;
    const aVal = a[sortKey];
    const bVal = b[sortKey];
    const cmp = aVal < bVal ? -1 : aVal > bVal ? 1 : 0;
    return sortDir === 'asc' ? cmp : -cmp;
  });

  const perPage = 5;
  const totalPages = Math.ceil(sorted.length / perPage);
  const paged = sorted.slice(page * perPage, (page + 1) * perPage);

  const allSelected = paged.length > 0 && paged.every((r) => selected.has(r.id));
  const toggleAll = () => {
    if (allSelected) {
      setSelected(new Set([...selected].filter((id) => !paged.some((r) => r.id === id))));
    } else {
      const next = new Set(selected);
      paged.forEach((r) => next.add(r.id));
      setSelected(next);
    }
  };
  const toggleRow = (id: number) => {
    const next = new Set(selected);
    if (next.has(id)) next.delete(id); else next.add(id);
    setSelected(next);
  };
  const toggleExpand = (id: number) => {
    const next = new Set(expanded);
    if (next.has(id)) next.delete(id); else next.add(id);
    setExpanded(next);
  };

  const thStyle: React.CSSProperties = {
    padding: '10px 12px', textAlign: 'left', fontSize: 'var(--text-sm)', fontWeight: 'var(--weight-semibold)',
    borderBottom: '2px solid var(--color-border-default)', cursor: 'pointer', userSelect: 'none',
    background: 'var(--color-bg-subtle, #f9fafb)', position: 'sticky', top: 0, zIndex: 1,
  };
  const tdStyle: React.CSSProperties = { padding: '10px 12px', fontSize: 'var(--text-sm)', borderBottom: '1px solid var(--color-border-subtle, #e5e7eb)' };

  const renderTable = () => (
    <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', minWidth: 600 }}>
        <thead>
          <tr>
            <th style={thStyle}><Checkbox checked={allSelected} onChange={toggleAll} /></th>
            <th style={thStyle} onClick={() => handleSort('name')}>Name<SortArrow dir={sortKey === 'name' ? sortDir : null} /></th>
            <th style={thStyle} onClick={() => handleSort('email')}>Email<SortArrow dir={sortKey === 'email' ? sortDir : null} /></th>
            <th style={thStyle} onClick={() => handleSort('role')}>Role<SortArrow dir={sortKey === 'role' ? sortDir : null} /></th>
            <th style={thStyle} onClick={() => handleSort('status')}>Status<SortArrow dir={sortKey === 'status' ? sortDir : null} /></th>
            <th style={thStyle} onClick={() => handleSort('lastLogin')}>Last Login<SortArrow dir={sortKey === 'lastLogin' ? sortDir : null} /></th>
            <th style={thStyle}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {paged.map((row, i) => (
            <tr key={row.id} style={{ background: striped && i % 2 === 1 ? 'var(--color-bg-subtle, #f9fafb)' : 'transparent' }}>
              <td style={tdStyle}><Checkbox checked={selected.has(row.id)} onChange={() => toggleRow(row.id)} /></td>
              <td style={tdStyle}>{row.name}</td>
              <td style={tdStyle}>{row.email}</td>
              <td style={tdStyle}>{row.role}</td>
              <td style={tdStyle}>
                <span style={{ fontSize: 'var(--text-caption)', padding: '2px 8px', borderRadius: 'var(--radius-sm)', background: row.status === 'Active' ? 'var(--color-bg-success-subtle, #dcfce7)' : 'var(--color-bg-subtle, #f3f4f6)', color: row.status === 'Active' ? 'var(--color-success, #16a34a)' : 'var(--color-text-tertiary)' }}>{row.status}</span>
              </td>
              <td style={tdStyle}>{row.lastLogin}</td>
              <td style={tdStyle}>
                <button onClick={() => toggleExpand(row.id)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-text-secondary)', padding: 0 }}>
                  {expanded.has(row.id) ? <ChevronUpIcon /> : <ChevronDownIcon />}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {expanded.size > 0 && Array.from(expanded).map((id) => {
        const row = sampleData.find((r) => r.id === id);
        if (!row) return null;
        return (
          <div key={`exp-${id}`} style={{ padding: '12px 16px', background: 'var(--color-bg-subtle, #f9fafb)', borderTop: '1px solid var(--color-border-subtle, #e5e7eb)', fontSize: 'var(--text-sm)' }}>
            <strong>Details for {row.name}:</strong> Email: {row.email} · Role: {row.role} · Status: {row.status} · Last Login: {row.lastLogin}
          </div>
        );
      })}
    </div>
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Tables</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Enterprise table with sorting, selection, pagination</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Table Controls</h2>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          <label style={{ fontSize: 'var(--text-sm)', display: 'flex', alignItems: 'center', gap: '4px' }}>
            <input type="checkbox" checked={striped} onChange={() => setStriped((v) => !v)} /> Striped
          </label>
          <label style={{ fontSize: 'var(--text-sm)', display: 'flex', alignItems: 'center', gap: '4px' }}>
            <input type="checkbox" checked={bordered} onChange={() => setBordered((v) => !v)} /> Bordered
          </label>
          <label style={{ fontSize: 'var(--text-sm)', display: 'flex', alignItems: 'center', gap: '4px' }}>
            <input type="checkbox" checked={loading} onChange={() => setLoading((v) => !v)} /> Loading
          </label>
          <label style={{ fontSize: 'var(--text-sm)', display: 'flex', alignItems: 'center', gap: '4px' }}>
            <input type="checkbox" checked={error} onChange={() => setError((v) => !v)} /> Error
          </label>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Data Table</h2>
        {loading ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {Array.from({ length: 5 }).map((_, i) => (
              <div key={i} style={{ display: 'flex', gap: '12px', padding: '10px 12px' }}>
                {Array.from({ length: 6 }).map((_, j) => (
                  <div key={j} style={{ flex: 1, height: 16, background: 'var(--color-bg-subtle, #f3f4f6)', borderRadius: '4px', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
                ))}
              </div>
            ))}
          </div>
        ) : error ? (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px', padding: '40px', textAlign: 'center' }}>
            <div style={{ color: 'var(--color-danger, #dc2626)' }}><AlertIcon /></div>
            <div style={{ fontWeight: 'var(--weight-semibold)' }}>Failed to load table data</div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>An error occurred while fetching data.</span>
            <button onClick={() => setError(false)} style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-sm)', padding: '6px 12px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>Retry</button>
          </div>
        ) : (
          <>
            {renderTable()}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-sm)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{selected.size} selected</span>
              <div style={{ display: 'flex', gap: '4px', alignItems: 'center' }}>
                <button disabled={page === 0} onClick={() => setPage((p) => Math.max(0, p - 1))} style={{ background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', padding: '4px 8px', cursor: page === 0 ? 'not-allowed' : 'pointer', opacity: page === 0 ? 0.5 : 1 }}><ChevronLeftIcon /></button>
                {Array.from({ length: totalPages }).map((_, i) => (
                  <button key={i} onClick={() => setPage(i)} style={{ background: i === page ? 'var(--color-bg-primary-default)' : 'var(--color-bg-surface-default)', color: i === page ? '#fff' : 'var(--color-text-primary)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', padding: '4px 10px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>{i + 1}</button>
                ))}
                <button disabled={page >= totalPages - 1} onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))} style={{ background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', padding: '4px 8px', cursor: page >= totalPages - 1 ? 'not-allowed' : 'pointer', opacity: page >= totalPages - 1 ? 0.5 : 1 }}><ChevronRightIcon /></button>
              </div>
            </div>
          </>
        )}
      </section>
    </div>
  );
}
