import React, { useState, useCallback } from 'react';
import { DropZone } from './DropZone';
import { FilePreview } from './FilePreview';

export interface FileUploadProps {
  value?: File[];
  onChange?: (files: File[]) => void;
  multiple?: boolean;
  accept?: string;
  maxSize?: number;
  maxFiles?: number;
  disabled?: boolean;
  readOnly?: boolean;
  error?: string;
  label?: string;
  helperText?: string;
  required?: boolean;
  className?: string;
}

function isAccepted(file: File, accept?: string): boolean {
  if (!accept) return true;
  const extensions = accept.split(',').map((ext) => ext.trim().toLowerCase());
  const fileExt = '.' + file.name.split('.').pop()?.toLowerCase();
  return extensions.some((ext) => {
    if (ext.includes('/')) {
      const pattern = ext.replace('*', '.*');
      return file.type.match(pattern);
    }
    return fileExt === ext;
  });
}

function validateFiles(
  incoming: File[],
  current: File[],
  accept?: string,
  maxSize?: number,
  maxFiles?: number
): { valid: File[]; error: string | null } {
  const maxSizeBytes = maxSize ? maxSize * 1024 * 1024 : Infinity;
  const maxCount = maxFiles ?? Infinity;
  const combined = [...current, ...incoming];
  const exceededCount = combined.length > maxCount;

  for (const file of incoming) {
    if (!isAccepted(file, accept)) {
      return { valid: [], error: `File type not accepted: ${file.name}` };
    }
    if (file.size > maxSizeBytes) {
      const mb = maxSize ?? 0;
      return { valid: [], error: `File exceeds maximum size of ${mb} MB: ${file.name}` };
    }
  }

  if (exceededCount) {
    return { valid: [], error: `Maximum ${maxFiles} file(s) allowed` };
  }

  return { valid: incoming, error: null };
}

export const FileUpload: React.FC<FileUploadProps> = ({
  value,
  onChange,
  multiple = false,
  accept,
  maxSize,
  maxFiles,
  disabled = false,
  readOnly = false,
  error: externalError,
  label,
  helperText,
  required = false,
  className = '',
}) => {
  const isControlled = value !== undefined;
  const [internalFiles, setInternalFiles] = useState<File[]>([]);
  const [validationError, setValidationError] = useState<string | null>(null);

  const currentFiles = isControlled ? (value || []) : internalFiles;
  const displayError = externalError || validationError;

  const handleFiles = useCallback(
    (incoming: File[]) => {
      if (disabled || readOnly) return;
      const result = validateFiles(incoming, currentFiles, accept, maxSize, maxFiles);
      if (result.error) {
        setValidationError(result.error);
        return;
      }
      setValidationError(null);
      let newFiles: File[];
      if (multiple) {
        newFiles = [...currentFiles, ...result.valid];
      } else {
        newFiles = result.valid.slice(0, 1);
      }
      if (!isControlled) {
        setInternalFiles(newFiles);
      }
      onChange?.(newFiles);
    },
    [currentFiles, disabled, readOnly, accept, maxSize, maxFiles, multiple, isControlled, onChange]
  );

  const handleRemove = useCallback(
    (index: number) => {
      if (disabled || readOnly) return;
      const newFiles = currentFiles.filter((_, i) => i !== index);
      if (!isControlled) {
        setInternalFiles(newFiles);
      }
      setValidationError(null);
      onChange?.(newFiles);
    },
    [currentFiles, disabled, readOnly, isControlled, onChange]
  );

  const wrapperStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
    width: '100%',
  };

  const labelStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-label)',
    fontWeight: 'var(--weight-medium)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-primary)',
    letterSpacing: 'var(--tracking-normal)',
  };

  const requiredIndicator: React.CSSProperties = {
    color: 'var(--color-text-danger)',
    marginLeft: 2,
  };

  const fileListStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
  };

  const helperStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
  };

  return (
    <div
      className={`sk-file-upload ${className}`}
      style={wrapperStyle}
    >
      {(label || required) && (
        <span style={labelStyle}>
          {label}
          {required && <span style={requiredIndicator} aria-hidden="true">*</span>}
        </span>
      )}

      <DropZone
        onFiles={handleFiles}
        accept={accept}
        multiple={multiple}
        disabled={disabled}
        error={displayError || undefined}
      />

      {currentFiles.length > 0 && (
        <div style={fileListStyle} role="list" aria-label="Selected files">
          {currentFiles.map((file, index) => (
            <FilePreview
              key={`${file.name}-${file.size}-${index}`}
              file={file}
              onRemove={!readOnly ? () => handleRemove(index) : undefined}
            />
          ))}
        </div>
      )}

      {helperText && !displayError && (
        <span style={helperStyle}>{helperText}</span>
      )}
    </div>
  );
};

FileUpload.displayName = 'FileUpload';

export default FileUpload;
