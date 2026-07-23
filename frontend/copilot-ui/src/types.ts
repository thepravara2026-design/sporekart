export interface CopilotMessage {
  id: string;
  role: 'user' | 'assistant' | 'system' | 'tool';
  content: string;
  metadata?: Record<string, unknown>;
  timestamp: string;
}

export interface Suggestion {
  label: string;
  action: string;
  payload?: Record<string, unknown>;
}

export interface CopilotContext {
  userId?: string;
  pageUrl?: string;
  pageTitle?: string;
  section?: string;
  entityType?: string;
  entityId?: string;
  roles?: string[];
  workspaceId?: string;
}

export interface CopilotPersona {
  name: string;
  description: string;
  role: string;
  tone: string;
}

export type CopilotStatus = 'active' | 'inactive' | 'degraded' | 'error';

export interface MemoryEntry {
  id: string;
  type: 'action' | 'observation' | 'decision' | 'context';
  summary: string;
  detail?: string;
  timestamp: string;
}

export interface KnowledgeReference {
  id: string;
  title: string;
  snippet: string;
  source: string;
  url?: string;
  relevanceScore: number;
}

export interface ToolExecution {
  id: string;
  toolName: string;
  status: 'pending' | 'running' | 'success' | 'error';
  input: Record<string, unknown>;
  output?: string;
  error?: string;
  executionTimeMs?: number;
  startedAt: string;
  completedAt?: string;
}

export interface QuickAction {
  id: string;
  label: string;
  icon: string;
  action: string;
  payload?: Record<string, unknown>;
}
