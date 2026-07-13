import { useMemo, useState, useCallback } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { tokenManifest, TokenEntry } from './catalog/tokenManifest';
import TokenDisplay from './components/TokenDisplay';

type TokenType = 'all' | 'primitive' | 'semantic';

const categories = ['color', 'typography', 'spacing', 'radius', 'elevation', 'animation'] as const;
type Category = (typeof categories)[number];

const categoryConfig: Record<Category, { label: string; color: string }> = {
  color: { label: 'Colors', color: '#4CAF50' },
  typography: { label: 'Typography', color: '#2196F3' },
  spacing: { label: 'Spacing', color: '#FF9800' },
  radius: { label: 'Radius', color: '#9C27B0' },
  elevation: { label: 'Elevation', color: '#607D8B' },
  animation: { label: 'Animation', color: '#E91E63' },
};

const pageStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap, var(--spacing-2xl))',
  padding: 'var(--space-page-y, var(--spacing-3xl)) var(--space-page-x, var(--spacing-2xl))',
  maxWidth: '1280px',
  margin: '0 auto',
  width: '100%',
};

const heroStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-xs)',
};

const heroTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h1, 2.25rem)',
  fontWeight: 700,
  margin: 0,
  color: 'var(--color-text-primary, #1D2B22)',
};

const heroDescStyle: React.CSSProperties = {
  fontSize: 'var(--text-body, 1rem)',
  color: 'var(--color-text-secondary, #6D6D6D)',
  margin: 0,
  lineHeight: 1.6,
};

const statsRowStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: 'var(--spacing-md)',
};

const statCardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-2xs)',
  padding: 'var(--spacing-md) var(--spacing-lg)',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #E3E6E3)',
  background: 'var(--color-bg-surface-default, #FFFFFF)',
  minWidth: '140px',
};

const statValueStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2, 1.875rem)',
  fontWeight: 700,
  color: 'var(--color-text-primary, #1D2B22)',
  lineHeight: 1,
};

const statLabelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption, 0.75rem)',
  color: 'var(--color-text-secondary, #6D6D6D)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
  fontWeight: 500,
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2, 1.875rem)',
  fontWeight: 600,
  margin: 0,
  color: 'var(--color-text-primary, #1D2B22)',
};

const categoryCardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-sm)',
  padding: 'var(--spacing-lg)',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #E3E6E3)',
  background: 'var(--color-bg-surface-default, #FFFFFF)',
  transition: 'box-shadow 0.15s ease, transform 0.15s ease',
};

const categoryCardHeaderStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  gap: 'var(--spacing-xs)',
};

const categoryLabelStyle: React.CSSProperties = {
  fontSize: 'var(--text-h3, 1.5rem)',
  fontWeight: 600,
  margin: 0,
  color: 'var(--color-text-primary, #1D2B22)',
};

const categoryCountStyle: React.CSSProperties = {
  fontSize: 'var(--text-body-sm, 0.875rem)',
  color: 'var(--color-text-secondary, #6D6D6D)',
  fontWeight: 500,
};

const exploreLinkStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--spacing-2xs)',
  fontSize: 'var(--text-body-sm, 0.875rem)',
  fontWeight: 500,
  color: 'var(--color-text-link, #2F6F4F)',
  textDecoration: 'none',
  alignSelf: 'flex-start',
};

const filterRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--spacing-md)',
  flexWrap: 'wrap',
};

const filterBtnBase: React.CSSProperties = {
  padding: '6px 16px',
  borderRadius: 'var(--radius-pill, 9999px)',
  fontSize: 'var(--text-body-sm, 0.875rem)',
  fontWeight: 500,
  border: '1px solid var(--color-border-default, #E3E6E3)',
  background: 'transparent',
  color: 'var(--color-text-secondary, #6D6D6D)',
  cursor: 'pointer',
  transition: 'all 0.15s ease',
};

const filterBtnActive: React.CSSProperties = {
  ...filterBtnBase,
  background: 'var(--color-bg-primary-default, #2F6F4F)',
  color: '#fff',
  borderColor: 'var(--color-bg-primary-default, #2F6F4F)',
};

const searchInputStyle: React.CSSProperties = {
  padding: '6px 12px',
  borderRadius: 'var(--radius-input, 6px)',
  border: '1px solid var(--color-border-default, #E3E6E3)',
  fontSize: 'var(--text-body-sm, 0.875rem)',
  color: 'var(--color-text-primary, #1D2B22)',
  background: 'var(--color-bg-surface-default, #FFFFFF)',
  outline: 'none',
  minWidth: '240px',
  flex: 1,
  maxWidth: '400px',
};

const tokenGridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
  gap: 'var(--spacing-md)',
};

function CategoryPreview({ category, tokens }: { category: Category; tokens: TokenEntry[] }) {
  const previewTokens = tokens.slice(0, 5);

  switch (category) {
    case 'color':
      return (
        <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
          {previewTokens.map((t) => (
            <div
              key={t.id}
              style={{
                width: 28,
                height: 28,
                borderRadius: 'var(--radius-sm, 4px)',
                background: t.value,
                border: '1px solid var(--color-border-default, #E3E6E3)',
              }}
              title={t.name}
            />
          ))}
        </div>
      );
    case 'typography':
      return (
        <div style={{ color: 'var(--color-text-primary, #1D2B22)' }}>
          {previewTokens.slice(0, 3).map((t) => (
            <div key={t.id} style={{ fontSize: t.value, lineHeight: 1.4, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
              Aa
            </div>
          ))}
        </div>
      );
    case 'spacing':
      return (
        <div style={{ display: 'flex', gap: 4, alignItems: 'flex-end' }}>
          {previewTokens.map((t) => {
            const num = parseInt(t.value, 10);
            return (
              <div
                key={t.id}
                style={{
                  width: isNaN(num) ? 16 : Math.max(num, 4),
                  height: isNaN(num) ? 16 : Math.max(num, 4),
                  background: 'var(--color-bg-primary-default, #2F6F4F)',
                  borderRadius: 'var(--radius-sm, 2px)',
                  opacity: 0.6,
                }}
                title={`${t.name}: ${t.value}`}
              />
            );
          })}
        </div>
      );
    case 'radius':
      return (
        <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
          {previewTokens.map((t) => (
            <div
              key={t.id}
              style={{
                width: 24,
                height: 24,
                borderRadius: t.value,
                background: 'var(--color-bg-surface-raised, #f1f5f9)',
                border: '1px solid var(--color-border-default, #E3E6E3)',
              }}
              title={`${t.name}: ${t.value}`}
            />
          ))}
        </div>
      );
    case 'elevation':
      return (
        <div style={{ display: 'flex', gap: 8 }}>
          {previewTokens.slice(0, 3).map((t) => (
            <div
              key={t.id}
              style={{
                width: 36,
                height: 36,
                borderRadius: 'var(--radius-sm, 4px)',
                background: 'var(--color-bg-surface-default, #FFFFFF)',
                boxShadow: t.value,
              }}
              title={`${t.name}: ${t.value}`}
            />
          ))}
        </div>
      );
    case 'animation':
      return (
        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          {previewTokens.slice(0, 3).map((t) => {
            const isDuration = t.name.includes('duration');
            const isEasing = t.name.includes('easing');
            const animName = isDuration ? 'pulse' : isEasing ? 'slide' : 'none';
            return (
              <div key={t.id} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                <div
                  style={{
                    width: 16,
                    height: 16,
                    borderRadius: '50%',
                    background: 'var(--color-bg-primary-default, #2F6F4F)',
                    animation: isDuration ? `${animName} ${t.value} infinite` : undefined,
                    transition: isEasing ? `transform ${t.value}` : undefined,
                  }}
                  title={`${t.name}: ${t.value}`}
                />
                <style>{`
                  @keyframes pulse {
                    0%, 100% { transform: scale(1); opacity: 1; }
                    50% { transform: scale(0.6); opacity: 0.5; }
                  }
                  @keyframes slide {
                    0% { transform: translateX(0); }
                    50% { transform: translateX(8px); }
                    100% { transform: translateX(0); }
                  }
                `}</style>
                <span style={{ fontSize: '10px', color: 'var(--color-text-secondary, #6D6D6D)' }}>
                  {t.value}
                </span>
              </div>
            );
          })}
        </div>
      );
  }
}

export default function TokenExplorer() {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialType = searchParams.get('type') as TokenType | null;
  const [typeFilter, setTypeFilter] = useState<TokenType>(
    initialType === 'primitive' || initialType === 'semantic' ? initialType : 'all',
  );
  const [searchQuery, setSearchQuery] = useState('');

  const handleTypeChange = useCallback(
    (type: TokenType) => {
      setTypeFilter(type);
      if (type === 'all') {
        setSearchParams({});
      } else {
        setSearchParams({ type });
      }
    },
    [setSearchParams],
  );

  const filteredTokens = useMemo(() => {
    let result = tokenManifest;
    if (typeFilter !== 'all') {
      result = result.filter((t) => t.type === typeFilter);
    }
    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase();
      result = result.filter((t) => t.name.toLowerCase().includes(q) || t.value.toLowerCase().includes(q));
    }
    return result;
  }, [typeFilter, searchQuery]);

  const stats = useMemo(() => {
    const total = tokenManifest.length;
    const primitives = tokenManifest.filter((t) => t.type === 'primitive').length;
    const semantic = tokenManifest.filter((t) => t.type === 'semantic').length;
    const deprecated = tokenManifest.filter((t) => t.deprecated).length;
    return { total, primitives, semantic, deprecated };
  }, []);

  return (
    <div style={pageStyle}>
      <section style={heroStyle}>
        <h1 style={heroTitleStyle}>Design Token Explorer</h1>
        <p style={heroDescStyle}>
          Browse, search, and inspect all design tokens powering the SporeKart Design System.
          Tokens are organized by category and split between primitive values and semantic aliases.
        </p>
      </section>

      <section>
        <div style={statsRowStyle}>
          <div style={statCardStyle}>
            <span style={statValueStyle}>{stats.total}</span>
            <span style={statLabelStyle}>Total Tokens</span>
          </div>
          <div style={statCardStyle}>
            <span style={statValueStyle}>{stats.primitives}</span>
            <span style={statLabelStyle}>Primitives</span>
          </div>
          <div style={statCardStyle}>
            <span style={statValueStyle}>{stats.semantic}</span>
            <span style={statLabelStyle}>Semantic</span>
          </div>
          <div style={statCardStyle}>
            <span style={{ ...statValueStyle, color: stats.deprecated > 0 ? 'var(--color-danger-500, #EF5350)' : undefined }}>
              {stats.deprecated}
            </span>
            <span style={statLabelStyle}>Deprecated</span>
          </div>
        </div>
      </section>

      <section>
        <h2 style={sectionTitleStyle}>Categories</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--spacing-md)', marginTop: 'var(--spacing-md)' }}>
          {categories.map((cat) => {
            const tokens = tokenManifest.filter((t) => t.category === cat);
            const config = categoryConfig[cat];
            return (
              <div
                key={cat}
                style={categoryCardStyle}
                onMouseEnter={(e) => {
                  e.currentTarget.style.boxShadow = 'var(--shadow-card-hover, var(--elevation-2))';
                  e.currentTarget.style.transform = 'translateY(-2px)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.boxShadow = 'none';
                  e.currentTarget.style.transform = 'none';
                }}
              >
                <div style={categoryCardHeaderStyle}>
                  <h3 style={categoryLabelStyle}>{config.label}</h3>
                  <span style={categoryCountStyle}>{tokens.length} token{tokens.length !== 1 ? 's' : ''}</span>
                </div>
                <CategoryPreview category={cat} tokens={tokens} />
                <Link to={`/design-system/tokens/${cat}`} style={exploreLinkStyle}>
                  Explore {'\u2192'}
                </Link>
              </div>
            );
          })}
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-md)' }}>
        <div style={filterRowStyle}>
          {(['all', 'primitive', 'semantic'] as const).map((type) => (
            <button
              key={type}
              onClick={() => handleTypeChange(type)}
              style={typeFilter === type ? filterBtnActive : filterBtnBase}
            >
              {type === 'all' ? 'All' : type.charAt(0).toUpperCase() + type.slice(1)}
            </button>
          ))}
          <input
            type="text"
            placeholder="Search tokens by name or value..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={searchInputStyle}
          />
        </div>

        {filteredTokens.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 'var(--spacing-2xl)', color: 'var(--color-text-secondary, #6D6D6D)' }}>
            No tokens match your search.
          </div>
        ) : (
          <div style={tokenGridStyle}>
            {filteredTokens.map((token) => (
              <TokenDisplay
                key={token.id}
                name={token.name}
                value={token.value}
                category={token.category}
                usageCount={token.usage}
                deprecated={token.deprecated}
                replacement={token.replacement}
              />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
