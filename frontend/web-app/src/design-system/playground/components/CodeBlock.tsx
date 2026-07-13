import React, { useState, useCallback } from 'react';
import { Copy } from '../../icons/registry';

export interface CodeBlockProps {
  code: string;
  language?: string;
  showCopy?: boolean;
  title?: string;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  overflow: 'hidden',
  fontFamily: 'var(--font-family-base, sans-serif)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  padding: '8px 16px',
  background: 'var(--color-bg-surface-raised, #f8fafc)',
  borderBottom: '1px solid var(--color-border-default, #e2e8f0)',
};

const titleTextStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption, 12px)',
  fontWeight: 'var(--weight-semibold, 600)',
  color: 'var(--color-text-tertiary, #64748b)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
};

const langBadgeStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption, 11px)',
  color: 'var(--color-text-tertiary, #94a3b8)',
  background: 'var(--color-bg-code, #f1f5f9)',
  padding: '2px 8px',
  borderRadius: 'var(--radius-sm, 3px)',
  fontFamily: 'var(--font-family-mono, "SF Mono", Monaco, monospace)',
};

const preStyle: React.CSSProperties = {
  margin: 0,
  padding: '16px',
  background: 'var(--color-bg-code-block, #0f172a)',
  overflowX: 'auto',
  lineHeight: 1.6,
};

const codeStyle: React.CSSProperties = {
  fontFamily: 'var(--font-family-mono, "SF Mono", Monaco, monospace)',
  fontSize: 'var(--text-code, 13px)',
  color: 'var(--color-text-code-block, #e2e8f0)',
  whiteSpace: 'pre',
  tabSize: 2,
};

const copyBtnStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '4px',
  padding: '4px 10px',
  fontSize: 'var(--text-caption, 12px)',
  borderRadius: 'var(--radius-sm, 4px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  background: 'var(--color-bg-input, #ffffff)',
  color: 'var(--color-text-secondary)',
  cursor: 'pointer',
  outline: 'none',
  transition: 'all 0.15s ease',
};

export function CodeBlock({
  code,
  language,
  showCopy = true,
  title,
}: CodeBlockProps) {
  const [copied, setCopied] = useState(false);

  const handleCopy = useCallback(() => {
    navigator.clipboard.writeText(code).then(() => {
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }).catch(() => {});
  }, [code]);

  return (
    <div style={wrapperStyle}>
      {(title || language || showCopy) && (
        <div style={headerStyle}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            {title && <span style={titleTextStyle}>{title}</span>}
            {language && <span style={langBadgeStyle}>{language}</span>}
          </div>
          {showCopy && (
            <button
              onClick={handleCopy}
              style={{
                ...copyBtnStyle,
                color: copied ? 'var(--color-text-success, #16a34a)' : 'var(--color-text-secondary)',
                borderColor: copied ? 'var(--color-border-success, #16a34a)' : 'var(--color-border-default, #e2e8f0)',
              }}
            >
              <Copy size={14} />
              {copied ? 'Copied!' : 'Copy'}
            </button>
          )}
        </div>
      )}
      <pre style={preStyle}>
        <code style={codeStyle}>{code}</code>
      </pre>
    </div>
  );
}

export default CodeBlock;
