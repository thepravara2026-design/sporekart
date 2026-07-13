export interface CsvExportData {
  headers: string[];
  rows: (string | number)[][];
}

export function useCsvExport() {
  const exportCsv = (data: CsvExportData, filename = 'export.csv') => {
    const csv = [data.headers.join(','), ...data.rows.map(r => r.join(','))].join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(link.href);
  };
  return { exportCsv };
}
