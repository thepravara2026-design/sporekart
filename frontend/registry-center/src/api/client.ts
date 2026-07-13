const API_BASE = '/api/v1';

export interface Health {
  status: string;
  details?: Record<string, string>;
}

export async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(init?.headers || {}) },
    ...init
  });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(`Request failed (${res.status}): ${text}`);
  }
  if (res.status === 204) {
    return undefined as T;
  }
  return (await res.json()) as T;
}

export const api = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, body: unknown) =>
    request<T>(path, { method: 'POST', body: JSON.stringify(body) }),
  put: <T>(path: string, body: unknown) =>
    request<T>(path, { method: 'PUT', body: JSON.stringify(body) }),
  del: <T>(path: string) => request<T>(path, { method: 'DELETE' })
};

/* Provider Registry */
export interface Provider {
  id: string;
  name: string;
  type: string;
  endpoint: string;
  status: string;
  registeredAt: string;
}
export interface ProviderInput {
  name: string;
  type: string;
  endpoint: string;
}

/* Prompt Registry */
export interface Prompt {
  id: string;
  key: string;
  description: string;
  currentVersion: number;
  status: string;
}
export interface PromptVersion {
  id: string;
  promptKey: string;
  version: number;
  status: string;
  createdAt: string;
}
export interface PromptInput {
  key: string;
  description: string;
  content: string;
}

/* Knowledge Registry */
export interface KnowledgeSource {
  id: string;
  name: string;
  type: string;
  source: string;
  syncStatus: string;
  lastSyncedAt: string;
}
export interface KnowledgeInput {
  name: string;
  type: string;
  source: string;
}

/* Usage Tracking */
export interface UsagePoint {
  date: string;
  requests: number;
  cost: number;
  failures: number;
}
export interface NamedMetric {
  name: string;
  value: number;
}
export interface UsageSummary {
  daily: UsagePoint[];
  monthly: UsagePoint[];
  failureRate: number;
  topProviders: NamedMetric[];
  topModels: NamedMetric[];
}

/* Config Registry */
export interface ConfigEntry {
  id: string;
  key: string;
  value: string;
  environment: string;
  version: number;
  status: string;
}
export interface ConfigSnapshot {
  id: string;
  name: string;
  createdAt: string;
}
export interface ConfigInput {
  key: string;
  value: string;
  environment: string;
}

/* Event Catalog */
export interface EventType {
  id: string;
  name: string;
  topic: string;
  producers: string[];
  consumers: string[];
  subscriptions: number;
}
export interface EventInput {
  name: string;
  topic: string;
}

/* API Registry */
export interface ApiEntry {
  id: string;
  name: string;
  path: string;
  method: string;
  health: string;
  dependencies: string[];
}
export interface ApiInput {
  name: string;
  path: string;
  method: string;
}

/* Capability Discovery */
export interface Capability {
  id: string;
  name: string;
  category: string;
  available: boolean;
  discoveredVia: string;
}
export interface CapabilityInput {
  name: string;
  category: string;
}
