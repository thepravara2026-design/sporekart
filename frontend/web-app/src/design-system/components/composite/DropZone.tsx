import React, { useState, useRef, useCallback } from 'react';

export interface DropZoneProps {
  onFiles: (files: File[]) => void;
  accept?: string;
  multiple?: boolean;
  disabled?: boolean;
  error?: string;
  className?: string;
}

const UploadIcon: React.FC = () => (
  <svg
    width="40"
    height="40"
    viewBox="0 0 40 40"
    fill="none"
    aria-hidden="true"
  >
    <rect width="40" height="40" rx="8" fill="var(--color-bg-primary-weak)" />
    <path
      d="M20 26V14M20 14L14 20M20 14L26 20"
      stroke="var(--color-bg-primary-default)"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const DropZone: React.FC<DropZoneProps> = ({
  onFiles,
  accept,
  multiple = false,
  disabled = false,
  error,
  className = '',
}) => {
  const [isDragOver, setIsDragOver] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleDragOver = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (!disabled) setIsDragOver(true);
  }, [disabled]);

  const handleDragLeave = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragOver(false);
  }, []);

  const processFiles = useCallback((fileList: FileList) => {
    const files = Array.from(fileList);
    if (accept) {
      const allowedExtensions = accept.split(',').map((ext) => ext.trim().toLowerCase());
      const filtered = files.filter((file) => {
        const fileExt = '.' + file.name.split('.').pop()?.toLowerCase();
        const mimeMatch = allowedExtensions.some((ext) => {
          if (ext.includes('/')) {
            return file.type.match(ext.replace('*', '.*'));
          }
          return fileExt === ext;
        });
        return mimeMatch;
      });
      onFiles(filtered);
    } else {
      onFiles(files);
    }
  }, [accept, onFiles]);

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragOver(false);
    if (disabled) return;
    if (e.dataTransfer.files.length > 0) {
      processFiles(e.dataTransfer.files);
    }
  }, [disabled, processFiles]);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      processFiles(e.target.files);
    }
    e.target.value = '';
  }, [processFiles]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    if (disabled) return;
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      inputRef.current?.click();
    }
  }, [disabled]);

  const handleClick = useCallback(() => {
    if (!disabled) {
      inputRef.current?.click();
    }
  }, [disabled]);

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 'var(--space-stack-sm)',
    padding: 'var(--space-8) var(--space-4)',
    border: `${isDragOver ? 'var(--border-width-thick)' : 'var(--border-width-thin)'} ${isDragOver ? 'var(--border-style-solid)' : 'var(--border-style-dashed)'} ${isDragOver ? 'var(--color-border-focus)' : error ? 'var(--color-border-error)' : 'var(--color-border-default)'}`,
    borderRadius: 'var(--radius-card)',
    background: isDragOver ? 'var(--color-bg-primary-weak)' : 'var(--color-bg-surface-default)',
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    transition: 'border-color var(--duration-fast) var(--easing-standard), background var(--duration-fast) var(--easing-standard)',
    outline: 'none',
  };

  const textStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-primary)',
    textAlign: 'center',
    margin: 0,
  };

  const subtextStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    textAlign: 'center',
    margin: 0,
  };

  const errorStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-danger)',
    textAlign: 'center',
    margin: 0,
  };

  const hiddenInputStyle: React.CSSProperties = {
    position: 'absolute',
    width: 0,
    height: 0,
    opacity: 0,
    pointerEvents: 'none',
  };

  return (
    <div
      className={`sk-dropzone ${isDragOver ? 'sk-dropzone--drag-over' : ''} ${disabled ? 'sk-dropzone--disabled' : ''} ${error ? 'sk-dropzone--error' : ''} ${className}`}
      style={containerStyle}
      onDragOver={handleDragOver}
      onDragEnter={handleDragOver}
      onDragLeave={handleDragLeave}
      onDrop={handleDrop}
      onClick={handleClick}
      onKeyDown={handleKeyDown}
      tabIndex={disabled ? -1 : 0}
      role="button"
      aria-label="Upload files. Drag and drop or click to browse."
      aria-disabled={disabled}
    >
      <UploadIcon />
      <p style={textStyle}>
        {isDragOver ? 'Drop files here' : 'Drag & drop files here or click to browse'}
      </p>
      <p style={subtextStyle}>
        {accept ? `Accepted: ${accept}` : 'All file types accepted'}
        {multiple ? ' (multiple)' : ''}
      </p>
      {error && <p style={errorStyle} role="alert">{error}</p>}
      <input
        ref={inputRef}
        type="file"
        accept={accept}
        multiple={multiple}
        onChange={handleInputChange}
        style={hiddenInputStyle}
        tabIndex={-1}
        aria-hidden="true"
      />
    </div>
  );
};

DropZone.displayName = 'DropZone';

export default DropZone;
