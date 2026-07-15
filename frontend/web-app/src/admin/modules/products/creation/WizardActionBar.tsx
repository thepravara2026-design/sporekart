import React from 'react';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { Dialog } from '../../../../design-system/components/feedback/Dialog';
import type { WizardStepId } from './types';
import { STEP_ORDER } from './validation';

export interface WizardActionBarProps {
  step: WizardStepId;
  unsaved: boolean;
  isDraft: boolean;
  canSubmit?: boolean;
  submitting?: boolean;
  onBack: () => void;
  onNext: () => void;
  onSaveDraft: () => void;
  onDiscard: () => void;
  onCancel: () => void;
  onSubmit: () => void;
}

const WizardActionBar: React.FC<WizardActionBarProps> = ({
  step,
  unsaved,
  isDraft,
  canSubmit = true,
  submitting = false,
  onBack,
  onNext,
  onSaveDraft,
  onDiscard,
  onCancel,
  onSubmit,
}) => {
  const [discardOpen, setDiscardOpen] = React.useState(false);
  const [cancelOpen, setCancelOpen] = React.useState(false);

  if (step === 'confirmation') return null;

  const isFirst = STEP_ORDER.indexOf(step) === 0;
  const isReview = step === 'review';

  const handleCancelClick = () => {
    if (submitting) return;
    if (unsaved) setCancelOpen(true);
    else onCancel();
  };

  return (
    <>
      <div
        role="toolbar"
        aria-label="Wizard actions"
        style={{
          position: 'sticky',
          bottom: 0,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 'var(--space-inline-sm)',
          flexWrap: 'wrap',
          padding: 'var(--space-stack-md) 0',
          marginTop: 'var(--space-component-gap)',
          borderTop: '1px solid var(--color-border)',
          background: 'var(--color-bg-surface-default)',
          zIndex: 5,
        }}
      >
        <div style={{ display: 'flex', gap: 'var(--space-inline-xs)' }}>
          <Button variant="ghost" onClick={handleCancelClick} leftIcon={<Icon name="X" size={18} />} disabled={submitting}>
            Cancel
          </Button>
          <Button
            variant="ghost"
            onClick={() => !submitting && setDiscardOpen(true)}
            leftIcon={<Icon name="Trash" size={18} />}
            disabled={submitting}
          >
            Discard
          </Button>
        </div>

        <div style={{ display: 'flex', gap: 'var(--space-inline-xs)', alignItems: 'center' }}>
          <Button
            variant="secondary"
            onClick={onSaveDraft}
            leftIcon={<Icon name="Save" size={18} />}
            disabled={submitting || (isDraft && !unsaved)}
          >
            {isDraft ? 'Update Draft' : 'Save Draft'}
          </Button>
          {!isFirst && (
            <Button variant="secondary" onClick={onBack} leftIcon={<Icon name="arrow-left" size={18} />} disabled={submitting}>
              Back
            </Button>
          )}
          {isReview ? (
            <Button
              variant="primary"
              onClick={onSubmit}
              disabled={!canSubmit || submitting}
              leftIcon={<Icon name="Check" size={18} />}
              title={canSubmit ? undefined : 'Fix the listed errors before submitting'}
            >
              {submitting ? 'Creating…' : 'Create Product'}
            </Button>
          ) : (
            <Button variant="primary" onClick={onNext} rightIcon={<Icon name="arrow-right" size={18} />} disabled={submitting}>
              Next
            </Button>
          )}
        </div>
      </div>

      <Dialog
        open={discardOpen}
        onClose={() => setDiscardOpen(false)}
        title="Discard this product?"
        actions={
          <>
            <Button variant="ghost" onClick={() => setDiscardOpen(false)}>
              Keep Editing
            </Button>
            <Button
              variant="destructive"
              onClick={() => {
                setDiscardOpen(false);
                onDiscard();
              }}
            >
              Discard
            </Button>
          </>
        }
      >
        This will permanently remove the saved draft (if any) and clear all entered information. This action cannot be undone.
      </Dialog>

      <Dialog
        open={cancelOpen}
        onClose={() => setCancelOpen(false)}
        title="Discard unsaved changes?"
        actions={
          <>
            <Button variant="ghost" onClick={() => setCancelOpen(false)}>
              Keep Editing
            </Button>
            <Button
              variant="destructive"
              onClick={() => {
                setCancelOpen(false);
                onCancel();
              }}
            >
              Leave
            </Button>
          </>
        }
      >
        You have unsaved changes. If you leave now, your progress will be lost unless you saved a draft.
      </Dialog>
    </>
  );
};

export default React.memo(WizardActionBar);
