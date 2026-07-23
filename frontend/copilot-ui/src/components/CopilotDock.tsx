import { type CopilotStatus } from '../types';
import { CopilotFloatingButton } from './CopilotFloatingButton';

interface CopilotDockProps {
  copilotType: string;
  onToggle: () => void;
  isOpen: boolean;
  status: CopilotStatus;
  unread?: boolean;
}

export function CopilotDock({ copilotType, onToggle, isOpen, status, unread }: CopilotDockProps) {
  return (
    <div className="cp-dock" role="complementary" aria-label={`${copilotType} copilot dock`}>
      <CopilotFloatingButton
        onClick={onToggle}
        isOpen={isOpen}
        status={status}
        unread={unread}
        label={`Toggle ${copilotType} copilot`}
      />
    </div>
  );
}
