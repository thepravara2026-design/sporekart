export interface CopilotInfo {
  copilotId: string; name: string; type: string; version: string;
  enabled: boolean; status: string; capabilities: string[];
  activeSessions: number; avgLatencyMs: number;
}

export interface WorkspaceSession {
  sessionId: string; workspaceId: string; userId: string;
  activeCopilotId: string; status: string;
  sharedContext: Record<string, unknown>;
  startedAt: string; lastActivityAt: string;
}

export interface ChatMessage {
  id: string; role: 'user' | 'assistant'; content: string;
  copilotId?: string; copilotName?: string;
  timestamp: string; suggestions?: Suggestion[];
  collaborationResponses?: CollaborationResponse[];
  handoff?: boolean;
}

export interface CollaborationResponse {
  copilotId: string; copilotName: string; message: string; status: string;
}

export interface Suggestion { label: string; action: string; payload?: Record<string, unknown>; }

export interface ContextSnapshot {
  userContext: Record<string, unknown>;
  conversationContext: Record<string, unknown>;
  businessContext: Record<string, unknown>;
  knowledgeContext: Record<string, unknown>;
}

export interface HandoffRequest {
  fromCopilotId: string; toCopilotId: string; reason: string; contextSummary: string;
}

export interface TimelineEvent {
  id: string;
  type: 'message' | 'handoff' | 'collaboration' | 'session' | 'error';
  title: string;
  description?: string;
  copilotName?: string;
  copilotId?: string;
  timestamp: string;
}

export interface WorkspaceStatus {
  workspaceId: string; name: string; status: string;
  activeSessions: number; availableCopilots: CopilotInfo[];
}
