import { useCallback, useRef } from 'react';

export function useChartExport() {
  const svgRef = useRef<SVGSVGElement | null>(null);

  const exportPNG = useCallback((filename = 'chart.png') => {
    const svg = svgRef.current;
    if (!svg) return;
    const svgData = new XMLSerializer().serializeToString(svg);
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    const img = new Image();
    img.onload = () => {
      canvas.width = img.width;
      canvas.height = img.height;
      ctx?.drawImage(img, 0, 0);
      const link = document.createElement('a');
      link.download = filename;
      link.href = canvas.toDataURL('image/png');
      link.click();
    };
    img.src = 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svgData)));
  }, []);

  const exportCSV = useCallback((data: { headers: string[]; rows: (string | number)[][] }, filename = 'chart-data.csv') => {
    const csv = [data.headers.join(','), ...data.rows.map(r => r.join(','))].join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const link = document.createElement('a');
    link.download = filename;
    link.href = URL.createObjectURL(blob);
    link.click();
    URL.revokeObjectURL(link.href);
  }, []);

  return { svgRef, exportPNG, exportCSV };
}
