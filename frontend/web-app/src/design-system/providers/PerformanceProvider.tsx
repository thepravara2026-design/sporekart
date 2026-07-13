import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';

const PerformanceContext = createContext<any>(undefined);

export interface PerformanceMetrics {
  lcp?: number;
  fid?: number;
  cls?: number;
  fcp?: number;
  ttfb?: number;
}

export function PerformanceProvider({ children }: { children: React.ReactNode }) {
  const [metrics, setMetrics] = useState<Record<string, number>>({});
  useEffect(() => {
    if (typeof window === 'undefined') return;

    // Observe LCP
    const lcpObserver = new PerformanceObserver((entryList) => {
      const entries = entryList.getEntries();
      const lastEntry = entries[entries.length - 1];
      if (lastEntry) {
        reportMetric('LCP', lastEntry.startTime);
      }
    });
    lcpObserver.observe({ type: 'largest-contentful-paint', buffered: true });

    // Observe FID
    const fidObserver = new PerformanceObserver((entryList) => {
      const entries = entryList.getEntries();
      entries.forEach((entry: any) => {
        if (entry.processingStart && entry.startTime) {
          reportMetric('FID', entry.processingStart - entry.startTime);
        }
      });
    });
    fidObserver.observe({ type: 'first-input', buffered: true });

    // Observe CLS
    const clsObserver = new PerformanceObserver((entryList) => {
      let clsValue = 0;
      entryList.getEntries().forEach((entry: any) => {
        if (!entry.hadRecentInput) {
          clsValue += entry.value;
        }
      });
      if (clsValue > 0) {
        reportMetric('CLS', clsValue);
      }
    });
    clsObserver.observe({ type: 'layout-shift', buffered: true });

    // Observe TTFB
    const navigationEntries = performance.getEntriesByType('navigation');
    if (navigationEntries.length > 0) {
      const navEntry = navigationEntries[0] as PerformanceNavigationTiming;
      reportMetric('TTFB', navEntry.responseStart - navEntry.requestStart);
    }

    return () => {
      lcpObserver.disconnect();
      fidObserver.disconnect();
      clsObserver.disconnect();
    };
  }, []);

  const reportMetric = useCallback((name: string, value: number) => {
    setMetrics((prev) => ({ ...prev, [name]: value }));
  }, []);

  const _measureStarts: Record<string, number> = {};

  const startMeasure = useCallback((name: string) => {
    _measureStarts[name] = performance.now();
  }, []);

  const endMeasure = useCallback((name: string) => {
    const start = _measureStarts[name];
    if (start) {
      const duration = performance.now() - start;
      setMetrics((m) => ({ ...m, [name]: duration }));
      delete _measureStarts[name];
    }
  }, []);

  const getMetrics = useCallback(() => {
    return metrics;
  }, [metrics]);

  return (
    <PerformanceContext.Provider value={{ metrics, reportMetric, startMeasure, endMeasure, getMetrics }}>
      {children}
    </PerformanceContext.Provider>
  );
}

export function usePerformance() {
  const context = useContext(PerformanceContext);
  if (!context) {
    throw new Error('usePerformance must be used within a PerformanceProvider');
  }
  return context;
}