import { memo } from 'react';

interface RadarChartProps {
  data: { category: string; averageLevel: number; maxLevel: number }[];
  size?: number;
}

export const RadarChart = memo(function RadarChart({ data, size = 180 }: RadarChartProps) {
  const cx = size / 2;
  const cy = size / 2;
  const r = cx - 20;
  const n = data.length;
  const levels = 5;

  const grid = Array.from({ length: levels }, (_, li) => {
    const lr = (r / levels) * (li + 1);
    const pts = data.map((_, i) => {
      const angle = (Math.PI * 2 * i) / n - Math.PI / 2;
      return `${cx + lr * Math.cos(angle)},${cy + lr * Math.sin(angle)}`;
    }).join(' ');
    return <polygon key={li} points={pts} fill="none" stroke="var(--color-border-subtle)" strokeWidth={1} />;
  });

  const axes = data.map((_, i) => {
    const angle = (Math.PI * 2 * i) / n - Math.PI / 2;
    return <line key={i} x1={cx} y1={cy} x2={cx + r * Math.cos(angle)} y2={cy + r * Math.sin(angle)} stroke="var(--color-border-subtle)" strokeWidth={1} />;
  });

  const dataPts = data.map((d, i) => {
    const angle = (Math.PI * 2 * i) / n - Math.PI / 2;
    const val = (d.averageLevel / d.maxLevel) * r;
    return `${cx + val * Math.cos(angle)},${cy + val * Math.sin(angle)}`;
  }).join(' ');

  const labels = data.map((d, i) => {
    const angle = (Math.PI * 2 * i) / n - Math.PI / 2;
    const lx = cx + (r + 16) * Math.cos(angle);
    const ly = cy + (r + 16) * Math.sin(angle);
    return <text key={i} x={lx} y={ly} textAnchor="middle" dominantBaseline="central" fontSize={8} fill="var(--color-text-tertiary)">{d.category}</text>;
  });

  return (
    <svg width={size} height={size}>
      {grid}
      {axes}
      <polygon points={dataPts} fill="#2563eb33" stroke="#2563eb" strokeWidth={2} />
      {labels}
    </svg>
  );
});
