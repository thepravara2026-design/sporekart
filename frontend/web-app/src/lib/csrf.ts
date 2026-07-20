import { useEnv } from '../config/env';
import { logger } from './logger';

interface CsrfState {
  token: string | null;
  fetched: boolean;
  fetching: Promise<string | null> | null;
}

let state: CsrfState = { token: null, fetched: false, fetching: null };

function generateMockToken(): string {
  const bytes = new Uint8Array(32);
  crypto.getRandomValues(bytes);
  return Array.from(bytes)
    .map((b) => b.toString(16).padStart(2, '0'))
    .join('');
}

export async function fetchCsrfToken(): Promise<string | null> {
  const env = useEnv();
  const isMock = env.featureFlags.mockMode;

  if (isMock) {
    const mockToken = generateMockToken();
    state = { token: mockToken, fetched: true, fetching: null };
    return mockToken;
  }

  if (state.fetching) return state.fetching;

  state.fetching = (async () => {
    try {
      const res = await fetch(`${env.apiBaseUrl}/csrf`, {
        credentials: 'include',
      });
      if (!res.ok) throw new Error(`CSRF fetch failed: ${res.status}`);
      const data = await res.json();
      const token: string = data.token ?? data.csrf_token ?? '';
      if (!token) throw new Error('CSRF endpoint returned empty token');
      state = { token, fetched: true, fetching: null };
      logger.info('[csrf] Token acquired');
      return token;
    } catch (err) {
      logger.error('[csrf] Failed to fetch token', err);
      state = { token: null, fetched: true, fetching: null };
      return null;
    }
  })();

  return state.fetching;
}

export function getCsrfToken(): string | null {
  return state.token;
}

export function resetCsrf(): void {
  state = { token: null, fetched: false, fetching: null };
}

export function csrfHeader(): Record<string, string> {
  const token = getCsrfToken();
  return token ? { 'X-CSRF-Token': token } : {};
}

export async function ensureCsrfToken(): Promise<string | null> {
  if (state.token) return state.token;
  return fetchCsrfToken();
}
