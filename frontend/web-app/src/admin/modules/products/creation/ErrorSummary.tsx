import React from 'react';
import type { WizardErrors, WizardStepId } from './types';
import { FIELD_LABELS, STEP_LABELS, STEP_ORDER, getStepFields } from './validation';

export interface ErrorSummaryProps {
  errors: WizardErrors;
  steps?: WizardStepId[];
  onJump?: (step: WizardStepId) => void;
  title?: string;
  compact?: boolean;
}

const ErrorSummary: React.FC<ErrorSummaryProps> = ({ errors, steps, onJump, title = 'Please fix the following', compact }) => {
  const stepsToShow = steps ?? STEP_ORDER.filter((s) => s !== 'review' && s !== 'confirmation');
  const groups = stepsToShow
    .map((step) => {
      const fields = getStepFields(step)
        .filter((f) => errors[f])
        .map((f) => ({ field: f, message: errors[f] }));
      return { step, fields };
    })
    .filter((g) => g.fields.length > 0);

  if (groups.length === 0) return null;

  const total = groups.reduce((n, g) => n + g.fields.length, 0);

  return (
    <div
      role="alert"
      style={{
        border: '1px solid var(--color-border-error)',
        background: 'var(--color-bg-danger-weak)',
        borderRadius: 'var(--radius-md)',
        padding: compact ? 'var(--space-stack-sm) var(--space-inline-md)' : 'var(--space-stack-md) var(--space-inline-md)',
        marginBottom: 'var(--space-component-gap)',
      }}
    >
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 'var(--space-inline-xs)',
          color: 'var(--color-text-danger)',
          fontWeight: 'var(--weight-semibold)',
          fontSize: 'var(--text-body-sm)',
          marginBottom: groups.length > 1 && !compact ? 'var(--space-stack-xs)' : 0,
        }}
      >
        <span aria-hidden="true">!</span>
        <span>
          {title}
          {!compact ? ` (${total})` : ''}
        </span>
      </div>
      {groups.map((g) => (
        <div key={g.step} style={{ marginTop: groups.length > 1 ? 'var(--space-stack-xs)' : 0 }}>
          {groups.length > 1 && (
            <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-secondary)' }}>
              {STEP_LABELS[g.step]}
            </div>
          )}
          <ul style={{ margin: '4px 0 0', paddingLeft: 'var(--space-inline-lg)' }}>
            {g.fields.map((f) => (
              <li key={f.field} style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                {onJump ? (
                  <button
                    type="button"
                    onClick={() => onJump(g.step)}
                    style={{
                      background: 'none',
                      border: 'none',
                      padding: 0,
                      color: 'inherit',
                      cursor: 'pointer',
                      font: 'inherit',
                      textDecoration: 'underline',
                    }}
                  >
                    {FIELD_LABELS[f.field] ?? f.field}
                  </button>
                ) : (
                  <span>{FIELD_LABELS[f.field] ?? f.field}</span>
                )}
                {f.message ? ` — ${f.message}` : ''}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

export default ErrorSummary;
