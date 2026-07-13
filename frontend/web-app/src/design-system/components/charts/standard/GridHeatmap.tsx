import React from 'react';

export interface GridHeatmapDataItem {
  row: string;
  column: string;
  value: number;
}

export interface GridHeatmapProps {
  data: GridHeatmapDataItem[];
  rows: string[];
  columns: string[];
  colorScheme?: string[];
  className?: string;
  style?: React.CSSProperties;
}

const DEFAULT_COLORS = [
  'var(--color-neutral-100)',
  'var(--color-green-100)',
  'var(--color-green-300)',
  'var(--color-green-600)',
  'var(--color-green-800)',
];

const CELL = 40;
const LABEL_W = 100;
const LABEL_H = 28;

export const GridHeatmap: React.FC<GridHeatmapProps> = ({
  data, rows, columns,
  colorScheme, className = '', style,
}) => {
  const colors = colorScheme ?? DEFAULT_COLORS;

  const valueMap = new Map<string, number>();
  let maxVal = 0;
  for (const d of data) {
    const key = `${d.row}|${d.column}`;
    const v = Math.abs(d.value);
    valueMap.set(key, v);
    if (v > maxVal) maxVal = v;
  }

  const totalW = columns.length * CELL + LABEL_W + 60;
  const totalH = rows.length * CELL + LABEL_H + 40;

  const getColor = (v: number) => {
    if (v === 0 || !v) return colors[0];
    if (maxVal === 0) return colors[0];
    const idx = Math.min(Math.floor((v / maxVal) * (colors.length - 1)), colors.length - 1);
    return colors[idx] || colors[colors.length - 1];
  };

  const descText = `Grid heatmap: ${rows.length} rows x ${columns.length} columns, ${data.length} data points, max value ${maxVal}`;

  return (
    <svg
      className={className}
      style={{ width: totalW, height: totalH, ...style }}
      role="img"
      aria-label={`Grid heatmap: ${rows.length} rows by ${columns.length} columns`}
      viewBox={`0 0 ${totalW} ${totalH}`}
    >
      <desc>{descText}</desc>

      {/* Column headers */}
      <g aria-label="Columns" fontFamily="var(--font-family-sans)" fontSize="12" fill="var(--color-text-secondary)" textAnchor="middle">
        {columns.map((col, i) => (
          <text
            key={`ch${i}`}
            x={LABEL_W + i * CELL + CELL / 2}
            y={LABEL_H - 8}
            style={{ writingMode: 'horizontal-tb' }}
          >
            {col.length > 8 ? `${col.slice(0, 8)}...` : col}
          </text>
        ))}
      </g>

      {/* Row labels */}
      <g aria-label="Rows" fontFamily="var(--font-family-sans)" fontSize="12" fill="var(--color-text-secondary)" textAnchor="end">
        {rows.map((row, i) => (
          <text
            key={`rl${i}`}
            x={LABEL_W - 8}
            y={LABEL_H + i * CELL + CELL / 2 + 4}
          >
            {row.length > 12 ? `${row.slice(0, 12)}...` : row}
          </text>
        ))}
      </g>

      {/* Cells */}
      {rows.map((row, ri) =>
        columns.map((col, ci) => {
          const key = `${row}|${col}`;
          const val = valueMap.get(key) ?? 0;
          const x = LABEL_W + ci * CELL;
          const y = LABEL_H + ri * CELL;
          return (
            <rect
              key={`c${ri}-${ci}`}
              x={x} y={y}
              width={CELL} height={CELL}
              fill={getColor(val)}
              stroke="var(--color-bg-surface-default)"
              strokeWidth="1"
              role="graphics-symbol"
              aria-label={`${row}, ${col}: ${val}`}
            >
              <title>{`${row}, ${col}: ${val}`}</title>
            </rect>
          );
        })
      )}

      {/* Cell values */}
      <g aria-label="Values" fontFamily="var(--font-family-sans)" fontSize="11" fill="var(--color-text-on-primary)" textAnchor="middle" dominantBaseline="central">
        {rows.map((row, ri) =>
          columns.map((col, ci) => {
            const key = `${row}|${col}`;
            const val = valueMap.get(key);
            if (val === undefined || val === null) return null;
            const x = LABEL_W + ci * CELL + CELL / 2;
            const y = LABEL_H + ri * CELL + CELL / 2;
            return (
              <text key={`v${ri}-${ci}`} x={x} y={y}>
                {val % 1 === 0 ? val.toFixed(0) : val.toFixed(1)}
              </text>
            );
          })
        )}
      </g>

      {/* Legend */}
      <g aria-label="Legend" transform={`translate(${LABEL_W}, ${totalH - 30})`}>
        <text x="0" y="10" fontFamily="var(--font-family-sans)" fontSize="10" fill="var(--color-text-secondary)">Low</text>
        {colors.map((c, i) => (
          <rect key={`leg${i}`} x={30 + i * (18 + 2)} y="2" width="18" height="14" rx="2" fill={c} />
        ))}
        <text x={30 + colors.length * 20 + 4} y="10" fontFamily="var(--font-family-sans)" fontSize="10" fill="var(--color-text-secondary)">High</text>
      </g>
    </svg>
  );
};

GridHeatmap.displayName = 'GridHeatmap';
export default GridHeatmap;
