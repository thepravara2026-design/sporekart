import React from 'react';
import { Button } from '../../components/core/Button';

export interface FormActionsProps {
  children?: React.ReactNode;
  submitLabel?: string;
  cancelLabel?: string;
  onCancel?: () => void;
  submitting?: boolean;
  submittingLabel?: string;
  disabled?: boolean;
  cancelFirst?: boolean;
  align?: 'left' | 'center' | 'right';
  sticky?: boolean;
  className?: string;
}

export const FormActions: React.FC<FormActionsProps> = ({
  children,
  submitLabel = 'Submit',
  cancelLabel = 'Cancel',
  onCancel,
  submitting = false,
  submittingLabel = 'Submitting...',
  disabled = false,
  cancelFirst = false,
  align = 'right',
  sticky = false,
  className = '',
}) => {
  const justifyMap: Record<string, string> = {
    left: 'flex-start',
    center: 'center',
    right: 'flex-end',
  };

  const actionsStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-md)',
    justifyContent: justifyMap[align] ?? 'flex-end',
    flexWrap: 'wrap',
    paddingTop: 'var(--space-stack-md)',
    ...(sticky
      ? {
          position: 'sticky',
          bottom: 0,
          background: 'var(--color-bg-surface-default)',
          zIndex: 'var(--z-sticky)',
          paddingBottom: 'var(--space-stack-md)',
          borderTop: '1px solid var(--color-border-default)',
          marginTop: 'var(--space-stack-md)',
        }
      : {}),
  };

  const buttons: React.ReactNode[] = [];

  if (cancelFirst && onCancel) {
    buttons.push(
      <Button
        key="cancel"
        type="button"
        variant="outline"
        onClick={onCancel}
        disabled={disabled || submitting}
      >
        {cancelLabel}
      </Button>
    );
  }

  buttons.push(
    <Button
      key="submit"
      type="submit"
      variant="primary"
      loading={submitting}
      disabled={disabled || submitting}
      aria-busy={submitting}
    >
      {submitting ? submittingLabel : submitLabel}
    </Button>
  );

  if (!cancelFirst && onCancel) {
    buttons.push(
      <Button
        key="cancel"
        type="button"
        variant="outline"
        onClick={onCancel}
        disabled={disabled || submitting}
      >
        {cancelLabel}
      </Button>
    );
  }

  return (
    <div
      className={`sk-form-actions ${className}`}
      style={actionsStyle}
    >
      {children}
      {buttons}
    </div>
  );
};

FormActions.displayName = 'FormActions';

export default FormActions;
