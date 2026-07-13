export interface Assistant {
  id: string;
  name: string;
  description: string;
  status: 'Active' | 'Inactive';
  usageCount: number;
  avgLatency: number;
  successRate: number;
}

export interface Intent {
  name: string;
  confidence: number;
  priority: 'Low' | 'Medium' | 'High';
  entities: { name: string; value: string }[];
}

export interface Message {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: string;
  intent?: Intent;
  taskStatus?: string;
}

export interface Task {
  id: string;
  name: string;
  description: string;
  status: 'Pending' | 'Planning' | 'Queued' | 'Executing' | 'Completed' | 'Failed';
  duration?: number;
  createdAt: string;
}

export interface Recommendation {
  id: string;
  title: string;
  description: string;
  copilotName: string;
  actionable: boolean;
}

export interface WorkflowInvocation {
  id: string;
  name: string;
  status: 'Running' | 'Completed' | 'Failed';
  triggeredBy: string;
  result?: string;
  startedAt: string;
}

export interface Conversation {
  id: string;
  title: string;
  intent: string;
  copilotUsed: string;
  messageCount: number;
  createdAt: string;
}

export interface Feedback {
  conversationId: string;
  rating: number;
  comment: string;
}

export interface ExecutionLog {
  id: string;
  action: string;
  copilot: string;
  latency: number;
  status: 'Success' | 'Failed';
  timestamp: string;
}

export interface UsageStats {
  totalConversations: number;
  totalTasks: number;
  avgResponseTime: number;
  successRate: number;
}
