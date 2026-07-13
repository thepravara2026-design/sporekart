import { useCallback } from 'react';

export function useChartPrint() {
  const printChart = useCallback((element: HTMLElement | null) => {
    if (!element) return;
    const original = document.body.innerHTML;
    const printContents = element.outerHTML;
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = original;
    window.location.reload();
  }, []);

  return { printChart };
}
