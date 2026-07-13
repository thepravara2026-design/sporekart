import { useMemo, useState, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { tokenManifest, TokenEntry } from './catalog/tokenManifest';

const categories = ['color', 'typography', 'spacing', 'radius', 'elevation', 'animation'] as const;
type Category = (typeof categories)[number];

const categoryMeta: Record<Category, { label: string; description: string; color: string }> = {
  color: { label: 'Colors', description: 'Primitive palette colors and semantic color tokens for backgrounds, text, borders, and data visualization.', color: '#4CAF50' },
  typography: { label: 'Typography', description: 'Font size, weight, line height, and letter spacing tokens that define the type scale.', color: '#2196F3' },
  spacing: { label: 'Spacing', description: 'Primitive spacing values and semantic spacing tokens for padding, margin, and gaps.', color: '#FF9800' },
  radius: { label: 'Radius', description: 'Border radius tokens that control the roundness of component corners.', color: '#9C27B0' },
  elevation: { label: 'Elevation', description: 'Box shadow and elevation tokens for surface depth and layering.', color: '#607D8B' },
  animation: { label: 'Animation', description: 'Duration and easing tokens that control motion and transitions.', color: '#E91E63' },
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

const backLinkStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--spacing-2xs)',
  color: 'var(--color-text-link, #2F6F4F)',
  textDecoration: 'none',
  fontSize: 'var(--text-body-sm, 0.875rem)',
  fontWeight: 500,
  alignSelf: 'flex-start',
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

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2, 1.875rem)',
  fontWeight: 600,
  margin: 0,
  color: 'var(--color-text-primary, #1D2B22)',
};

const previewCardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-md)',
  padding: 'var(--spacing-lg)',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #E3E6E3)',
  background: 'var(--color-bg-surface-default, #FFFFFF)',
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
  maxWidth: '400px',
};

const tableStyle: React.CSSProperties = {
  width: '100%',
  borderCollapse: 'collapse',
  fontSize: 'var(--text-body-sm, 0.875rem)',
};

const thStyle: React.CSSProperties = {
  textAlign: 'left',
  padding: '10px 12px',
  fontWeight: 600,
  color: 'var(--color-text-primary, #1D2B22)',
  borderBottom: '2px solid var(--color-border-default, #E3E6E3)',
  whiteSpace: 'nowrap',
};

const tdStyle: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border-default, #E3E6E3)',
  verticalAlign: 'middle',
};

const codeStyle: React.CSSProperties = {
  fontFamily: 'var(--font-family-mono, monospace)',
  fontSize: 'var(--text-caption, 0.75rem)',
  color: 'var(--color-text-code, #1e293b)',
  background: 'var(--color-bg-code, #f1f5f9)',
  padding: '2px 6px',
  borderRadius: 'var(--radius-sm, 3px)',
  wordBreak: 'break-all',
};

const copyBtnStyle: React.CSSProperties = {
  padding: '4px 10px',
  fontSize: 'var(--text-caption, 0.75rem)',
  fontWeight: 500,
  borderRadius: 'var(--radius-sm, 4px)',
  border: '1px solid var(--color-border-default, #E3E6E3)',
  background: 'var(--color-bg-surface-default, #FFFFFF)',
  color: 'var(--color-text-secondary, #6D6D6D)',
  cursor: 'pointer',
  transition: 'all 0.15s ease',
  whiteSpace: 'nowrap',
};

const badgeBase: React.CSSProperties = {
  display: 'inline-block',
  padding: '2px 8px',
  fontSize: 'var(--text-caption, 0.75rem)',
  fontWeight: 500,
  borderRadius: 'var(--radius-sm, 3px)',
  textTransform: 'capitalize',
};

const typeBadge: Record<string, React.CSSProperties> = {
  primitive: { ...badgeBase, background: 'var(--color-bg-surface-raised, #f1f5f9)', color: 'var(--color-text-secondary, #6D6D6D)' },
  semantic: { ...badgeBase, background: 'var(--color-bg-primary-subtle, #E8F5E9)', color: 'var(--color-bg-primary-default, #2F6F4F)' },
};

function ColorPreview({ tokens }: { tokens: TokenEntry[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--spacing-sm)' }}>
      {tokens.map((t) => (
        <div
          key={t.id}
          style={{
            display: 'flex',
            flexDirection: 'column',
            gap: 4,
            padding: 'var(--spacing-sm)',
            borderRadius: 'var(--radius-sm, 4px)',
            background: 'var(--color-bg-surface-raised, #f8fafc)',
          }}
        >
          <div style={{ width: '100%', height: 40, borderRadius: 'var(--radius-sm, 4px)', background: t.value, border: '1px solid var(--color-border-default, #E3E6E3)' }} />
          <code style={{ fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace' }}>{t.name}</code>
          <span style={{ fontSize: 'var(--text-caption, 0.75rem)', color: 'var(--color-text-secondary, #6D6D6D)', fontFamily: 'monospace' }}>{t.value}</span>
        </div>
      ))}
    </div>
  );
}

function TypographyPreview({ tokens }: { tokens: TokenEntry[] }) {
  const samples = tokens.filter((t) => t.category === 'typography' && t.name.includes('font-size'));
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-md)' }}>
      {samples.map((t) => (
        <div key={t.id} style={{ display: 'flex', alignItems: 'baseline', gap: 'var(--spacing-md)' }}>
          <code style={{ minWidth: 80, fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>
            {t.name.replace('--font-size-', '')}
          </code>
          <span style={{ fontSize: t.value, lineHeight: 1.4, color: 'var(--color-text-primary, #1D2B22)' }}>
            The quick brown fox jumps over the lazy dog
          </span>
        </div>
      ))}
    </div>
  );
}

function SpacingPreview({ tokens }: { tokens: TokenEntry[] }) {
  const primitives = tokens.filter((t) => t.type === 'primitive');
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-sm)' }}>
      {primitives.map((t) => {
        const num = parseInt(t.value, 10);
        return (
          <div key={t.id} style={{ display: 'flex', alignItems: 'center', gap: 'var(--spacing-md)' }}>
            <code style={{ minWidth: 80, fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>
              {t.name.replace('--spacing-', '')}
            </code>
            <div
              style={{
                height: isNaN(num) ? 16 : Math.max(num, 4),
                width: isNaN(num) ? 60 : Math.max(num * 3, 20),
                background: 'var(--color-bg-primary-default, #2F6F4F)',
                borderRadius: 'var(--radius-sm, 2px)',
                opacity: 0.5,
              }}
            />
            <span style={{ fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>{t.value}</span>
          </div>
        );
      })}
    </div>
  );
}

function RadiusPreview({ tokens }: { tokens: TokenEntry[] }) {
  return (
    <div style={{ display: 'flex', gap: 'var(--spacing-md)', flexWrap: 'wrap' }}>
      {tokens.map((t) => (
        <div key={t.id} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
          <div
            style={{
              width: 48,
              height: 48,
              borderRadius: t.value,
              background: 'var(--color-bg-primary-default, #2F6F4F)',
              opacity: 0.7,
            }}
          />
          <code style={{ fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>
            {t.value}
          </code>
          <span style={{ fontSize: '10px', fontFamily: 'monospace', color: 'var(--color-text-disabled, #A8A8A8)' }}>
            {t.name.replace('--radius-', '')}
          </span>
        </div>
      ))}
    </div>
  );
}

function ElevationPreview({ tokens }: { tokens: TokenEntry[] }) {
  return (
    <div style={{ display: 'flex', gap: 'var(--spacing-lg)', flexWrap: 'wrap' }}>
      {tokens.map((t) => (
        <div key={t.id} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
          <div
            style={{
              width: 64,
              height: 64,
              borderRadius: 'var(--radius-card, 8px)',
              background: 'var(--color-bg-surface-default, #FFFFFF)',
              boxShadow: t.value,
              border: '1px solid var(--color-border-default, #E3E6E3)',
            }}
          />
          <code style={{ fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>
            {t.name.replace('--elevation-', '').replace('--shadow-', '')}
          </code>
        </div>
      ))}
    </div>
  );
}

function AnimationPreview({ tokens }: { tokens: TokenEntry[] }) {
  const durations = tokens.filter((t) => t.name.includes('duration'));
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-md)' }}>
      <style>{`
        @keyframes token-pulse {
          0%, 100% { transform: scale(1); opacity: 1; }
          50% { transform: scale(0.5); opacity: 0.3; }
        }
        @keyframes token-slide {
          0% { transform: translateX(0); }
          50% { transform: translateX(24px); }
          100% { transform: translateX(0); }
        }
        @keyframes token-shimmer {
          0% { opacity: 0.3; }
          50% { opacity: 1; }
          100% { opacity: 0.3; }
        }
      `}</style>
      {durations.map((t) => {
        const animName = t.value === '0ms' ? 'none' : t.value === '1500ms' ? 'token-shimmer' : 'token-slide';
        return (
          <div key={t.id} style={{ display: 'flex', alignItems: 'center', gap: 'var(--spacing-md)' }}>
            <code style={{ minWidth: 100, fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>
              {t.name.replace('--duration-', '').replace('--easing-', '').replace('--transition-', '')}
            </code>
            <div
              style={{
                width: 16,
                height: 16,
                borderRadius: '50%',
                background: 'var(--color-bg-primary-default, #2F6F4F)',
                animation: t.value === '0ms' ? undefined : `${animName} ${t.value} infinite`,
              }}
            />
            <span style={{ fontSize: 'var(--text-caption, 0.75rem)', fontFamily: 'monospace', color: 'var(--color-text-secondary, #6D6D6D)' }}>{t.value}</span>
          </div>
        );
      })}
    </div>
  );
}

export default function TokenCategoryPage() {
  const { category } = useParams<{ category: string }>();
  const [searchQuery, setSearchQuery] = useState('');

  const isValid = category && (categories as readonly string[]).includes(category);
  const meta = isValid ? categoryMeta[category as Category] : null;

  const filteredTokens = useMemo(() => {
    if (!isValid) return [];
    const catTokens = tokenManifest.filter((t) => t.category === category);
    if (!searchQuery.trim()) return catTokens;
    const q = searchQuery.toLowerCase();
    return catTokens.filter((t) => t.name.toLowerCase().includes(q) || t.value.toLowerCase().includes(q));
  }, [category, searchQuery, isValid]);

  const renderPreview = () => {
    if (!isValid || !category) return null;
    const tokens = tokenManifest.filter((t) => t.category === category);
    switch (category as Category) {
      case 'color':
        return <ColorPreview tokens={tokens} />;
      case 'typography':
        return <TypographyPreview tokens={tokens} />;
      case 'spacing':
        return <SpacingPreview tokens={tokens} />;
      case 'radius':
        return <RadiusPreview tokens={tokens} />;
      case 'elevation':
        return <ElevationPreview tokens={tokens} />;
      case 'animation':
        return <AnimationPreview tokens={tokens} />;
    }
  };

  if (!isValid || !meta) {
    return (
      <div style={{ ...pageStyle, alignItems: 'center', justifyContent: 'center', minHeight: '60vh' }}>
        <h1 style={{ fontSize: 'var(--text-h1, 2.25rem)', fontWeight: 700, margin: 0, color: 'var(--color-text-primary, #1D2B22)' }}>
          Category Not Found
        </h1>
        <p style={{ color: 'var(--color-text-secondary, #6D6D6D)', fontSize: 'var(--text-body, 1rem)', margin: 0 }}>
          Invalid token category "{category}". Valid categories are: color, typography, spacing, radius, elevation, animation.
        </p>
        <Link
          to="/design-system/tokens"
          style={{
            padding: '8px 20px',
            background: 'var(--color-bg-primary-default, #2F6F4F)',
            color: '#fff',
            borderRadius: 'var(--radius-button, 4px)',
            textDecoration: 'none',
            fontWeight: 500,
            fontSize: 'var(--text-body, 1rem)',
          }}
        >
          Back to Token Explorer
        </Link>
      </div>
    );
  }

  return (
    <div style={pageStyle}>
      <Link to="/design-system/tokens" style={backLinkStyle}>
        {'\u2190'} Back to Token Explorer
      </Link>

      <section style={heroStyle}>
        <h1 style={heroTitleStyle}>{meta.label}</h1>
        <p style={heroDescStyle}>{meta.description}</p>
      </section>

      <section style={previewCardStyle}>
        <h2 style={sectionTitleStyle}>Preview</h2>
        {renderPreview()}
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-md)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--spacing-md)', flexWrap: 'wrap' }}>
          <h2 style={{ ...sectionTitleStyle, fontSize: 'var(--text-h3, 1.5rem)' }}>All {meta.label}</h2>
          <input
            type="text"
            placeholder="Filter by token name or value..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={searchInputStyle}
          />
        </div>

        {filteredTokens.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 'var(--spacing-2xl)', color: 'var(--color-text-secondary, #6D6D6D)' }}>
            No tokens match your filter.
          </div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={tableStyle}>
              <thead>
                <tr>
                  <th style={thStyle}>Token Name</th>
                  <th style={thStyle}>Value</th>
                  <th style={thStyle}>Type</th>
                  <th style={{ ...thStyle, textAlign: 'center' }}>Usage</th>
                  <th style={{ ...thStyle, textAlign: 'center' }}>Status</th>
                  <th style={thStyle}></th>
                </tr>
              </thead>
              <tbody>
                {filteredTokens.map((token) => (
                  <TokenRow key={token.id} token={token} />
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}

function TokenRow({ token }: { token: TokenEntry }) {
  const [copied, setCopied] = useState(false);

  const handleCopy = useCallback(() => {
    navigator.clipboard.writeText(token.name).then(() => {
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }).catch(() => {});
  }, [token.name]);

  return (
    <tr>
      <td style={tdStyle}>
        <code style={codeStyle}>{token.name}</code>
      </td>
      <td style={{ ...tdStyle, fontFamily: 'monospace', fontSize: 'var(--text-caption, 0.75rem)', color: 'var(--color-text-secondary, #6D6D6D)' }}>
        {token.value}
      </td>
      <td style={tdStyle}>
        <span style={typeBadge[token.type]}>{token.type}</span>
      </td>
      <td style={{ ...tdStyle, textAlign: 'center', color: 'var(--color-text-secondary, #6D6D6D)' }}>
        {token.usage}
      </td>
      <td style={{ ...tdStyle, textAlign: 'center' }}>
        {token.deprecated ? (
          <span style={{ ...badgeBase, background: 'var(--color-bg-danger-subtle, #FBE9E7)', color: 'var(--color-danger-600, #C62828)' }}>
            Deprecated
          </span>
        ) : (
          <span style={{ ...badgeBase, background: 'var(--color-bg-success-subtle, #E8F5E9)', color: 'var(--color-success-600, #2E7D32)' }}>
            Active
          </span>
        )}
      </td>
      <td style={tdStyle}>
        <button
          onClick={handleCopy}
          style={{
            ...copyBtnStyle,
            color: copied ? 'var(--color-text-success, #16a34a)' : undefined,
            borderColor: copied ? 'var(--color-border-success, #16a34a)' : undefined,
          }}
        >
          {copied ? 'Copied!' : 'Copy'}
        </button>
      </td>
    </tr>
  );
}
