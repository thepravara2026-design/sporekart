import React, { memo, useMemo, useState, useCallback } from 'react';

export interface HeatMapProps {
  data: { row: string; col: string; value: number }[];
  rows: string[];
  cols: string[];
  title?: string;
}

function getColor(value: number, min: number, max: number): string {
  const range = max - min || 1;
  const ratio = (value - min) / range;
  const r = Math.round(ratio * 255);
  const g = Math.round((1 - ratio) * 255);
  return `rgb(${r}, ${g}, 50)`;
}

export const HeatMap: React.FC<HeatMapProps> = memo(({ data, rows, cols, title }) => {
  const [tooltip, setTooltip] = useState<{ x: number; y: number; text: string } | null>(null);

  const values = useMemo(() => data.map(d => d.value), [data]);
  const minVal = useMemo(() => Math.min(...values, 0), [values]);
  const maxVal = useMemo(() => Math.max(...values, 1), [values]);

  const valueMap = useMemo(() => {
    const map = new Map<string, number>();
    data.forEach(d => map.set(`${d.row}|${d.col}`, d.value));
    return map;
  }, [data]);

  const cellSize = 40;
  const labelWidth = 100;
  const headerHeight = 30;
  const padding = 8;
  const gap = 2;

  const svgWidth = labelWidth + cols.length * (cellSize + gap) + padding * 2;
  const svgHeight = headerHeight + rows.length * (cellSize + gap) + padding * 2;

  const handleMouseEnter = useCallback((e: React.MouseEvent<SVGRectElement>, row: string, col: string, value: number) => {
    const rect = (e.target as SVGRectElement).getBoundingClientRect();
    setTooltip({ x: rect.left + rect.width / 2, y: rect.top - 8, text: `${row} - ${col}: ${value}` });
  }, []);

  const handleMouseLeave = useCallback(() => setTooltip(null), []);

  return (
    <div role="img" aria-label={title || 'Heat map'} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, width: '100%', overflow: 'hidden', position: 'relative' }}>
      {title && <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', marginBottom: 12 }}>{title}</div>}
      <svg width="100%" height={svgHeight} viewBox={`0 0 ${svgWidth} ${svgHeight}`} style={{ display: 'block', overflow: 'visible' }}>
        {cols.map((col, i) => (
          <text
            key={`ch-${i}`}
            x={labelWidth + padding + i * (cellSize + gap) + cellSize / 2}
            y={padding + headerHeight - 8}
            textAnchor="middle"
            fill="var(--color-text-secondary)"
            fontSize="var(--text-body-xs)"
          >
            {col}
          </text>
        ))}
        {rows.map((row, i) => (
          <g key={`r-${i}`}>
            <text
              x={labelWidth + padding - 6}
              y={padding + headerHeight + i * (cellSize + gap) + cellSize / 2 + 4}
              textAnchor="end"
              fill="var(--color-text-secondary)"
              fontSize="var(--text-body-xs)"
            >
              {row}
            </text>
            {cols.map((col, j) => {
              const val = valueMap.get(`${row}|${col}`) ?? 0;
              const fill = getColor(val, minVal, maxVal);
              return (
                <rect
                  key={`c-${i}-${j}`}
                  x={labelWidth + padding + j * (cellSize + gap)}
                  y={padding + headerHeight + i * (cellSize + gap)}
                  width={cellSize}
                  height={cellSize}
                  rx={4}
                  fill={fill}
                  stroke="transparent"
                  strokeWidth={1}
                  style={{ cursor: 'pointer', transition: 'stroke 0.15s' }}
                  onMouseEnter={(e) => handleMouseEnter(e, row, col, val)}
                  onMouseLeave={handleMouseLeave}
                  onFocus={() => {}}
                />
              );
            })}
          </g>
        ))}
      </svg>
      {tooltip && (
        <div
          style={{
            position: 'fixed',
            top: tooltip.y,
            left: tooltip.x,
            transform: 'translate(-50%, -100%)',
            background: 'var(--color-bg-surface-raised)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body-xs)',
            padding: '4px 8px',
            borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border)',
            whiteSpace: 'nowrap',
            pointerEvents: 'none',
            zIndex: 1000,
          }}
          role="tooltip"
        >
          {tooltip.text}
        </div>
      )}
    </div>
  );
});

HeatMap.displayName = 'HeatMap';
export default HeatMap;
