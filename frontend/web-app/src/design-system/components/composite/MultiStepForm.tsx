import React, { useState, useCallback, useId } from 'react';
import { StepIndicator, type Step } from './StepIndicator';
import { Button } from '../core/Button';

export interface MultiStepFormProps {
  steps: Step[];
  children: React.ReactNode | ((props: {
    currentStep: number;
    totalSteps: number;
    isFirst: boolean;
    isLast: boolean;
    goNext: () => void;
    goPrev: () => void;
    goToStep: (step: number) => void;
  }) => React.ReactNode);
  onComplete?: () => void;
  onStepChange?: (step: number) => void;
  validateBeforeNext?: boolean;
  initialStep?: number;
  orientation?: 'horizontal' | 'vertical';
  className?: string;
}

export const MultiStepForm: React.FC<MultiStepFormProps> = ({
  steps,
  children,
  onComplete,
  onStepChange,
  validateBeforeNext = false,
  initialStep = 0,
  orientation = 'horizontal',
  className = '',
}) => {
  const [currentStep, setCurrentStep] = useState(initialStep);
  const generatedId = useId();
  const formId = `sk-multistep-${generatedId}`;

  const totalSteps = steps.length;
  const isFirst = currentStep === 0;
  const isLast = currentStep === totalSteps - 1;

  const goNext = useCallback(() => {
    if (isLast) {
      onComplete?.();
      return;
    }
    setCurrentStep((prev) => {
      const next = prev + 1;
      onStepChange?.(next);
      return next;
    });
  }, [isLast, onComplete, onStepChange]);

  const goPrev = useCallback(() => {
    if (isFirst) return;
    setCurrentStep((prev) => {
      const next = prev - 1;
      onStepChange?.(next);
      return next;
    });
  }, [isFirst, onStepChange]);

  const goToStep = useCallback((step: number) => {
    if (step >= 0 && step < totalSteps) {
      setCurrentStep(step);
      onStepChange?.(step);
    }
  }, [totalSteps, onStepChange]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateBeforeNext) {
      return;
    }
    if (isLast) {
      onComplete?.();
    } else {
      goNext();
    }
  };

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-component-gap)',
    width: '100%',
  };

  const navStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 'var(--space-inline-md)',
    paddingTop: 'var(--space-stack-md)',
    borderTop: '1px solid var(--color-border-default)',
    marginTop: 'var(--space-stack-md)',
  };

  const renderContent = () => {
    if (typeof children === 'function') {
      return children({ currentStep, totalSteps, isFirst, isLast, goNext, goPrev, goToStep });
    }
    return children;
  };

  return (
    <form
      id={formId}
      className={`sk-multi-step-form ${className}`}
      style={containerStyle}
      onSubmit={handleSubmit}
      noValidate
      aria-label="Multi-step form"
    >
      <StepIndicator
        steps={steps}
        currentStep={currentStep}
        orientation={orientation}
        onChange={goToStep}
        aria-label="Form steps"
      />

      <div
        aria-live="polite"
        aria-atomic="false"
        style={{ minHeight: 200 }}
      >
        {renderContent()}
      </div>

      <div style={navStyle}>
        <Button
          type="button"
          variant="outline"
          onClick={goPrev}
          disabled={isFirst}
          aria-disabled={isFirst}
        >
          Previous
        </Button>
        <Button
          type="submit"
          variant="primary"
        >
          {isLast ? 'Complete' : 'Next'}
        </Button>
      </div>
    </form>
  );
};

MultiStepForm.displayName = 'MultiStepForm';

export default MultiStepForm;
