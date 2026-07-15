export type SessionState = 'active' | 'idle' | 'timeout_warning' | 'expired';

export interface SessionConfig {
  timeoutDuration: number;
  warningDuration: number;
}

export interface SessionInfo {
  state: SessionState;
  lastActivity: number;
  expiresAt: number;
  idleThreshold: number;
}
