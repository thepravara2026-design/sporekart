export interface ExcelExportData {
  headers: string[];
  rows: (string | number)[][];
}

export function useExcelExport() {
  const exportExcel = (data: ExcelExportData, filename = 'export.xls') => {
    const tsv = [data.headers.join('\t'), ...data.rows.map(r => r.join('\t'))].join('\n');
    const blob = new Blob([tsv], { type: 'application/vnd.ms-excel;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(link.href);
  };
  return { exportExcel };
}
