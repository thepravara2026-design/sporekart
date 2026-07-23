import { type CopilotPersona, type CopilotStatus } from '../types';

interface CopilotHeaderProps {
  name: string;
  persona: CopilotPersona;
  status: CopilotStatus;
  onClose: () => void;
  onMinimize?: () => void;
  onSettings?: () => void;
}

const statusLabels: Record<CopilotStatus, string> = {
  active: 'Online',
  inactive: 'Offline',
  degraded: 'Degraded',
  error: 'Error',
};

export function CopilotHeader({ name, persona, status, onClose, onMinimize, onSettings }: CopilotHeaderProps) {
  return (
    <header className="cp-header" role="banner">
      <div className="cp-header__left">
        <span className="cp-header__avatar" aria-hidden="true">
          &#x1F916;
        </span>
        <div className="cp-header__info">
          <span className="cp-header__name">{name}</span>
          <span className="cp-header__status" data-status={status}>
            <span className={`cp-header__dot cp-header__dot--${status}`} />
            {statusLabels[status]}
          </span>
        </div>
      </div>
      <span className="cp-header__persona-badge">{persona.role}</span>
      <div className="cp-header__right">
        {onSettings && (
          <button
            type="button"
            className="cp-header__icon-btn"
            onClick={onSettings}
            aria-label="Settings"
          >
            &#x2699;
          </button>
        )}
        {onMinimize && (
          <button
            type="button"
            className="cp-header__icon-btn"
            onClick={onMinimize}
            aria-label="Minimize"
          >
            &#x2212;
          </button>
        )}
        <button
          type="button"
          className="cp-header__icon-btn"
          onClick={onClose}
          aria-label="Close copilot"
        >
          &#x2715;
        </button>
      </div>
    </header>
  );
}
