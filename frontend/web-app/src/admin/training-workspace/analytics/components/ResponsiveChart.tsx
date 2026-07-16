import { memo, useEffect, useRef, useState } from 'react';
import type { ReactNode } from 'react';

export interface ResponsiveChartProps {
  height?: number;
  minHeight?: number;
  ariaLabel?: string;
  children: (dimensions: { width: number; height: number }) => ReactNode;
}

const ResponsiveChart = memo(function ResponsiveChart({
  height = 280,
  minHeight = 180,
  ariaLabel,
  children,
}: ResponsiveChartProps) {
  const ref = useRef<HTMLDivElement>(null);
  const [width, setWidth] = useState(640);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const update = () => setWidth(Math.max(240, el.clientWidth));
    update();
    if (typeof ResizeObserver === 'undefined') {
      window.addEventListener('resize', update);
      return () => window.removeEventListener('resize', update);
    }
    const ro = new ResizeObserver(update);
    ro.observe(el);
    return () => ro.disconnect();
  }, []);

  const resolvedHeight = Math.max(minHeight, height);

  return (
    <div
      ref={ref}
      role={ariaLabel ? 'img' : undefined}
      aria-label={ariaLabel}
      style={{ width: '100%', minHeight: resolvedHeight }}
    >
      {children({ width, height: resolvedHeight })}
    </div>
  );
});

export default ResponsiveChart;
