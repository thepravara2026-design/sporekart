import React, { useState, useCallback } from 'react';
import { Copy } from '../../icons/registry';

export interface TokenDisplayProps {
  name: string;
  value: string;
  category: 'color' | 'typography' | 'spacing' | 'radius' | 'elevation' | 'animation';
  usageCount?: number;
  deprecated?: boolean;
  replacement?: string;
  onCopy?: (name: string) => void;
}

const cardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
  padding: '16px',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  background: 'var(--color-bg-surface-default, #ffffff)',
  fontFamily: 'var(--font-family-base, sans-serif)',
  position: 'relative',
  transition: 'box-shadow 0.15s ease',
};

const topRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'flex-start',
  justifyContent: 'space-between',
  gap: '8px',
};

const nameStyle: React.CSSProperties = {
  fontFamily: 'var(--font-family-mono, "SF Mono", Monaco, monospace)',
  fontSize: 'var(--text-code, 13px)',
  color: 'var(--color-text-code, #1e293b)',
  background: 'var(--color-bg-code, #f1f5f9)',
  padding: '2px 8px',
  borderRadius: 'var(--radius-sm, 3px)',
  fontWeight: 'var(--weight-medium, 500)',
  wordBreak: 'break-all',
};

const actionBtnStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: '28px',
  height: '28px',
  borderRadius: 'var(--radius-sm, 4px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  background: 'var(--color-bg-input, #ffffff)',
  color: 'var(--color-text-secondary)',
  cursor: 'pointer',
  outline: 'none',
  flexShrink: 0,
  transition: 'all 0.15s ease',
};

const valueStyle: React.CSSProperties = {
  fontSize: 'var(--text-body, 14px)',
  color: 'var(--color-text-secondary)',
  fontFamily: 'var(--font-family-mono, "SF Mono", Monaco, monospace)',
  wordBreak: 'break-all',
  lineHeight: 1.5,
};

const previewBoxBase: React.CSSProperties = {
  borderRadius: 'var(--radius-sm, 4px)',
  border: '1px solid var(--color-border-subtle, #f1f5f9)',
  overflow: 'hidden',
};

const badgeRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '8px',
  flexWrap: 'wrap',
};

const badgeBase: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: '4px',
  padding: '2px 8px',
  fontSize: 'var(--text-caption, 11px)',
  fontWeight: 'var(--weight-semibold, 600)',
  borderRadius: 'var(--radius-sm, 3px)',
  textTransform: 'uppercase',
  letterSpacing: '0.03em',
};

const deprecatedBadgeStyle: React.CSSProperties = {
  ...badgeBase,
  background: 'var(--color-bg-warning-subtle, #fffbeb)',
  color: 'var(--color-text-warning, #d97706)',
};

const usageBadgeStyle: React.CSSProperties = {
  ...badgeBase,
  background: 'var(--color-bg-surface-raised, #f1f5f9)',
  color: 'var(--color-text-tertiary, #64748b)',
};

const replacementStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption, 12px)',
  color: 'var(--color-text-warning, #d97706)',
};

const categoryPreviewStyles: Record<string, React.CSSProperties> = {
  color: {
    ...previewBoxBase,
    width: '100%',
    height: '48px',
  },
  typography: {
    ...previewBoxBase,
    padding: '12px 16px',
    background: 'var(--color-bg-surface-raised, #f8fafc)',
    fontSize: 'var(--token-value, 16px)',
    fontWeight: 'var(--weight-normal, 400)',
    lineHeight: 1.5,
    color: 'var(--color-text-primary)',
  },
  spacing: {
    ...previewBoxBase,
    background: 'var(--color-bg-brand-subtle, #eff6ff)',
    borderColor: 'var(--color-border-brand, #3b82f6)',
    height: '24px',
  },
  radius: {
    ...previewBoxBase,
    width: '64px',
    height: '64px',
    background: 'var(--color-bg-surface-raised, #f8fafc)',
  },
  elevation: {
    ...previewBoxBase,
    width: '100%',
    height: '48px',
    background: 'var(--color-bg-surface-default, #ffffff)',
  },
  animation: {
    ...previewBoxBase,
    width: '100%',
    height: '32px',
    background: 'var(--color-bg-surface-raised, #f8fafc)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
};

export function TokenDisplay({
  name,
  value,
  category,
  usageCount,
  deprecated,
  replacement,
  onCopy,
}: TokenDisplayProps) {
  const [justCopied, setJustCopied] = useState(false);

  const handleCopy = useCallback(() => {
    if (onCopy) {
      onCopy(name);
    }
    navigator.clipboard.writeText(name).then(() => {
      setJustCopied(true);
      setTimeout(() => setJustCopied(false), 2000);
    }).catch(() => {});
  }, [name, onCopy]);

  const renderCategoryPreview = () => {
    const base = categoryPreviewStyles[category];

    switch (category) {
      case 'color':
        return <div style={{ ...base, background: value }} />;

      case 'typography':
        return (
          <div style={base}>
            <span style={{ fontSize: value, lineHeight: 1.5 }}>
              The quick brown fox jumps over the lazy dog
            </span>
          </div>
        );

      case 'spacing': {
        const numericValue = parseInt(value, 10);
        return (
          <div
            style={{
              ...base,
              width: isNaN(numericValue) ? 'var(--token-value, 16px)' : `${numericValue}px`,
            }}
          />
        );
      }

      case 'radius':
        return (
          <div
            style={{
              ...base,
              borderRadius: value,
              background: 'var(--color-bg-brand-subtle, #eff6ff)',
              border: '1px solid var(--color-border-brand, #3b82f6)',
            }}
          />
        );

      case 'elevation':
        return <div style={{ ...base, boxShadow: value }} />;

      case 'animation':
        return (
          <div style={base}>
            <div
              style={{
                width: '16px',
                height: '16px',
                borderRadius: '50%',
                background: 'var(--color-bg-brand-default, #3b82f6)',
                animation: `${value} 1s infinite`,
              }}
            />
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <div style={cardStyle}>
      <div style={topRowStyle}>
        <code style={nameStyle}>{name}</code>
        <button
          onClick={handleCopy}
          style={{
            ...actionBtnStyle,
            color: justCopied ? 'var(--color-text-success, #16a34a)' : 'var(--color-text-secondary)',
            borderColor: justCopied ? 'var(--color-border-success, #16a34a)' : undefined,
          }}
          title={justCopied ? 'Copied!' : 'Copy token name'}
        >
          <Copy size={14} />
        </button>
      </div>

      <div style={valueStyle}>{value}</div>

      {renderCategoryPreview()}

      <div style={badgeRowStyle}>
        {deprecated && (
          <span style={deprecatedBadgeStyle}>
            Deprecated
          </span>
        )}
        {usageCount !== undefined && (
          <span style={usageBadgeStyle}>
            Used {usageCount} time{usageCount !== 1 ? 's' : ''}
          </span>
        )}
      </div>

      {deprecated && replacement && (
        <div style={replacementStyle}>
          Replace with: <code style={{ fontFamily: 'var(--font-family-mono, monospace)' }}>{replacement}</code>
        </div>
      )}
    </div>
  );
}

export default TokenDisplay;
