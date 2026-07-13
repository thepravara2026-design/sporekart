export interface PdfExportData {
  headers: string[];
  rows: (string | number)[][];
  title?: string;
}

export function usePdfExport() {
  const exportPdf = (data: PdfExportData, filename = 'export.pdf') => {
    const tableRows = data.rows
      .map(r => `<tr>${r.map(c => `<td style="border:1px solid #ccc;padding:4px 8px;text-align:left">${c}</td>`).join('')}</tr>`)
      .join('');
    const tableHeaders = data.headers.map(h => `<th style="border:1px solid #ccc;padding:4px 8px;text-align:left;background:#f5f5f5">${h}</th>`).join('');

    const html = `
      <html>
        <head>
          <style>
            body { font-family: system-ui, -apple-system, sans-serif; padding: 24px; }
            h1 { font-size: 18px; margin-bottom: 16px; }
            table { border-collapse: collapse; width: 100%; }
            th, td { border: 1px solid #ccc; padding: 4px 8px; text-align: left; }
            th { background: #f5f5f5; }
            @media print { body { padding: 0; } }
          </style>
        </head>
        <body>
          ${data.title ? `<h1>${data.title}</h1>` : ''}
          <table>
            <thead><tr>${tableHeaders}</tr></thead>
            <tbody>${tableRows}</tbody>
          </table>
          <script>
            window.print();
            window.close();
          <\/script>
        </body>
      </html>
    `;

    const blob = new Blob([html], { type: 'text/html;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const win = window.open(url, '_blank');
    if (win) {
      win.focus();
    } else {
      const link = document.createElement('a');
      link.href = url;
      link.download = filename.replace('.pdf', '.html');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  };

  return { exportPdf };
}
