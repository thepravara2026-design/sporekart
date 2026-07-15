import React from 'react';
import Dialog from '../../../../design-system/components/feedback/Dialog';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';

export interface UnsavedChangesGuardProps {
  unsaved: boolean;
  /** Title of the destination (e.g. product name) used in the dialog copy. */
  title?: string;
  /** Called when the user confirms leaving despite unsaved changes (discard). */
  onDiscard: () => void;
  children?: React.ReactNode;
}

export interface UnsavedGuardApi {
  /** Returns true if the leave was permitted (no unsaved changes or user confirmed). */
  requestLeave: (onLeave: () => void) => boolean;
}

const UnsavedChangesGuardContext = React.createContext<UnsavedGuardApi | null>(null);

export function useUnsavedGuard(): UnsavedGuardApi {
  const ctx = React.useContext(UnsavedChangesGuardContext);
  if (!ctx) {
    return { requestLeave: (onLeave) => { onLeave(); return true; } };
  }
  return ctx;
}

const UnsavedChangesGuard: React.FC<UnsavedChangesGuardProps> = ({ unsaved, title, onDiscard, children }) => {
  const [pendingLeave, setPendingLeave] = React.useState<(() => void) | null>(null);

  // Browser-level protection: refresh, close tab, navigate away.
  React.useEffect(() => {
    if (!unsaved) return;
    const handler = (e: BeforeUnloadEvent) => {
      e.preventDefault();
      e.returnValue = '';
      return '';
    };
    window.addEventListener('beforeunload', handler);
    return () => window.removeEventListener('beforeunload', handler);
  }, [unsaved]);

  const requestLeave = React.useCallback(
    (onLeave: () => void) => {
      if (!unsaved) {
        onLeave();
        return true;
      }
      setPendingLeave(() => onLeave);
      return false;
    },
    [unsaved],
  );

  const confirmLeave = React.useCallback(() => {
    if (pendingLeave) {
      onDiscard();
      const leave = pendingLeave;
      setPendingLeave(null);
      leave();
    }
  }, [pendingLeave, onDiscard]);

  const api = React.useMemo<UnsavedGuardApi>(() => ({ requestLeave }), [requestLeave]);

  return (
    <UnsavedChangesGuardContext.Provider value={api}>
      {children}
      <Dialog
        open={pendingLeave !== null}
        onClose={() => setPendingLeave(null)}
        title="Unsaved changes"
        aria-label="Unsaved changes confirmation"
        actions={
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 'var(--space-2)' }}>
            <Button
              variant="ghost"
              onClick={() => setPendingLeave(null)}
              leftIcon={<Icon name="X" size={16} />}
            >
              Stay on page
            </Button>
            <Button
              variant="destructive"
              onClick={confirmLeave}
              leftIcon={<Icon name="RefreshCw" size={16} />}
            >
              Leave &amp; discard
            </Button>
          </div>
        }
      >
        <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-sm)', lineHeight: 1.5 }}>
          You have unsaved changes{title ? ` to <strong>${title}</strong>` : ''}. If you leave this page, your changes will be lost.
          Save a draft first, or discard and continue.
        </p>
      </Dialog>
    </UnsavedChangesGuardContext.Provider>
  );
};

export default UnsavedChangesGuard;
