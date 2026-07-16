import { Card } from '../../../../../design-system/components/composite/Card';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { useBuilderContext } from '../../state/BuilderContext';

export function SaveIndicator() {
  const { state, markSaved } = useBuilderContext();

  const handleSave = () => {
    markSaved();
  };

  const dotColor = state.unsavedChanges ? 'var(--color-warning-500)' : 'var(--color-success-500)';
  const dotPulse = state.unsavedChanges ? 'pulse 2s infinite' : 'none';

  return (
    <Card variant="outlined" padding="sm">
      <Inline gap="md" align="center">
        <span
          style={{
            width: 8,
            height: 8,
            borderRadius: '50%',
            backgroundColor: dotColor,
            animation: dotPulse,
            display: 'inline-block',
          }}
        />
        <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
          {state.unsavedChanges ? 'Unsaved changes' : state.lastSavedAt ? `Saved at ${new Date(state.lastSavedAt).toLocaleTimeString()}` : 'No changes yet'}
        </span>
        <button
          onClick={handleSave}
          disabled={!state.unsavedChanges}
          style={{
            padding: '4px 12px',
            fontSize: 'var(--font-size-sm)',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: state.unsavedChanges ? 'var(--color-primary-500)' : 'var(--color-bg-disabled)',
            color: state.unsavedChanges ? '#fff' : 'var(--color-text-disabled)',
            cursor: state.unsavedChanges ? 'pointer' : 'not-allowed',
          }}
        >
          Save
        </button>
        <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
          v{state.versionNumber}
        </span>
      </Inline>
    </Card>
  );
}
