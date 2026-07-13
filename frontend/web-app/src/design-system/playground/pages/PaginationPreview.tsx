import { useState } from 'react';

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const PreviewBox = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const paginationRow: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '4px',
  fontFamily: 'var(--font-family)',
};

const pageBtn: React.CSSProperties = {
  minWidth: '32px',
  height: '32px',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-surface-default)',
  cursor: 'pointer',
  fontSize: 'var(--text-sm)',
  color: 'var(--color-text-primary)',
  padding: '0 8px',
};

const pageBtnActive: React.CSSProperties = {
  ...pageBtn,
  background: 'var(--color-bg-primary-default)',
  color: '#fff',
  borderColor: 'var(--color-bg-primary-default)',
};

const pageBtnDisabled: React.CSSProperties = {
  ...pageBtn,
  opacity: 0.4,
  cursor: 'not-allowed',
};

const ellipsis: React.CSSProperties = {
  padding: '0 4px',
  color: 'var(--color-text-tertiary)',
  fontSize: 'var(--text-sm)',
};

const ChevronLeft = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M9 3L5 7l4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const ChevronRight = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M5 3l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

function StandardPagination({ totalPages = 10 }: { totalPages?: number }) {
  const [page, setPage] = useState(1);
  const visible = (p: number) => {
    if (totalPages <= 7) return Array.from({ length: totalPages }, (_, i) => i + 1);
    const pages: (number | string)[] = [1];
    if (p > 3) pages.push('...');
    const start = Math.max(2, p - 1);
    const end = Math.min(totalPages - 1, p + 1);
    for (let i = start; i <= end; i++) pages.push(i);
    if (p < totalPages - 2) pages.push('...');
    pages.push(totalPages);
    return pages;
  };

  return (
    <div style={paginationRow}>
      <button disabled={page === 1} onClick={() => setPage(page - 1)} style={page === 1 ? pageBtnDisabled : pageBtn}>
        <ChevronLeft />
      </button>
      {visible(page).map((p, i) =>
        typeof p === 'string' ? (
          <span key={`e${i}`} style={ellipsis}>…</span>
        ) : (
          <button key={p} onClick={() => setPage(p)} style={p === page ? pageBtnActive : pageBtn}>
            {p}
          </button>
        )
      )}
      <button disabled={page === totalPages} onClick={() => setPage(page + 1)} style={page === totalPages ? pageBtnDisabled : pageBtn}>
        <ChevronRight />
      </button>
    </div>
  );
}

function CompactPagination() {
  const [page, setPage] = useState(1);
  const total = 10;
  return (
    <div style={{ ...paginationRow, gap: '8px' }}>
      <button disabled={page === 1} onClick={() => setPage(page - 1)} style={page === 1 ? pageBtnDisabled : { ...pageBtn, minWidth: 'auto', padding: '0 12px' }}>
        <ChevronLeft /> Prev
      </button>
      <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-primary)' }}>
        Page <strong>{page}</strong> of {total}
      </span>
      <button disabled={page === total} onClick={() => setPage(page + 1)} style={page === total ? pageBtnDisabled : { ...pageBtn, minWidth: 'auto', padding: '0 12px' }}>
        Next <ChevronRight />
      </button>
    </div>
  );
}

function WithPageSize() {
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const total = 100;
  const totalPages = Math.ceil(total / size);
  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '8px' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
        <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>Rows per page:</span>
        <select
          value={size}
          onChange={(e) => { setSize(Number(e.target.value)); setPage(1); }}
          style={{
            padding: '4px 8px',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            fontSize: 'var(--text-sm)',
            background: 'var(--color-bg-surface-default)',
          }}
        >
          {[10, 20, 50, 100].map((s) => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
      </div>
      <div style={{ ...paginationRow }}>
        <button disabled={page === 1} onClick={() => setPage(page - 1)} style={page === 1 ? pageBtnDisabled : pageBtn}>
          <ChevronLeft />
        </button>
        <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-primary)', padding: '0 8px' }}>
          {page} of {totalPages}
        </span>
        <button disabled={page === totalPages} onClick={() => setPage(page + 1)} style={page === totalPages ? pageBtnDisabled : pageBtn}>
          <ChevronRight />
        </button>
      </div>
    </div>
  );
}

function WithPageJump() {
  const [page, setPage] = useState(1);
  const total = 10;
  const [input, setInput] = useState('1');
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
      <div style={paginationRow}>
        <button disabled={page === 1} onClick={() => setPage(page - 1)} style={page === 1 ? pageBtnDisabled : pageBtn}>
          <ChevronLeft />
        </button>
        <span style={{ fontSize: 'var(--text-sm)', padding: '0 8px' }}>Page</span>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onBlur={() => { const v = parseInt(input, 10); if (v >= 1 && v <= total) setPage(v); else setInput(String(page)); }}
          onKeyDown={(e) => { if (e.key === 'Enter') { const v = parseInt(input, 10); if (v >= 1 && v <= total) setPage(v); else setInput(String(page)); } }}
          style={{
            width: '48px',
            padding: '4px 8px',
            textAlign: 'center',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            fontSize: 'var(--text-sm)',
          }}
        />
        <span style={{ fontSize: 'var(--text-sm)' }}>of {total}</span>
        <button disabled={page === total} onClick={() => setPage(page + 1)} style={page === total ? pageBtnDisabled : pageBtn}>
          <ChevronRight />
        </button>
      </div>
    </div>
  );
}

function ResponsivePagination() {
  const [page, setPage] = useState(1);
  const total = 10;
  const collapsed = total > 7;

  const getPages = () => {
    if (!collapsed) return Array.from({ length: total }, (_, i) => i + 1);
    const p: (number | string)[] = [1];
    if (page > 3) p.push('...');
    for (let i = Math.max(2, page - 1); i <= Math.min(total - 1, page + 1); i++) p.push(i);
    if (page < total - 2) p.push('...');
    p.push(total);
    return p;
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '8px' }}>
      <div style={paginationRow}>
        <button disabled={page === 1} onClick={() => setPage(page - 1)} style={page === 1 ? pageBtnDisabled : { ...pageBtn, minWidth: 'auto', padding: '0 8px', fontSize: 'var(--text-xs)' }}>Prev</button>
        {getPages().map((p, i) =>
          typeof p === 'string' ? (
            <span key={`e${i}`} style={ellipsis}>…</span>
          ) : (
            <button key={p} onClick={() => setPage(p)} style={p === page ? pageBtnActive : pageBtn}>
              {p}
            </button>
          )
        )}
        <button disabled={page === total} onClick={() => setPage(page + 1)} style={page === total ? pageBtnDisabled : { ...pageBtn, minWidth: 'auto', padding: '0 8px', fontSize: 'var(--text-xs)' }}>Next</button>
      </div>
      <span style={{ fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)' }}>Responsive — collapses on mobile</span>
    </div>
  );
}

function DisabledPagination() {
  return (
    <div style={paginationRow}>
      <button disabled style={pageBtnDisabled}><ChevronLeft /></button>
      {[1, 2, 3, '...', 10].map((p, i) =>
        typeof p === 'string' ? (
          <span key={`e${i}`} style={ellipsis}>…</span>
        ) : (
          <button key={p} disabled style={{ ...pageBtn, opacity: 0.4, cursor: 'not-allowed' }}>{p}</button>
        )
      )}
      <button disabled style={pageBtnDisabled}><ChevronRight /></button>
    </div>
  );
}

function SizeVariants() {
  const [page, setPage] = useState(1);
  const sizes = [
    { label: 'sm', style: { minWidth: '28px', height: '28px', fontSize: 'var(--text-xs)' } },
    { label: 'md', style: { minWidth: '32px', height: '32px', fontSize: 'var(--text-sm)' } },
    { label: 'lg', style: { minWidth: '40px', height: '40px', fontSize: 'var(--text-base)' } },
  ] as const;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
      {sizes.map((size) => (
        <div key={size.label} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', minWidth: '28px' }}>{size.label}</span>
          <div style={paginationRow}>
            <button disabled={page === 1} onClick={() => setPage(page - 1)} style={{ ...pageBtn, ...size.style, opacity: page === 1 ? 0.4 : 1, cursor: page === 1 ? 'not-allowed' : 'pointer' }}><ChevronLeft /></button>
            <button onClick={() => setPage(1)} style={{ ...size.style, border: 'none', background: 'transparent', cursor: 'pointer', fontSize: size.style.fontSize }}>1</button>
            <button onClick={() => setPage(2)} style={{ ...pageBtnActive, ...size.style }}>2</button>
            <button onClick={() => setPage(3)} style={{ ...size.style, border: 'none', background: 'transparent', cursor: 'pointer', fontSize: size.style.fontSize }}>3</button>
            <span style={ellipsis}>…</span>
            <button onClick={() => setPage(10)} style={{ ...size.style, border: 'none', background: 'transparent', cursor: 'pointer', fontSize: size.style.fontSize }}>10</button>
            <button disabled={page === 10} onClick={() => setPage(page + 1)} style={{ ...pageBtn, ...size.style, opacity: page === 10 ? 0.4 : 1, cursor: page === 10 ? 'not-allowed' : 'pointer' }}><ChevronRight /></button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default function PaginationPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Pagination Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All pagination variants</p>
      </div>

      <Section label="Standard Pagination">
        <PreviewBox label="Page 1 of 10 with page numbers">
          <StandardPagination />
        </PreviewBox>
      </Section>

      <Section label="Compact Pagination">
        <PreviewBox label="'Page 3 of 10' style">
          <CompactPagination />
        </PreviewBox>
      </Section>

      <Section label="With Page Size Selector">
        <PreviewBox label="10/20/50/100 selector">
          <WithPageSize />
        </PreviewBox>
      </Section>

      <Section label="With Page Jump Input">
        <PreviewBox label="Type a page number and press Enter">
          <WithPageJump />
        </PreviewBox>
      </Section>

      <Section label="Responsive">
        <PreviewBox label="Collapses on mobile viewport">
          <ResponsivePagination />
        </PreviewBox>
      </Section>

      <Section label="Disabled State">
        <PreviewBox label="All controls disabled">
          <DisabledPagination />
        </PreviewBox>
      </Section>

      <Section label="All Sizes">
        <PreviewBox label="sm, md, lg">
          <SizeVariants />
        </PreviewBox>
      </Section>
    </div>
  );
}
