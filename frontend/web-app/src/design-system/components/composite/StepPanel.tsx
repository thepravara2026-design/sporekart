import React from 'react';

export interface StepPanelProps {
  children: React.ReactNode;
  stepId: string;
  active?: boolean;
  className?: string;
}

export const StepPanel: React.FC<StepPanelProps> = ({
  children,
  stepId,
  active = false,
  className = '',
}) => {
  const panelStyle: React.CSSProperties = {
    display: active ? 'block' : 'none',
    animation: active ? 'sk-fade-in var(--duration-normal) var(--easing-standard)' : 'none',
  };

  return (
    <div
      id={stepId}
      role="tabpanel"
      className={`sk-step-panel ${className}`}
      style={panelStyle}
      hidden={!active}
    >
      {children}
      <style>{`
        @keyframes sk-fade-in {
          from {
            opacity: 0;
            transform: translateY(8px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `}</style>
    </div>
  );
};

StepPanel.displayName = 'StepPanel';

export default StepPanel;
