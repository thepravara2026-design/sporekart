import { useState, useRef, useEffect, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import { useDataGrid } from '../data-grid/DataGridProvider';

interface ExportButtonProps {
  formats?: { label: string; value: 'csv' | 'excel' | 'pdf' | 'print' }[];
  filename?: string;
}

const DEFAULT_FORMATS = [
  { label: 'CSV', value: 'csv' as const },
  { label: 'Excel', value: 'excel' as const },
  { label: 'PDF', value: 'pdf' as const },
  { label: 'Print', value: 'print' as const },
];

export const ExportButton = memo(function ExportButton({ formats = DEFAULT_FORMATS, filename = 'export' }: ExportButtonProps) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);
  const { rawData, columns, visibleColumns } = useDataGrid();

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const exportCSV = () => {
    const visible = columns.filter((c) => visibleColumns.some((vc) => vc.key === c.key));
    const headers = visible.map((c) => c.header);
    const rows = rawData.map((row) =>
      visible.map((col) => {
        const val = col.render ? stripHtml(String(col.render(row, 0) ?? '')) : String(row[col.key] ?? '');
        return `"${val.replace(/"/g, '""')}"`;
      })
    );
    const csv = [headers.join(','), ...rows.map((r) => r.join(','))].join('\n');
    downloadBlob(new Blob([csv], { type: 'text/csv;charset=utf-8;' }), `${filename}.csv`);
    setOpen(false);
  };

  const exportExcel = () => {
    const visible = columns.filter((c) => visibleColumns.some((vc) => vc.key === c.key));
    const headers = visible.map((c) => c.header);
    const rows = rawData.map((row) =>
      visible.map((col) => {
        const val = col.render ? stripHtml(String(col.render(row, 0) ?? '')) : String(row[col.key] ?? '');
        return val;
      })
    );
    let xml = '<?xml version="1.0"?><?mso-application progid="Excel.Sheet"?>';
    xml += '<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"';
    xml += ' xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">';
    xml += '<Worksheet ss:Name="Sheet1"><Table>';
    xml += '<Row>' + headers.map((h) => `<Cell><Data ss:Type="String">${escXml(h)}</Data></Cell>`).join('') + '</Row>';
    for (const row of rows) {
      xml += '<Row>' + row.map((c) => `<Cell><Data ss:Type="String">${escXml(c)}</Data></Cell>`).join('') + '</Row>';
    }
    xml += '</Table></Worksheet></Workbook>';
    downloadBlob(new Blob([xml], { type: 'application/vnd.ms-excel' }), `${filename}.xls`);
    setOpen(false);
  };

  const exportPrint = () => {
    const visible = columns.filter((c) => visibleColumns.some((vc) => vc.key === c.key));
    const headers = visible.map((c) => c.header);
    const rows = rawData.map((row) =>
      visible.map((col) => {
        const val = col.render ? stripHtml(String(col.render(row, 0) ?? '')) : String(row[col.key] ?? '');
        return val;
      })
    );
    const tableHtml = `
      <html><head><title>${filename}</title>
      <style>table { border-collapse: collapse; width: 100%; } th, td { border: 1px solid #ccc; padding: 8px; text-align: left; } th { background: #f5f5f5; }</style>
      </head><body>
      <table><thead><tr>${headers.map((h) => `<th>${escXml(h)}</th>`).join('')}</tr></thead>
      <tbody>${rows.map((r) => `<tr>${r.map((c) => `<td>${escXml(c)}</td>`).join('')}</tr>`).join('')}</tbody>
      </table></body></html>`;
    const win = window.open('', '_blank');
    if (win) {
      win.document.write(tableHtml);
      win.document.close();
      win.print();
    }
    setOpen(false);
  };

  const handleExport = (format: string) => {
    switch (format) {
      case 'csv': exportCSV(); break;
      case 'excel': exportExcel(); break;
      case 'pdf': console.warn('PDF export placeholder'); setOpen(false); break;
      case 'print': exportPrint(); break;
    }
  };

  return (
    <div ref={ref} style={{ position: 'relative' }}>
      <Button variant="outline" size="sm" onClick={() => setOpen(!open)} aria-haspopup="menu" aria-expanded={open}>
        <Icon name="download" size={14} /> Export
      </Button>
      {open && (
        <div
          role="menu"
          style={{
            position: 'absolute',
            top: '100%',
            right: 0,
            marginTop: 4,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            boxShadow: 'var(--elevation-md)',
            zIndex: 100,
            minWidth: 140,
            padding: 4,
          }}
        >
          {formats.map((fmt) => (
            <button
              key={fmt.value}
              role="menuitem"
              onClick={() => handleExport(fmt.value)}
              style={{
                display: 'block',
                width: '100%',
                padding: '8px 12px',
                border: 'none',
                background: 'transparent',
                cursor: 'pointer',
                textAlign: 'left',
                color: 'var(--color-text-primary)',
                fontSize: 'var(--text-body)',
                borderRadius: 'var(--radius-sm)',
              }}
            >
              {fmt.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
});

function downloadBlob(blob: Blob, filename: string) {
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

function stripHtml(str: string): string {
  return str.replace(/<[^>]*>/g, '');
}

function escXml(str: string): string {
  return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
