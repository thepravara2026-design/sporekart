import { useState, useRef, useCallback } from 'react';

type UploadState = 'empty' | 'selected' | 'uploading' | 'complete' | 'error';

interface FileItem {
  name: string;
  size: number;
  state: UploadState;
  progress: number;
  error?: string;
}

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap)',
  padding: 'var(--space-page-y) var(--space-page-x)',
};

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  padding: '24px',
  boxShadow: 'var(--shadow-1)',
  display: 'flex',
  flexDirection: 'column',
  gap: '16px',
};

const dropZoneStyle: React.CSSProperties = {
  border: '2px dashed var(--color-border-default)',
  borderRadius: 'var(--radius-md)',
  padding: '40px 24px',
  textAlign: 'center',
  cursor: 'pointer',
  transition: 'border-color 0.2s, background 0.2s',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  gap: '8px',
};

const fileRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '12px',
  padding: '8px 12px',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-surface-raised)',
};

const progressTrackStyle: React.CSSProperties = {
  flex: 1,
  height: '6px',
  borderRadius: '3px',
  background: 'var(--color-bg-disabled)',
  overflow: 'hidden',
};

const btnStyle: React.CSSProperties = {
  background: 'var(--color-bg-primary-default)',
  color: '#fff',
  border: 'none',
  borderRadius: 'var(--radius-md)',
  padding: '8px 16px',
  fontSize: 'var(--text-base)',
  fontWeight: 500,
  cursor: 'pointer',
};

function formatSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function UploadArea({ accept, multiple, maxSize, label }: { accept?: string; multiple?: boolean; maxSize?: number; label: string }) {
  const [files, setFiles] = useState<FileItem[]>([]);
  const [dragOver, setDragOver] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  const addFiles = useCallback((incoming: File[]) => {
    const items: FileItem[] = incoming.map((f) => {
      let state: UploadState = 'selected';
      let error: string | undefined;
      if (maxSize && f.size > maxSize) { state = 'error'; error = `File exceeds ${formatSize(maxSize)} limit`; }
      return { name: f.name, size: f.size, state, progress: 0, error };
    });
    setFiles((prev) => [...prev, ...items]);
  }, [maxSize]);

  const simulateProgress = (index: number) => {
    setFiles((prev) => prev.map((f, i) => i === index ? { ...f, state: 'uploading' as UploadState, progress: 0 } : f));
    let pct = 0;
    const interval = setInterval(() => {
      pct += Math.random() * 15 + 5;
      if (pct >= 100) {
        pct = 100;
        clearInterval(interval);
        setFiles((prev) => prev.map((f, i) => i === index ? { ...f, state: 'complete' as UploadState, progress: 100 } : f));
      } else {
        setFiles((prev) => prev.map((f, i) => i === index ? { ...f, progress: Math.min(pct, 99) } : f));
      }
    }, 300);
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setDragOver(false);
    if (e.dataTransfer.files.length) addFiles(Array.from(e.dataTransfer.files));
  };

  const stateColor = (state: UploadState): string => {
    switch (state) {
      case 'complete': return 'var(--color-text-success)';
      case 'error': return 'var(--color-text-danger)';
      case 'uploading': return 'var(--color-text-link)';
      default: return 'var(--color-text-secondary)';
    }
  };

  const stateIcon = (state: UploadState) => {
    switch (state) {
      case 'complete': return '\u2713';
      case 'error': return '\u2717';
      case 'uploading': return '';
      default: return '\uD83D\uDCC4';
    }
  };

  return (
    <div style={cardStyle}>
      <h3 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h3>
      <div
        style={{ ...dropZoneStyle, borderColor: dragOver ? 'var(--color-focus-ring)' : 'var(--color-border-default)', background: dragOver ? 'var(--color-bg-primary-subtle)' : 'transparent' }}
        onClick={() => inputRef.current?.click()}
        onDragOver={(e) => { e.preventDefault(); setDragOver(true); }}
        onDragLeave={() => setDragOver(false)}
        onDrop={handleDrop}
        tabIndex={0}
        onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') inputRef.current?.click(); }}
      >
        <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
          <path d="M16 4v16M8 12l8-8 8 8M4 24v4h24v-4" stroke="var(--color-text-secondary)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
        <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>Drop files here or click to browse</span>
        {accept && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Accepted: {accept}</span>}
        {maxSize && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Max size: {formatSize(maxSize)}</span>}
      </div>
      <input ref={inputRef} type="file" accept={accept} multiple={multiple} style={{ display: 'none' }} onChange={(e) => { if (e.target.files?.length) addFiles(Array.from(e.target.files)); }} />

      {files.length > 0 && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
          {files.map((file, i) => (
            <div key={i} style={fileRowStyle}>
              <span style={{ fontSize: 'var(--text-body)', color: stateColor(file.state) }}>{stateIcon(file.state)}</span>
              <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '4px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{file.name}</span>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{formatSize(file.size)}</span>
                </div>
                {file.state === 'uploading' && (
                  <div style={progressTrackStyle}>
                    <div style={{ width: `${file.progress}%`, height: '100%', borderRadius: '3px', background: 'var(--color-bg-primary-default)', transition: 'width 0.2s' }} />
                  </div>
                )}
                {file.state === 'error' && file.error && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger)' }}>{file.error}</span>}
                {file.state === 'complete' && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-success)' }}>Upload complete</span>}
              </div>
              {file.state === 'selected' && (
                <button style={{ ...btnStyle, padding: '4px 8px', fontSize: 'var(--text-caption)' }} onClick={() => simulateProgress(i)}>Upload</button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default function UploadPreview() {
  return (
    <div style={sectionStyle}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>File Upload</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>File upload with all states</p>
      </div>

      <UploadArea label="Single File Upload" />
      <UploadArea label="Multiple File Upload" multiple />
      <UploadArea label="Drag and Drop Zone" multiple />
      <UploadArea label="Image Files Only (accept images)" accept="image/*" />
      <UploadArea label="Max 5MB Limit" maxSize={5 * 1024 * 1024} />
    </div>
  );
}
