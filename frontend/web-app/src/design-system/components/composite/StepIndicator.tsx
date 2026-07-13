import React from 'react';

export interface Step {
  id: string;
  label: string;
  description?: string;
  icon?: React.ReactNode;
}

export interface StepIndicatorProps {
  steps: Step[];
  currentStep: number;
  orientation?: 'horizontal' | 'vertical';
  onChange?: (step: number) => void;
  className?: string;
}

export const StepIndicator: React.FC<StepIndicatorProps> = ({
  steps,
  currentStep,
  orientation = 'horizontal',
  onChange,
  className = '',
}) => {
  const progressPercent = steps.length > 0
    ? ((currentStep) / (steps.length - 1)) * 100
    : 0;

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: orientation === 'vertical' ? 'column' : 'column',
    gap: 'var(--space-stack-md)',
    width: '100%',
  };

  const progressBarWrapperStyle: React.CSSProperties = {
    position: 'relative',
    width: '100%',
    height: 'var(--space-1)',
    background: 'var(--color-border-default)',
    borderRadius: 'var(--radius-full)',
    overflow: 'hidden',
  };

  const progressBarStyle: React.CSSProperties = {
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: `${progressPercent}%`,
    background: 'var(--color-bg-primary-default)',
    borderRadius: 'var(--radius-full)',
    transition: 'width var(--duration-normal) var(--easing-standard)',
  };

  const stepsRowStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: orientation === 'vertical' ? 'column' : 'row',
    alignItems: orientation === 'vertical' ? 'flex-start' : 'flex-start',
    gap: orientation === 'vertical' ? 'var(--space-stack-md)' : 'var(--space-0)',
    position: 'relative',
  };

  const stepItemStyle = (index: number): React.CSSProperties => {
    const isCompleted = index < currentStep;
    const isClickable = isCompleted && onChange;

    return {
      display: 'flex',
      flexDirection: orientation === 'vertical' ? 'row' : 'column',
      alignItems: 'center',
      gap: orientation === 'vertical' ? 'var(--space-inline-sm)' : 'var(--space-stack-xs)',
      flex: orientation === 'vertical' ? '0 0 auto' : 1,
      cursor: isClickable ? 'pointer' : 'default',
      position: 'relative',
      ...(isClickable ? {
        outline: 'none',
      } : {}),
    };
  };

  const circleSize = 32;

  const circleStyle = (index: number): React.CSSProperties => {
    const isCompleted = index < currentStep;
    const isActive = index === currentStep;

    return {
      width: `${circleSize}px`,
      height: `${circleSize}px`,
      borderRadius: '50%',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      fontFamily: 'var(--font-family-sans)',
      fontWeight: 'var(--weight-semibold)',
      fontSize: 'var(--text-caption)',
      lineHeight: 1,
      flexShrink: 0,
      transition: 'all var(--duration-fast) var(--easing-standard)',
      background: isCompleted
        ? 'var(--color-bg-primary-default)'
        : isActive
          ? 'var(--color-bg-surface-default)'
          : 'var(--color-bg-surface-default)',
      color: isCompleted
        ? 'var(--color-text-on-primary)'
        : isActive
          ? 'var(--color-bg-primary-default)'
          : 'var(--color-text-disabled)',
      border: isActive
        ? '2px solid var(--color-bg-primary-default)'
        : isCompleted
          ? '2px solid var(--color-bg-primary-default)'
          : '2px solid var(--color-border-default)',
      ...(isActive ? {
        boxShadow: '0 0 0 3px var(--color-focus-ring)',
      } : {}),
    };
  };

  const labelStyle = (index: number): React.CSSProperties => {
    const isActive = index === currentStep;
    const isCompleted = index < currentStep;

    return {
      fontFamily: 'var(--font-family-sans)',
      fontWeight: isActive ? 'var(--weight-semibold)' : 'var(--weight-medium)',
      fontSize: 'var(--text-caption)',
      lineHeight: 'var(--leading-normal)',
      color: isActive
        ? 'var(--color-text-primary)'
        : isCompleted
          ? 'var(--color-text-primary)'
          : 'var(--color-text-disabled)',
      textAlign: orientation === 'vertical' ? 'left' : 'center',
      whiteSpace: orientation === 'vertical' ? 'normal' : 'nowrap',
    };
  };

  const connectorStyle = (index: number): React.CSSProperties => {
    if (orientation === 'vertical') {
      return {};
    }
    if (index >= steps.length - 1) {
      return { display: 'none' };
    }
    const isCompleted = index < currentStep - 1;
    const isCurrent = index === currentStep - 1;

    return {
      flex: 1,
      height: '2px',
      background: isCompleted || isCurrent
        ? 'var(--color-bg-primary-default)'
        : 'var(--color-border-default)',
      alignSelf: 'center',
      marginBottom: `${circleSize + 8}px`,
      transition: 'background var(--duration-fast) var(--easing-standard)',
    };
  };

  const handleStepClick = (index: number) => {
    if (index < currentStep && onChange) {
      onChange(index);
    }
  };

  const handleStepKeyDown = (e: React.KeyboardEvent, index: number) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      handleStepClick(index);
    }
  };

  const iconSize = 'var(--icon-xs)';

  return (
    <nav
      className={`sk-step-indicator sk-step-indicator--${orientation} ${className}`}
      style={containerStyle}
      aria-label="Progress"
    >
      <div style={stepsRowStyle} role="tablist" aria-orientation={orientation}>
        {steps.map((step, index) => (
          <React.Fragment key={step.id}>
            <div
              role="tab"
              aria-selected={index === currentStep}
              aria-disabled={index > currentStep}
              aria-setsize={steps.length}
              aria-posinset={index + 1}
              tabIndex={index < currentStep ? 0 : -1}
              style={{
                ...stepItemStyle(index),
                ...(orientation === 'vertical'
                  ? { width: '100%' }
                  : { flex: 1 }),
              }}
              onClick={() => handleStepClick(index)}
              onKeyDown={(e) => handleStepKeyDown(e, index)}
            >
              <div style={circleStyle(index)}>
                {index < currentStep ? (
                  <svg width={iconSize} height={iconSize} viewBox="0 0 16 16" fill="none" aria-hidden="true">
                    <path d="M13.3 4.3L6 11.6L2.7 8.3" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                ) : (
                  <span>{index + 1}</span>
                )}
              </div>
              <div style={labelStyle(index)}>
                {step.label}
              </div>
            </div>
            {index < steps.length - 1 && (
              <div
                aria-hidden="true"
                style={connectorStyle(index)}
              />
            )}
          </React.Fragment>
        ))}
      </div>
      <div style={progressBarWrapperStyle} role="progressbar" aria-valuenow={currentStep + 1} aria-valuemin={1} aria-valuemax={steps.length} aria-label="Step progress">
        <div style={progressBarStyle} />
      </div>
    </nav>
  );
};

StepIndicator.displayName = 'StepIndicator';

export default StepIndicator;
