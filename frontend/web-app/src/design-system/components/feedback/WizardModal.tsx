import React, { useState, useCallback } from 'react';
import { Modal, ModalProps } from './Modal';
import { Button } from '../core/Button';

export interface WizardStep {
  title: string;
  content: React.ReactNode;
}

export interface WizardModalProps extends Omit<ModalProps, 'children' | 'title' | 'actions'> {
  steps: WizardStep[];
  stepTitles?: string[];
  onFinish: () => void;
  backLabel?: string;
  nextLabel?: string;
  finishLabel?: string;
}

export const WizardModal: React.FC<WizardModalProps> = ({
  open,
  onClose,
  steps,
  stepTitles,
  onFinish,
  backLabel = 'Back',
  nextLabel = 'Next',
  finishLabel = 'Finish',
  size = 'lg',
  ...props
}) => {
  const [currentStep, setCurrentStep] = useState(0);
  const totalSteps = steps.length;

  React.useEffect(() => {
    if (!open) {
      setCurrentStep(0);
    }
  }, [open]);

  const handleBack = useCallback(() => {
    setCurrentStep((prev) => Math.max(0, prev - 1));
  }, []);

  const handleNext = useCallback(() => {
    if (currentStep < totalSteps - 1) {
      setCurrentStep((prev) => prev + 1);
    }
  }, [currentStep, totalSteps]);

  const isFirstStep = currentStep === 0;
  const isLastStep = currentStep === totalSteps - 1;

  const stepIndicatorStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-2)',
    marginBottom: 'var(--space-4)',
  };

  const dotBaseStyle: React.CSSProperties = {
    width: 8,
    height: 8,
    borderRadius: '50%',
    border: 'none',
    padding: 0,
    transition: 'background-color var(--duration-fast) var(--easing-standard)',
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={steps[currentStep]?.title || ''}
      size={size}
      {...props}
      actions={
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          width: '100%',
        } as React.CSSProperties}>
          <div style={stepIndicatorStyle}>
            {steps.map((_, index) => (
              <span
                key={index}
                style={{
                  ...dotBaseStyle,
                  backgroundColor: index === currentStep
                    ? 'var(--color-bg-primary-default)'
                    : 'var(--color-border-default)',
                  cursor: 'default',
                } as React.CSSProperties}
                aria-hidden="true"
              />
            ))}
          </div>
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-3)',
          } as React.CSSProperties}>
            {!isFirstStep && (
              <Button variant="secondary" onClick={handleBack}>
                {backLabel}
              </Button>
            )}
            {isLastStep ? (
              <Button variant="primary" onClick={onFinish}>
                {finishLabel}
              </Button>
            ) : (
              <Button variant="primary" onClick={handleNext}>
                {nextLabel}
              </Button>
            )}
          </div>
        </div>
      }
    >
      {currentStep + 1 <= totalSteps && (
        <div key={currentStep}>
          {steps[currentStep].content}
        </div>
      )}
    </Modal>
  );
};

WizardModal.displayName = 'WizardModal';
export default WizardModal;
