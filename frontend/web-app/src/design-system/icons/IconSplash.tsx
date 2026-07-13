import React from 'react';

export interface IconSplashProps {
  size?: number;
  className?: string;
}

export const IconSplash: React.FC<IconSplashProps> = ({
  size = 24,
  className = '',
}) => {
  const style: React.CSSProperties = {
    display: 'inline-flex',
    width: size,
    height: size,
    borderRadius: 'var(--radius-sm, 4px)',
    backgroundColor: 'var(--color-bg-surface-raised, #e0e0e0)',
    animation: 'sk-icon-splash-pulse 1.5s ease-in-out infinite',
    flexShrink: 0,
  };

  return (
    <>
      <span
        className={`sk-icon-splash ${className}`}
        style={style}
        aria-hidden="true"
      />
      <style>{`
        @keyframes sk-icon-splash-pulse {
          0%, 100% { opacity: 0.4; }
          50% { opacity: 0.8; }
        }
      `}</style>
    </>
  );
};

IconSplash.displayName = 'IconSplash';

export default IconSplash;
