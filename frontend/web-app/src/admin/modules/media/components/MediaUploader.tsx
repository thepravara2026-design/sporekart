import React, { useState, useCallback } from 'react';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import { DropZone } from '../../../../design-system/components/composite/DropZone';
import { FilePreview } from '../../../../design-system/components/composite/FilePreview';

interface MediaUploaderProps {
  open: boolean;
  onClose: () => void;
}

export const MediaUploader = React.memo(function MediaUploader({ open, onClose }: MediaUploaderProps) {
  const [files, setFiles] = useState<File[]>([]);
  const [uploading, setUploading] = useState(false);
  const [complete, setComplete] = useState(false);

  const handleFiles = useCallback((incoming: File[]) => {
    setFiles((prev) => [...prev, ...incoming]);
  }, []);

  const handleRemove = useCallback((index: number) => {
    setFiles((prev) => prev.filter((_, i) => i !== index));
  }, []);

  const handleUpload = useCallback(() => {
    if (files.length === 0) return;
    setUploading(true);
    setTimeout(() => {
      setUploading(false);
      setComplete(true);
    }, 1500);
  }, [files]);

  const handleDone = useCallback(() => {
    setFiles([]);
    setComplete(false);
    onClose();
  }, [onClose]);

  if (!open) return null;

  const overlayStyle: React.CSSProperties = {
    position: 'fixed', inset: 0, zIndex: 1000,
    background: 'rgba(0,0,0,0.6)', display: 'flex', alignItems: 'center',
    justifyContent: 'center', padding: 'var(--space-4)',
  };

  const modalStyle: React.CSSProperties = {
    display: 'flex', flexDirection: 'column',
    background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-modal)',
    maxWidth: 640, width: '100%', maxHeight: '80vh',
    boxShadow: 'var(--shadow-3)', overflow: 'hidden',
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex', alignItems: 'center', justifyContent: 'space-between',
    padding: 'var(--space-3) var(--space-4)',
    borderBottom: '1px solid var(--color-border-default)',
  };

  return (
    <div style={overlayStyle} onClick={onClose} role="dialog" aria-modal="true" aria-label="Upload media assets">
      <div style={modalStyle} onClick={(e) => e.stopPropagation()}>
        <div style={headerStyle}>
          <span style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
            Upload Assets
          </span>
          <button
            type="button"
            onClick={onClose}
            style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', width: 36, height: 36, border: 'none', background: 'none', cursor: 'pointer', borderRadius: 'var(--radius-xs)', color: 'var(--color-text-secondary)' }}
            aria-label="Close upload dialog"
          >
            <Icon name="x" size={20} />
          </button>
        </div>

        <div style={{ padding: 'var(--space-4)', display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)', overflowY: 'auto', flex: 1 }}>
          {complete ? (
            <div style={{ textAlign: 'center', padding: 'var(--space-8) 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 'var(--space-stack-sm)' }}>
              <Icon name="check-circle" size={48} style={{ color: 'var(--color-success)' }} />
              <span style={{ fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Upload Complete</span>
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                {files.length} file{files.length === 1 ? '' : 's'} uploaded successfully.
              </span>
              <Button variant="primary" onClick={handleDone}>Done</Button>
            </div>
          ) : (
            <>
              <DropZone
                onFiles={handleFiles}
                accept="image/*,video/*,audio/*,.pdf,.doc,.docx,.obj,.glb,.gltf"
                multiple
              />

              {files.length > 0 && (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)' }}>
                    {files.length} file{files.length === 1 ? '' : 's'} selected
                  </span>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
                    {files.map((file, index) => (
                      <FilePreview
                        key={`${file.name}-${index}`}
                        file={file}
                        onRemove={() => handleRemove(index)}
                      />
                    ))}
                  </div>
                </div>
              )}

              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 'var(--space-inline-xs)' }}>
                <Button variant="ghost" onClick={onClose}>Cancel</Button>
                <Button
                  variant="primary"
                  onClick={handleUpload}
                  disabled={files.length === 0 || uploading}
                  leftIcon={<Icon name="upload" size={14} />}
                >
                  {uploading ? 'Uploading...' : `Upload ${files.length > 0 ? `(${files.length})` : ''}`}
                </Button>
              </div>
            </>
          )}
        </div>

        <div style={{ padding: '8px var(--space-4)', borderTop: '1px solid var(--color-border-default)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Mock Mode — files are not persisted. Supported formats: JPG, PNG, MP4, PDF, OBJ, GLB, MP3.
        </div>
      </div>
    </div>
  );
});

export default MediaUploader;
