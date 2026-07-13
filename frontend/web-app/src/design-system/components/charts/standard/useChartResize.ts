import { useState, useEffect, useRef } from 'react';

interface ChartSize {
  width: number;
  height: number;
}

export function useChartResize(): { containerRef: React.RefObject<HTMLDivElement | null>; size: ChartSize } {
  const containerRef = useRef<HTMLDivElement | null>(null);
  const [size, setSize] = useState<ChartSize>({ width: 600, height: 400 });

  useEffect(() => {
    const el = containerRef.current;
    if (!el) return;
    const observer = new ResizeObserver((entries) => {
      const entry = entries[0];
      if (entry) {
        const { width, height } = entry.contentRect;
        setSize({ width: Math.round(width), height: Math.round(height) });
      }
    });
    observer.observe(el);
    return () => observer.disconnect();
  }, []);

  return { containerRef, size };
}
