export function usePrintExport() {
  const exportPrint = (element?: HTMLElement | null) => {
    if (!element) {
      window.print();
      return;
    }

    const originalTitle = document.title;
    const iframe = document.createElement('iframe');
    iframe.style.position = 'absolute';
    iframe.style.width = '0';
    iframe.style.height = '0';
    iframe.style.border = 'none';
    iframe.style.opacity = '0';
    iframe.style.pointerEvents = 'none';

    document.body.appendChild(iframe);

    const doc = iframe.contentWindow?.document;
    if (!doc) {
      window.print();
      document.body.removeChild(iframe);
      return;
    }

    doc.open();
    doc.write('<!DOCTYPE html><html><head>');
    const styles = document.querySelectorAll('style, link[rel="stylesheet"]');
    styles.forEach((s) => {
      doc.write(s.outerHTML);
    });
    doc.write(`</head><body>${element.outerHTML}</body></html>`);
    doc.close();

    iframe.contentWindow?.focus();
    iframe.contentWindow?.print();

    setTimeout(() => {
      document.body.removeChild(iframe);
      document.title = originalTitle;
    }, 100);
  };

  return { exportPrint };
}
