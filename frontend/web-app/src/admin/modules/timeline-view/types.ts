export type TimelineEventType = 'BUSINESS' | 'RISK' | 'ALERT' | 'PLATFORM' | 'AI' | 'WORKFLOW' | 'TRAINING' | 'INVENTORY' | 'MARKETPLACE';

export interface TimelineEvent {
  id: string;
  eventType: TimelineEventType;
  category: string;
  domain: string;
  title: string;
  description: string;
  severity: string;
  source: string;
  metadata: Record<string, unknown>;
  timestamp: string;
}
