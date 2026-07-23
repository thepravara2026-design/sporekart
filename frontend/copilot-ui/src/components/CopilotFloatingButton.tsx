import { useEffect, useState, useCallback, type KeyboardEvent } from 'react';
import { type CopilotStatus } from '../types';

interface CopilotFloatingButtonProps {
  onClick: () => void;
  isOpen: boolean;
  status: CopilotStatus;
  unread?: boolean;
  label: string;
}

const statusColors: Record<CopilotStatus, string> = {
  active: '#2e7d32',
  inactive: '#8c8c8c',
  degraded: '#f57f17',
  error: '#c62828',
};

export function CopilotFloatingButton({ onClick, isOpen, status, unread, label }: CopilotFloatingButtonProps) {
  const [tooltipVisible, setTooltipVisible] = useState(false);

  const handleKeyDown = useCallback(
    (e: KeyboardEvent<HTMLButtonElement>) => {
      if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        onClick();
      }
    },
    [onClick],
  );

  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        onClick();
      }
    };
    document.addEventListener('keydown', handler as unknown as EventListener);
    return () => document.removeEventListener('keydown', handler as unknown as EventListener);
  }, [onClick]);

  return (
    <button
      type="button"
      className={`cp-floating-btn${isOpen ? ' cp-floating-btn--active' : ''}${unread ? ' cp-floating-btn--unread' : ''}`}
      onClick={onClick}
      onKeyDown={handleKeyDown}
      onMouseEnter={() => setTooltipVisible(true)}
      onMouseLeave={() => setTooltipVisible(false)}
      aria-label={label}
      aria-expanded={isOpen}
      aria-controls="copilot-panel"
    >
      <span className="cp-floating-btn__icon" aria-hidden="true">
        {isOpen ? '\u2715' : '\uD83E\uDD16'}
      </span>
      <span
        className="cp-floating-btn__badge"
        style={{ backgroundColor: statusColors[status] }}
        aria-label={`Status: ${status}`}
      />
      {tooltipVisible && !isOpen && (
        <span className="cp-floating-btn__tooltip" role="tooltip">
          {label} (Ctrl+K)
        </span>
      )}
    </button>
  );
}
