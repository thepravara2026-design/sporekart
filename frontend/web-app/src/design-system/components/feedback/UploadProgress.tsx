import React from 'react';
import { LinearProgress } from './LinearProgress';

export interface UploadProgressProps {
  fileName: string;
  fileSize?: string;
  progress: number;
  status?: 'uploading' | 'processing' | 'completed' | 'error';
  onCancel?: () => void;
  onRetry?: () => void;
  className?: string;
  style?: React.CSSProperties;
}

function UploadCloudIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
      <path d="M21.5 14.5A4.5 4.5 0 0 0 17 10h-1.1a7 7 0 0 0-13.16-1.3A4.5 4.5 0 0 0 5 17h12.5a4.5 4.5 0 0 0 4-2.5z" />
      <line x1="12" y1="12" x2="12" y2="17" />
      <polyline points="9 14 12 17 15 14" />
    </svg>
  );
}

function CheckCircleIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
      <polyline points="22 4 12 14.01 9 11.01" />
    </svg>
  );
}

function ErrorIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
      <circle cx="12" cy="12" r="10" />
      <line x1="15" y1="9" x2="9" y2="15" />
      <line x1="9" y1="9" x2="15" y2="15" />
    </svg>
  );
}

function SpinnerIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" aria-hidden="true" style={{ animation: 'sk-spin 0.8s linear infinite' }}>
      <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" />
    </svg>
  );
}

const spinKeyframes = `
  @keyframes sk-spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
`;

export const UploadProgress: React.FC<UploadProgressProps> = ({
  fileName,
  fileSize,
  progress,
  status = 'uploading',
  onCancel,
  onRetry,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
    padding: 'var(--space-inline-md)',
    borderRadius: 'var(--radius-md)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-surface-default)',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
  };

  const metaStyle: React.CSSProperties = {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    gap: 2,
    minWidth: 0,
  };

  const nameStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-primary)',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  };

  const sizeStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
  };

  const actionsStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
  };

  const buttonBase: React.CSSProperties = {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: 'var(--space-1)',
    borderRadius: 'var(--radius-xs)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    transition: `opacity var(--duration-fast) var(--easing-standard)`,
  };

  const cancelBtnStyle: React.CSSProperties = {
    ...buttonBase,
    color: 'var(--color-text-secondary)',
  };

  const retryBtnStyle: React.CSSProperties = {
    ...buttonBase,
    color: 'var(--color-bg-primary-default)',
  };

  const statusTextStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    color:
      status === 'completed'
        ? 'var(--color-text-success)'
        : status === 'error'
          ? 'var(--color-text-danger)'
          : 'var(--color-text-secondary)',
  };

  const completed = status === 'completed';
  const isError = status === 'error';

  return (
    <div className={`sk-upload-progress ${className}`.trim()} style={containerStyle}>
      <style>{spinKeyframes}</style>
      <div style={headerStyle}>
        <span style={{ color: isError ? 'var(--color-icon-danger)' : completed ? 'var(--color-icon-success)' : 'var(--color-icon-primary)', flexShrink: 0 }}>
          {completed ? <CheckCircleIcon /> : isError ? <ErrorIcon /> : status === 'processing' ? <SpinnerIcon /> : <UploadCloudIcon />}
        </span>
        <div style={metaStyle}>
          <span style={nameStyle} title={fileName}>{fileName}</span>
          {fileSize && <span style={sizeStyle}>{fileSize}</span>}
        </div>
        <div style={actionsStyle}>
          {isError && onRetry && (
            <button type="button" style={retryBtnStyle} onClick={onRetry} aria-label="Retry upload">
              Retry
            </button>
          )}
          {!completed && !isError && onCancel && (
            <button type="button" style={cancelBtnStyle} onClick={onCancel} aria-label="Cancel upload">
              Cancel
            </button>
          )}
        </div>
      </div>
      {!completed && (
        <div>
          <LinearProgress
            value={isError ? 0 : progress}
            size="sm"
            color={isError ? 'danger' : 'primary'}
          />
          <span style={{ ...statusTextStyle, marginTop: 'var(--space-stack-xs)', display: 'inline-block' }}>
            {isError ? 'Upload failed' : status === 'processing' ? 'Processing...' : `${Math.round(progress)}%`}
          </span>
        </div>
      )}
      {completed && (
        <span style={statusTextStyle}>Upload complete</span>
      )}
    </div>
  );
};

UploadProgress.displayName = 'UploadProgress';
export default UploadProgress;
