// Types
export type {
  CopilotMessage,
  Suggestion,
  CopilotContext,
  CopilotPersona,
  CopilotStatus,
  MemoryEntry,
  KnowledgeReference,
  ToolExecution,
  QuickAction,
} from './types';

// Components
export { CopilotDock } from './components/CopilotDock';
export { CopilotFloatingButton } from './components/CopilotFloatingButton';
export { CopilotPanel } from './components/CopilotPanel';
export { CopilotHeader } from './components/CopilotHeader';
export { CopilotMessageBubble } from './components/CopilotMessageBubble';
export { CopilotStreamingRenderer } from './components/CopilotStreamingRenderer';
export { CopilotSuggestionChips } from './components/CopilotSuggestionChips';
export { CopilotQuickActions } from './components/CopilotQuickActions';
export { CopilotContextPanel } from './components/CopilotContextPanel';
export { CopilotMemoryTimeline } from './components/CopilotMemoryTimeline';
export { CopilotToolExecutionCard } from './components/CopilotToolExecutionCard';
export { CopilotKnowledgeReferences } from './components/CopilotKnowledgeReferences';

// Hooks
export { useCopilot } from './hooks/useCopilot';
export { useCopilotStream } from './hooks/useCopilotStream';

// Styles
import './styles/copilot.css';
