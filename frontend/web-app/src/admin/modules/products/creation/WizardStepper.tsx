import React from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { WizardStepId } from './types';
import { STEP_LABELS, STEP_ORDER } from './validation';

export interface WizardStepperProps {
  current: WizardStepId;
  visited: Set<WizardStepId>;
  onStepClick: (id: WizardStepId) => void;
}

const WizardStepper: React.FC<WizardStepperProps> = ({ current, visited, onStepClick }) => {
  const currentIndex = STEP_ORDER.indexOf(current);

  return (
    <nav aria-label="Product creation steps" style={{ overflowX: 'auto' }}>
      <ol
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 0,
          listStyle: 'none',
          margin: 0,
          padding: 'var(--space-stack-xs) 0',
          minWidth: 'max-content',
        }}
      >
        {STEP_ORDER.map((id, index) => {
          const isCurrent = id === current;
          const isVisited = visited.has(id);
          const isCompleted = isVisited && !isCurrent;
          const isClickable = isVisited && !isCurrent;

          let circleBg = 'var(--color-bg-surface-default)';
          let circleBorder = 'var(--color-border-default)';
          let circleColor = 'var(--color-text-secondary)';
          if (isCurrent) {
            circleBg = 'var(--color-bg-primary-default)';
            circleBorder = 'var(--color-bg-primary-default)';
            circleColor = 'var(--color-text-on-primary)';
          } else if (isCompleted) {
            circleBg = 'var(--color-bg-success-weak)';
            circleBorder = 'var(--color-success)';
            circleColor = 'var(--color-text-success)';
          }

          const labelColor = isCurrent || isCompleted ? 'var(--color-text-primary)' : 'var(--color-text-secondary)';
          const labelWeight = isCurrent ? 'var(--weight-semibold)' : 'var(--weight-normal)';

          return (
            <li key={id} style={{ display: 'flex', alignItems: 'center', flex: 1, minWidth: 0 }}>
              <button
                type="button"
                onClick={() => isClickable && onStepClick(id)}
                disabled={!isClickable}
                aria-current={isCurrent ? 'step' : undefined}
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  gap: 'var(--space-stack-xs)',
                  background: 'none',
                  border: 'none',
                  cursor: isClickable ? 'pointer' : 'default',
                  padding: '4px 8px',
                  flex: 1,
                  minWidth: 0,
                }}
              >
                <span
                  style={{
                    width: 32,
                    height: 32,
                    borderRadius: 'var(--radius-full)',
                    background: circleBg,
                    border: `2px solid ${circleBorder}`,
                    color: circleColor,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontWeight: 'var(--weight-semibold)',
                    fontSize: 'var(--text-body-sm)',
                    flexShrink: 0,
                  }}
                >
                  {isCompleted ? <Icon name="Check" size={16} /> : index + 1}
                </span>
                <span
                  style={{
                    fontSize: 'var(--text-caption)',
                    color: labelColor,
                    fontWeight: labelWeight,
                    whiteSpace: 'nowrap',
                  }}
                >
                  {STEP_LABELS[id]}
                </span>
              </button>
              {index < STEP_ORDER.length - 1 && (
                <span
                  aria-hidden="true"
                  style={{
                    flex: 1,
                    height: 2,
                    minWidth: 16,
                    background: index < currentIndex ? 'var(--color-success)' : 'var(--color-border-default)',
                  }}
                />
              )}
            </li>
          );
        })}
      </ol>
    </nav>
  );
};

export default React.memo(WizardStepper);
