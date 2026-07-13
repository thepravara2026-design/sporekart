import React from 'react';

export interface CalendarHeatmapDataItem {
  date: string;
  value: number;
}

export interface CalendarHeatmapProps {
  data: CalendarHeatmapDataItem[];
  year?: number;
  colorScheme?: string[];
  emptyColor?: string;
  className?: string;
  style?: React.CSSProperties;
}

function getDateInfo(dateStr: string) {
  const d = new Date(dateStr);
  return {
    day: d.getDay(),
    week: getWeekNumber(d),
    month: d.getMonth(),
    monthName: d.toLocaleString('en-US', { month: 'short' }),
    date: d,
  };
}

function getWeekNumber(d: Date) {
  const start = new Date(d.getFullYear(), 0, 1);
  const diff = d.getTime() - start.getTime();
  return Math.ceil((diff / 86400000 + start.getDay() + 1) / 7);
}

function getYearRange(year: number) {
  const start = new Date(year, 0, 1);
  const end = new Date(year, 11, 31);
  const days: string[] = [];
  for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
    days.push(d.toISOString().slice(0, 10));
  }
  return days;
}

const DEFAULT_COLORS = [
  'var(--color-neutral-100)',
  'var(--color-data-viz-1)',
  'var(--color-green-300)',
  'var(--color-green-600)',
  'var(--color-green-800)',
];

const CELL = 14;
const GAP = 2;
const LABEL_W = 32;
const HEADER_H = 20;

export const CalendarHeatmap: React.FC<CalendarHeatmapProps> = ({
  data, year,
  colorScheme, emptyColor,
  className = '', style,
}) => {
  const y = year ?? new Date().getFullYear();
  const allDays = getYearRange(y);
  const colors = colorScheme ?? DEFAULT_COLORS;
  const empty = emptyColor ?? 'var(--color-neutral-100)';

  const valueMap = new Map<string, number>();
  let maxVal = 0;
  for (const d of data) {
    const v = Math.abs(d.value);
    valueMap.set(d.date, v);
    if (v > maxVal) maxVal = v;
  }

  const weeks: { date: string; week: number; day: number; month: number; }[][] = [];
  let currentWeek: { date: string; week: number; day: number; month: number; }[] = [];
  let prevWeek = -1;
  let weekIdx = 0;

  for (const dateStr of allDays) {
    const info = getDateInfo(dateStr);
    if (info.week !== prevWeek && currentWeek.length > 0) {
      weeks.push(currentWeek);
      currentWeek = [];
      weekIdx++;
    }
    currentWeek.push({ date: dateStr, week: weekIdx, day: info.day, month: info.month });
    prevWeek = info.week;
  }
  if (currentWeek.length > 0) weeks.push(currentWeek);

  const totalW = weeks.length * (CELL + GAP) + LABEL_W;
  const totalH = 7 * (CELL + GAP) + HEADER_H;

  const getColor = (v: number) => {
    if (v === 0 || !v) return empty;
    if (maxVal === 0) return colors[0];
    const idx = Math.min(Math.floor((v / maxVal) * (colors.length - 1)), colors.length - 1);
    return colors[idx] || colors[colors.length - 1];
  };

  const monthsSeen = new Set<number>();
  const monthLabels: { month: number; label: string; x: number }[] = [];

  const descText = `${data.length} days of data in ${y}, max value ${maxVal}`;

  return (
    <svg
      className={className}
      style={{ width: totalW, height: totalH, ...style }}
      role="img"
      aria-label={`Calendar heatmap for ${y}: ${descText}`}
      viewBox={`0 0 ${totalW} ${totalH}`}
    >
      <desc>{descText}</desc>

      {/* Month labels */}
      <g aria-label="Months" fontFamily="var(--font-family-sans)" fontSize="10" fill="var(--color-text-secondary)">
        {weeks.map((w, wi) => {
          if (w.length > 0) {
            const m = w[0].month;
            if (!monthsSeen.has(m)) {
              monthsSeen.add(m);
              const x = LABEL_W + wi * (CELL + GAP);
              monthLabels.push({ month: m, label: w[0].date ? new Date(w[0].date).toLocaleString('en-US', { month: 'short' }) : '', x });
              return <text key={`m${m}`} x={x} y={HEADER_H - 6}>{new Date(w[0].date).toLocaleString('en-US', { month: 'short' })}</text>;
            }
          }
          return null;
        })}
      </g>

      {/* Day labels */}
      <g aria-label="Days" fontFamily="var(--font-family-sans)" fontSize="9" fill="var(--color-text-secondary)" textAnchor="end">
        {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((d, i) => (
          <text key={`dl${i}`} x={LABEL_W - 4} y={HEADER_H + i * (CELL + GAP) + CELL - 2}>{d}</text>
        ))}
      </g>

      {/* Cells */}
      {weeks.map((w, wi) =>
        w.map((dayInfo) => {
          const val = valueMap.get(dayInfo.date) ?? 0;
          const x = LABEL_W + wi * (CELL + GAP);
          const y = HEADER_H + dayInfo.day * (CELL + GAP);
          return (
            <rect
              key={dayInfo.date}
              x={x} y={y}
              width={CELL} height={CELL}
              rx="2"
              fill={getColor(val)}
              role="graphics-symbol"
              aria-label={`${dayInfo.date}: ${val}`}
            >
              <title>{`${dayInfo.date}: ${val}`}</title>
            </rect>
          );
        })
      )}

      {/* Legend */}
      <g aria-label="Legend" transform={`translate(${totalW - 120}, ${totalH - 16})`}>
        <text x="0" y="10" fontFamily="var(--font-family-sans)" fontSize="9" fill="var(--color-text-secondary)">Less</text>
        {colors.map((c, i) => (
          <rect key={`leg${i}`} x={30 + i * (CELL + 2)} y="2" width={CELL} height={CELL} rx="2" fill={c} />
        ))}
        <text x={30 + colors.length * (CELL + 2) + 4} y="10" fontFamily="var(--font-family-sans)" fontSize="9" fill="var(--color-text-secondary)">More</text>
      </g>
    </svg>
  );
};

CalendarHeatmap.displayName = 'CalendarHeatmap';
export default CalendarHeatmap;
