import React, { useState, useCallback } from 'react';
import { registry } from '../../icons/registry';

const SIZES = [16, 20, 24, 32, 48] as const;

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap)',
  padding: 'var(--space-page-y) var(--space-page-x)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  flexWrap: 'wrap',
  gap: '12px',
};

const headerInfoStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '4px',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h1)',
  fontWeight: 'var(--weight-bold)',
  margin: 0,
};

const countBadgeStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

const controlsRowStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: '12px',
  alignItems: 'center',
};

const searchInputStyle: React.CSSProperties = {
  padding: '8px 12px',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-input)',
  fontSize: 'var(--text-body)',
  background: 'var(--color-bg-surface)',
  color: 'var(--color-text-primary)',
  outline: 'none',
  minWidth: '220px',
};

const sizeBtnStyle = (active: boolean): React.CSSProperties => ({
  padding: '6px 12px',
  border: `1px solid var(--color-border-default)`,
  borderRadius: 'var(--radius-sm)',
  background: active ? 'var(--color-bg-primary-default)' : 'var(--color-bg-surface)',
  color: active ? 'var(--color-text-on-primary)' : 'var(--color-text-primary)',
  cursor: 'pointer',
  fontSize: 'var(--text-caption)',
  fontWeight: active ? 'var(--weight-semibold)' : 'var(--weight-normal)',
});

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(6, 1fr)',
  gap: '12px',
};

const iconCardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  gap: '8px',
  padding: '16px 8px',
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  border: '1px solid var(--color-border-default)',
  cursor: 'pointer',
  position: 'relative',
  transition: 'box-shadow 0.15s, border-color 0.15s',
};

const iconNameStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  textAlign: 'center',
  wordBreak: 'break-all',
  maxWidth: '100%',
};

const tooltipStyle: React.CSSProperties = {
  position: 'absolute',
  top: '-28px',
  left: '50%',
  transform: 'translateX(-50%)',
  background: 'var(--color-text-primary)',
  color: 'var(--color-bg-surface)',
  fontSize: '11px',
  padding: '3px 8px',
  borderRadius: 'var(--radius-sm)',
  whiteSpace: 'nowrap',
  pointerEvents: 'none',
  zIndex: 10,
};

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2)',
  fontWeight: 'var(--weight-semibold)',
  margin: 0,
};

const codeBlockStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-raised)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-md)',
  padding: '16px',
  fontFamily: 'var(--font-mono)',
  fontSize: 'var(--text-caption)',
  lineHeight: '1.6',
  overflowX: 'auto',
  whiteSpace: 'pre',
};

const overlayStyle: React.CSSProperties = {
  position: 'fixed',
  inset: 0,
  background: 'rgba(0,0,0,0.5)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  zIndex: 1000,
};

const panelStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface)',
  borderRadius: 'var(--radius-card)',
  padding: '32px',
  maxWidth: '480px',
  width: '90%',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  gap: '16px',
  boxShadow: 'var(--shadow-modal)',
};

const detailNameStyle: React.CSSProperties = {
  fontSize: 'var(--text-h3)',
  fontWeight: 'var(--weight-bold)',
  margin: 0,
};

const detailLabelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

const detailPathStyle: React.CSSProperties = {
  fontSize: 'var(--text-body)',
  fontFamily: 'var(--font-mono)',
  background: 'var(--color-bg-surface-raised)',
  padding: '4px 8px',
  borderRadius: 'var(--radius-sm)',
};

const copyBtnStyle: React.CSSProperties = {
  padding: '8px 20px',
  border: 'none',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-primary-default)',
  color: 'var(--color-text-on-primary)',
  cursor: 'pointer',
  fontWeight: 'var(--weight-semibold)',
  fontSize: 'var(--text-body)',
};

const closeBtnStyle: React.CSSProperties = {
  position: 'absolute',
  top: '16px',
  right: '20px',
  background: 'none',
  border: 'none',
  fontSize: '20px',
  cursor: 'pointer',
  color: 'var(--color-text-secondary)',
};

export default function IconLibrary() {
  const [search, setSearch] = useState('');
  const [size, setSize] = useState<number>(24);
  const [hoveredIcon, setHoveredIcon] = useState<string | null>(null);
  const [selectedIcon, setSelectedIcon] = useState<string | null>(null);
  const [copied, setCopied] = useState<string | null>(null);

  const iconNames = Array.from(registry.keys());
  const filtered = iconNames.filter((name) =>
    name.toLowerCase().includes(search.toLowerCase())
  );

  const handleCopy = useCallback(
    (name: string) => {
      navigator.clipboard.writeText(name).catch(() => {});
      setCopied(name);
      setTimeout(() => setCopied(null), 1500);
    },
    []
  );

  const IconComponent = selectedIcon ? registry.get(selectedIcon) : null;

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <div style={headerInfoStyle}>
          <h1 style={titleStyle}>Icon Library</h1>
          <span style={countBadgeStyle}>{iconNames.length} icons registered</span>
        </div>
      </div>

      <div style={controlsRowStyle}>
        <input
          type="text"
          placeholder="Search icons..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={searchInputStyle}
        />
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          Size:
        </span>
        {SIZES.map((s) => (
          <button
            key={s}
            onClick={() => setSize(s)}
            style={sizeBtnStyle(size === s)}
          >
            {s}
          </button>
        ))}
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>All Icons</h2>
        <div style={gridStyle as React.CSSProperties}>
          {filtered.map((name) => {
            const Icon = registry.get(name);
            if (!Icon) return null;
            return (
              <div
                key={name}
                style={iconCardStyle}
                onMouseEnter={() => setHoveredIcon(name)}
                onMouseLeave={() => setHoveredIcon(null)}
                onClick={() => setSelectedIcon(name)}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') setSelectedIcon(name);
                }}
                tabIndex={0}
                role="button"
                aria-label={`Icon ${name}`}
              >
                {hoveredIcon === name && (
                  <div style={tooltipStyle}>
                    {copied === name ? 'Copied!' : 'Copy name'}
                  </div>
                )}
                <Icon size={size} />
                <span style={iconNameStyle}>{name}</span>
              </div>
            );
          })}
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Usage Examples</h2>
        <div style={codeBlockStyle}>
{`import { Bell } from '../../design-system/icons/registry';
<Bell size={24} color="var(--color-primary-500)" aria-label="Notifications" />

import { useIcon } from '../../design-system/icons';
const Icon = useIcon('bell');
<Icon size={32} />

import { registry } from '../../design-system/icons/registry';
const DynamicIcon = registry.get('search');
<DynamicIcon size={20} />`}
        </div>
      </div>

      {selectedIcon && IconComponent && (
        <div
          style={overlayStyle}
          onClick={(e) => {
            if (e.target === e.currentTarget) setSelectedIcon(null);
          }}
        >
          <div style={{ ...panelStyle, position: 'relative' }}>
            <button
              style={closeBtnStyle}
              onClick={() => setSelectedIcon(null)}
              aria-label="Close detail panel"
            >
              ×
            </button>
            <IconComponent size={64} />
            <h3 style={detailNameStyle}>{selectedIcon}</h3>
            <span style={detailLabelStyle}>Import path</span>
            <code style={detailPathStyle}>
              ../../design-system/icons/registry
            </code>
            <span style={detailLabelStyle}>Available sizes</span>
            <span>{SIZES.join(', ')}px</span>
            <button
              style={copyBtnStyle}
              onClick={() => handleCopy(selectedIcon)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') handleCopy(selectedIcon);
              }}
            >
              {copied === selectedIcon ? 'Copied!' : 'Copy import code'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
