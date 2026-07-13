import React, { createContext, useContext, useState, useCallback } from 'react';

const DialogContext = createContext<any>(undefined);

export interface DialogConfig {
  title: string;
  content: React.ReactNode;
  primaryAction?: {
    label: string;
    onClick: () => void | Promise<void>;
    variant?: 'primary' | 'destructive' | 'secondary';
  };
  secondaryAction?: {
    label: string;
    onClick: () => void;
  };
  size?: 'sm' | 'md' | 'lg' | 'xl' | 'full';
  closeOnOverlayClick?: boolean;
  closeOnEscape?: boolean;
  showCloseButton?: boolean;
}

export function DialogProvider({ children }: { children: React.ReactNode }) {
  const [isOpen, setIsOpen] = useState(false);
  const [currentDialog, setCurrentDialog] = useState<DialogConfig | null>(null);
  const [resolvePromise, setResolvePromise] = useState<(() => void) | null>(null);

  const openDialog = useCallback((config: DialogConfig) => {
    return new Promise<void>((resolve) => {
      setCurrentDialog(config);
      setIsOpen(true);
      setResolvePromise(() => resolve);
    });
  }, []);

  const closeDialog = useCallback(() => {
    setIsOpen(false);
    setCurrentDialog(null);
    if (resolvePromise) {
      resolvePromise();
      setResolvePromise(null);
    }
  }, [resolvePromise]);

  const value = {
    openDialog,
    closeDialog,
    isOpen,
    currentDialog,
  };

  return (
    <DialogContext.Provider value={value}>
      {children}
      {isOpen && currentDialog && (
        <DialogModal
          config={currentDialog}
          isOpen={isOpen}
          onClose={closeDialog}
        />
      )}
    </DialogContext.Provider>
  );
}

function DialogModal({
  config,
  isOpen,
  onClose,
}: { config: DialogConfig; isOpen: boolean; onClose: () => void }) {
  if (!isOpen) return null;

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Escape' && config.closeOnEscape !== false) {
      onClose();
    }
    if (e.key === 'Tab') {
      // Focus trap logic would go here
    }
  };

  return (
    <div
      className="sk-dialog-overlay"
      onClick={(e) => {
        if (e.target === e.currentTarget && config.closeOnOverlayClick !== false) {
          onClose();
        }
      }}
      role="dialog"
      aria-modal="true"
      aria-labelledby={config.title ? 'dialog-title' : undefined}
      aria-describedby={config.content ? 'dialog-content' : undefined}
      onKeyDown={handleKeyDown}
    >
      <div
        className={`sk-dialog sk-dialog--${config.size || 'md'}`}
        role="document"
      >
        {config.showCloseButton !== false && (
          <button
            className="sk-dialog__close"
            onClick={onClose}
            aria-label="Close dialog"
          >
            ×
          </button>
        )}
        {config.title && (
          <h2 id="dialog-title" className="sk-dialog__title">
            {config.title}
          </h2>
        )}
        <div id="dialog-content" className="sk-dialog__content">
          {config.content}
        </div>
        <div className="sk-dialog__actions">
          {config.secondaryAction && (
            <button
              className="sk-btn sk-btn--secondary"
              onClick={() => {
                config.secondaryAction?.onClick();
                onClose();
              }}
            >
              {config.secondaryAction.label}
            </button>
          )}
          {config.primaryAction && (
            <button
              className={`sk-btn sk-btn--${config.primaryAction.variant || 'primary'}`}
              onClick={async () => {
                await config.primaryAction?.onClick();
                onClose();
              }}
            >
              {config.primaryAction.label}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

export function useDialog() {
  const context = useContext(DialogContext);
  if (!context) {
    throw new Error('useDialog must be used within a DialogProvider');
  }
  return context;
}