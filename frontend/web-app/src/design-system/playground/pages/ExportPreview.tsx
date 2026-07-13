import React from 'react';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

import { ExportMenu } from '../../components/charts/export/ExportMenu';
import { useCsvExport } from '../../components/charts/export/useCsvExport';
import { useExcelExport } from '../../components/charts/export/useExcelExport';
import { usePrintExport } from '../../components/charts/export/usePrintExport';

const sampleHeaders = ['Product', 'Revenue', 'Units Sold', 'Region', 'Date'];
const sampleRows: (string | number)[][] = [
  ['Widget A', 12500, 340, 'North', '2026-06-01'],
  ['Widget B', 8900, 215, 'South', '2026-06-02'],
  ['Gadget X', 22300, 510, 'East', '2026-06-03'],
  ['Gadget Y', 15700, 380, 'West', '2026-06-04'],
  ['Tool Pro', 31200, 720, 'North', '2026-06-05'],
  ['Tool Lite', 9800, 290, 'South', '2026-06-06'],
  ['Service A', 4500, 95, 'East', '2026-06-07'],
  ['Service B', 18200, 410, 'West', '2026-06-08'],
];

const tableStyle: React.CSSProperties = {
  width: '100%',
  borderCollapse: 'collapse',
  fontSize: 'var(--text-body-sm)',
  fontFamily: 'var(--font-family-sans)',
};

const thStyle: React.CSSProperties = {
  textAlign: 'left',
  padding: '8px 12px',
  borderBottom: '2px solid var(--color-border-default)',
  color: 'var(--color-text-secondary)',
  fontWeight: 'var(--weight-semibold)',
  fontSize: 'var(--text-caption)',
  whiteSpace: 'nowrap',
};

const tdStyle: React.CSSProperties = {
  padding: '8px 12px',
  borderBottom: '1px solid var(--color-border-default)',
  color: 'var(--color-text-primary)',
};

export default function ExportPreview() {
  const { exportCsv } = useCsvExport();
  const { exportExcel } = useExcelExport();
  const { exportPrint } = usePrintExport();
  const tableRef = React.useRef<HTMLDivElement>(null);

  const handleExport = (format: string) => {
    const data = { headers: sampleHeaders, rows: sampleRows };
    switch (format) {
      case 'csv':
        exportCsv(data, 'sales-report.csv');
        break;
      case 'excel':
        exportExcel(data, 'sales-report.xls');
        break;
      case 'print':
        exportPrint(tableRef.current);
        break;
      case 'pdf':
        alert('PDF export triggered');
        break;
      case 'png':
        alert('PNG export triggered');
        break;
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Export</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Export menu, CSV/Excel/Print hooks, and data preview</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>ExportMenu</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Export dropdown with all formats">
            <ExportMenu onExport={(fmt) => handleExport(fmt)} />
          </StateCard>
          <StateCard label="Disabled state">
            <ExportMenu onExport={(fmt) => handleExport(fmt)} disabled />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Export Hooks</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="useCsvExport">
            <button
              style={{
                display: 'inline-flex', alignItems: 'center', gap: '6px', height: 'var(--btn-height-md)',
                padding: '0 var(--space-3)', border: 'var(--border-width-thin) solid var(--color-border-default)',
                borderRadius: 'var(--radius-btn)', background: 'var(--color-bg-surface-default)',
                color: 'var(--color-text-primary)', cursor: 'pointer', fontFamily: 'var(--font-family-sans)',
                fontSize: 'var(--text-button)', fontWeight: 'var(--weight-medium)',
              }}
              onClick={() => exportCsv({ headers: sampleHeaders, rows: sampleRows }, 'data.csv')}
            >
              Download CSV
            </button>
          </StateCard>
          <StateCard label="useExcelExport">
            <button
              style={{
                display: 'inline-flex', alignItems: 'center', gap: '6px', height: 'var(--btn-height-md)',
                padding: '0 var(--space-3)', border: 'var(--border-width-thin) solid var(--color-border-default)',
                borderRadius: 'var(--radius-btn)', background: 'var(--color-bg-surface-default)',
                color: 'var(--color-text-primary)', cursor: 'pointer', fontFamily: 'var(--font-family-sans)',
                fontSize: 'var(--text-button)', fontWeight: 'var(--weight-medium)',
              }}
              onClick={() => exportExcel({ headers: sampleHeaders, rows: sampleRows }, 'data.xls')}
            >
              Download Excel
            </button>
          </StateCard>
          <StateCard label="usePrintExport">
            <button
              style={{
                display: 'inline-flex', alignItems: 'center', gap: '6px', height: 'var(--btn-height-md)',
                padding: '0 var(--space-3)', border: 'var(--border-width-thin) solid var(--color-border-default)',
                borderRadius: 'var(--radius-btn)', background: 'var(--color-bg-surface-default)',
                color: 'var(--color-text-primary)', cursor: 'pointer', fontFamily: 'var(--font-family-sans)',
                fontSize: 'var(--text-button)', fontWeight: 'var(--weight-medium)',
              }}
              onClick={() => exportPrint(tableRef.current)}
            >
              Print Table
            </button>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Sample Data Preview</h2>
        <StateCard label="Table that will be printed">
          <div ref={tableRef} style={{ width: '100%', overflowX: 'auto' }}>
            <table style={tableStyle}>
              <thead>
                <tr>
                  {sampleHeaders.map((h, i) => (
                    <th key={i} style={thStyle}>{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {sampleRows.map((row, ri) => (
                  <tr key={ri}>
                    {row.map((cell, ci) => (
                      <td key={ci} style={tdStyle}>{cell}</td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </StateCard>
      </section>
    </div>
  );
}
