import React from 'react';
import { createPortal } from 'react-dom';

export interface VideoModalProps {
  open: boolean;
  onClose: () => void;
  src: string;
  title?: string;
  poster?: string;
  className?: string;
  style?: React.CSSProperties;
}

export const VideoModal: React.FC<VideoModalProps> = ({
  open,
  onClose,
  src,
  title,
  poster,
  className = '',
  style,
}) => {
  const videoRef = React.useRef<HTMLVideoElement>(null);

  React.useEffect(() => {
    if (!open) {
      videoRef.current?.pause();
    }
  }, [open]);

  React.useEffect(() => {
    if (!open) return;
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [open, onClose]);

  if (!open) return null;

  const modal = (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 'var(--z-modal-backdrop)',
        backgroundColor: 'var(--backdrop-heavy)',
        animation: 'sk-modal-overlay-enter var(--duration-normal) var(--easing-standard)',
        ...style,
      } as React.CSSProperties}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
      role="dialog"
      aria-modal="true"
      aria-label={title || 'Video player'}
    >
      <style>{`
        @keyframes sk-modal-overlay-enter {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        @keyframes sk-video-enter {
          from { opacity: 0; transform: scale(0.95); }
          to { opacity: 1; transform: scale(1); }
        }
      `}</style>
      <div
        className={className}
        style={{
          position: 'relative',
          width: '80vw',
          maxWidth: '960px',
          animation: 'sk-video-enter var(--duration-normal) var(--easing-standard)',
        } as React.CSSProperties}
        onClick={(e) => e.stopPropagation()}
      >
        {title && (
          <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: 'var(--space-4) 0',
          } as React.CSSProperties}>
            <h2 style={{
              margin: 0,
              fontSize: 'var(--text-h3)',
              fontWeight: 'var(--weight-semibold)',
              color: '#FFFFFF',
            } as React.CSSProperties}>
              {title}
            </h2>
            <button
              onClick={onClose}
              aria-label="Close video"
              style={{
                background: 'none',
                border: 'none',
                cursor: 'pointer',
                padding: 'var(--space-1)',
                color: '#FFFFFF',
                fontSize: 'var(--text-h4)',
                lineHeight: 1,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              } as React.CSSProperties}
            >
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M18 6L6 18M6 6l12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
              </svg>
            </button>
          </div>
        )}
        <video
          ref={videoRef}
          src={src}
          poster={poster}
          controls
          autoPlay
          style={{
            width: '100%',
            borderRadius: 'var(--radius-image)',
            boxShadow: 'var(--shadow-4)',
            display: 'block',
          } as React.CSSProperties}
        >
          Your browser does not support the video tag.
        </video>
      </div>
    </div>
  );

  return createPortal(modal, document.body);
};

VideoModal.displayName = 'VideoModal';
export default VideoModal;
