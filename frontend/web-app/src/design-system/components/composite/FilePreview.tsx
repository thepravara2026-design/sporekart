import React from 'react';

export interface FilePreviewProps {
  file: File;
  progress?: number;
  uploading?: boolean;
  error?: string;
  onRemove?: () => void;
  className?: string;
}

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function truncateName(name: string, maxLength = 30): string {
  if (name.length <= maxLength) return name;
  const ext = name.split('.').pop() || '';
  const base = name.slice(0, maxLength - ext.length - 4);
  return `${base}...${ext}`;
}

function isImageType(file: File): boolean {
  return file.type.startsWith('image/');
}

const FileIcon: React.FC = () => (
  <svg
    width="24"
    height="24"
    viewBox="0 0 24 24"
    fill="none"
    aria-hidden="true"
  >
    <path
      d="M13 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V9l-7-7z"
      stroke="var(--color-icon-default)"
      strokeWidth="1.5"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M13 2v7h7"
      stroke="var(--color-icon-default)"
      strokeWidth="1.5"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

const CloseIcon: React.FC = () => (
  <svg
    width="16"
    height="16"
    viewBox="0 0 16 16"
    fill="none"
    aria-hidden="true"
  >
    <path
      d="M12 4L4 12M4 4l8 8"
      stroke="currentColor"
      strokeWidth="1.5"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const FilePreview: React.FC<FilePreviewProps> = ({
  file,
  progress = 0,
  uploading = false,
  error,
  onRemove,
  className = '',
}) => {
  const thumbnailUrl = isImageType(file) ? URL.createObjectURL(file) : null;

  const cardStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-2) var(--space-3)',
    background: 'var(--color-bg-surface-default)',
    border: error ? 'var(--border-width-thin) solid var(--color-border-error)' : 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-sm)',
    boxShadow: 'var(--shadow-1)',
    position: 'relative',
    overflow: 'hidden',
  };

  const infoStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
    display: 'flex',
    flexDirection: 'column',
    gap: 2,
  };

  const nameStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-primary)',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    margin: 0,
  };

  const sizeStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    margin: 0,
  };

  const progressBarContainerStyle: React.CSSProperties = {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    height: 3,
    background: 'var(--color-border-default)',
    borderRadius: '0 0 var(--radius-sm) var(--radius-sm)',
    overflow: 'hidden',
  };

  const progressBarStyle: React.CSSProperties = {
    height: '100%',
    width: `${Math.min(100, Math.max(0, progress))}%`,
    background: 'var(--color-bg-primary-default)',
    transition: 'width var(--duration-normal) var(--easing-standard)',
    borderRadius: '0 0 0 var(--radius-sm)',
  };

  const errorStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-danger)',
    margin: 0,
  };

  const removeBtnStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    border: 'none',
    background: 'none',
    cursor: 'pointer',
    padding: 'var(--space-1)',
    color: 'var(--color-text-secondary)',
    borderRadius: 'var(--radius-xs)',
    flexShrink: 0,
    transition: 'color var(--duration-fast) var(--easing-standard)',
  };

  const iconContainerStyle: React.CSSProperties = {
    width: 36,
    height: 36,
    borderRadius: 'var(--radius-xs)',
    overflow: 'hidden',
    flexShrink: 0,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    background: 'var(--color-bg-surface-default)',
  };

  const imgStyle: React.CSSProperties = {
    width: 36,
    height: 36,
    objectFit: 'cover',
    borderRadius: 'var(--radius-xs)',
  };

  return (
    <div
      className={`sk-file-preview ${error ? 'sk-file-preview--error' : ''} ${uploading ? 'sk-file-preview--uploading' : ''} ${className}`}
      style={cardStyle}
      role="listitem"
    >
      <div style={iconContainerStyle}>
        {thumbnailUrl ? (
          <img src={thumbnailUrl} alt={file.name} style={imgStyle} />
        ) : (
          <FileIcon />
        )}
      </div>

      <div style={infoStyle}>
        <p style={nameStyle} title={file.name}>{truncateName(file.name)}</p>
        <p style={sizeStyle}>{formatFileSize(file.size)}</p>
        {error && <p style={errorStyle} role="alert">{error}</p>}
      </div>

      {onRemove && (
        <button
          type="button"
          style={removeBtnStyle}
          onClick={onRemove}
          aria-label={`Remove ${file.name}`}
          className="sk-file-preview__remove"
        >
          <CloseIcon />
        </button>
      )}

      {uploading && (
        <div style={progressBarContainerStyle}>
          <div style={progressBarStyle} />
        </div>
      )}
      <style>{`
        .sk-file-preview__remove:hover {
          color: var(--color-text-danger);
        }
      `}</style>
    </div>
  );
};

FilePreview.displayName = 'FilePreview';

export default FilePreview;
