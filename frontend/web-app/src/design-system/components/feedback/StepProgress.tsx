import React from 'react';

export interface Step {
  label: string;
  completed?: boolean;
  active?: boolean;
  error?: boolean;
}

export interface StepProgressProps {
  steps: Step[];
  currentStep?: number;
  orientation?: 'horizontal' | 'vertical';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  style?: React.CSSProperties;
}

const sizeMap = { sm: 24, md: 32, lg: 40 };
const fontSizeMap = { sm: 'var(--text-caption)', md: 'var(--text-body-sm)', lg: 'var(--text-body)' };
const connectorWidthMap = { sm: 2, md: 3, lg: 4 };

function CheckIcon({ size: iconSize }: { size: number }) {
  return (
    <svg width={iconSize * 0.5} height={iconSize * 0.5} viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="3 8 7 12 13 4" />
    </svg>
  );
}

export const StepProgress: React.FC<StepProgressProps> = ({
  steps,
  currentStep,
  orientation = 'horizontal',
  size = 'md',
  className = '',
  style,
}) => {
  const stepSize = sizeMap[size];
  const fontSize = fontSizeMap[size];
  const connectorWidth = connectorWidthMap[size];

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: orientation === 'vertical' ? 'column' : 'row',
    alignItems: orientation === 'vertical' ? 'flex-start' : 'center',
    gap: 0,
    ...style,
  };

  const stepRowStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: orientation === 'vertical' ? 'row' : 'column',
    alignItems: 'center',
    gap: orientation === 'vertical' ? 'var(--space-inline-md)' : 'var(--space-stack-xs)',
    flex: orientation === 'horizontal' ? 1 : undefined,
    position: 'relative',
  };

  const circleStyle = (step: Step, index: number): React.CSSProperties => {
    const isCompleted = step.completed;
    const isActive = step.active || (currentStep !== undefined && index === currentStep);
    const isError = step.error;

    let backgroundColor = 'transparent';
    let borderColor = 'var(--color-neutral-300)';
    let textColor = 'var(--color-text-secondary)';
    let borderWidth = connectorWidth * 1.5;

    if (isError) {
      backgroundColor = 'var(--color-danger-500)';
      borderColor = 'var(--color-danger-500)';
      textColor = '#FFFFFF';
    } else if (isCompleted) {
      backgroundColor = 'var(--color-success-500)';
      borderColor = 'var(--color-success-500)';
      textColor = '#FFFFFF';
    } else if (isActive) {
      backgroundColor = 'var(--color-bg-primary-default)';
      borderColor = 'var(--color-bg-primary-default)';
      textColor = '#FFFFFF';
    } else {
      backgroundColor = 'transparent';
      borderColor = 'var(--color-neutral-300)';
      textColor = 'var(--color-text-secondary)';
    }

    return {
      width: stepSize,
      height: stepSize,
      borderRadius: 'var(--radius-full)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor,
      border: `${borderWidth}px solid ${borderColor}`,
      color: textColor,
      fontSize: fontSize,
      fontWeight: 'var(--weight-semibold)',
      flexShrink: 0,
      transition: `all var(--duration-fast) var(--easing-standard)`,
    };
  };

  const connectorStyle = (index: number): React.CSSProperties => {
    const isCompleted = steps[index]?.completed;
    const isNextCompleted = steps[index + 1]?.completed;
    const isActive =
      (currentStep !== undefined && index < currentStep) ||
      (currentStep !== undefined && index < steps.length - 1 && index < currentStep);

    let bgColor: string;
    if (isCompleted && isNextCompleted) {
      bgColor = 'var(--color-success-500)';
    } else if (isActive) {
      bgColor = 'var(--color-bg-primary-default)';
    } else {
      bgColor = 'var(--color-neutral-200)';
    }

    if (orientation === 'horizontal') {
      return {
        flex: 1,
        height: connectorWidth,
        backgroundColor: bgColor,
        minWidth: 24,
        transition: `background-color var(--duration-slow) var(--easing-standard)`,
      };
    }

    return {
      width: connectorWidth,
      height: 24,
      backgroundColor: bgColor,
      marginLeft: (stepSize - connectorWidth) / 2,
      transition: `background-color var(--duration-slow) var(--easing-standard)`,
    };
  };

  return (
    <div className={`sk-step-progress sk-step-progress--${orientation} ${className}`.trim()} style={containerStyle}>
      {steps.map((step, index) => (
        <React.Fragment key={index}>
          <div style={stepRowStyle}>
            <div style={circleStyle(step, index)} role="img" aria-label={`Step ${index + 1}: ${step.label}`}>
              {step.completed ? (
                <CheckIcon size={stepSize} />
              ) : (
                <span>{index + 1}</span>
              )}
            </div>
            <span style={{ fontSize, color: step.active || step.completed ? 'var(--color-text-primary)' : 'var(--color-text-secondary)', fontWeight: step.active ? 'var(--weight-semibold)' : 'var(--weight-normal)', whiteSpace: 'nowrap' }}>
              {step.label}
            </span>
          </div>
          {index < steps.length - 1 && <div style={connectorStyle(index)} />}
        </React.Fragment>
      ))}
    </div>
  );
};

StepProgress.displayName = 'StepProgress';
export default StepProgress;
