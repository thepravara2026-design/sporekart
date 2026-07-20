import { getAccessToken } from './supabase';
import { getCorrelationId, resetCorrelationId } from './correlationId';
import { logger } from './logger';
import { useEnv } from '../config/env';
import { ensureCsrfToken, csrfHeader } from './csrf';

interface RequestOptions {
  method?: string;
  headers?: Record<string, string>;
  body?: unknown;
  params?: Record<string, string>;
  signal?: AbortSignal;
}

interface ApiResponse<T = unknown> {
  ok: boolean;
  status: number;
  data: T;
  error?: string;
}

export async function apiRequest<T = unknown>(
  path: string,
  options: RequestOptions = {},
): Promise<ApiResponse<T>> {
  const env = useEnv();
  const baseUrl = env.apiBaseUrl;
  const url = new URL(path.startsWith('http') ? path : `${baseUrl}${path}`);

  if (options.params) {
    Object.entries(options.params).forEach(([key, value]) => {
      url.searchParams.set(key, value);
    });
  }

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    'X-Correlation-ID': getCorrelationId(),
    ...options.headers,
  };

  const token = await getAccessToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const method = options.method ?? 'GET';
  const isMutation = ['POST', 'PUT', 'PATCH', 'DELETE'].includes(method);
  if (isMutation) {
    await ensureCsrfToken();
    Object.assign(headers, csrfHeader());
  }

  const fetchOptions: RequestInit = {
    method: options.method ?? 'GET',
    headers,
    signal: options.signal,
  };

  if (options.body) {
    fetchOptions.body = JSON.stringify(options.body);
  }

  const startTime = performance.now();

  try {
    const response = await fetch(url.toString(), fetchOptions);
    const duration = Math.round(performance.now() - startTime);

    let data: T;
    const contentType = response.headers.get('content-type');
    if (contentType?.includes('application/json')) {
      data = await response.json();
    } else {
      data = (await response.text()) as unknown as T;
    }

    logger.debug(`[http] ${fetchOptions.method ?? 'GET'} ${path} → ${response.status} (${duration}ms)`);

    if (!response.ok) {
      return {
        ok: false,
        status: response.status,
        data,
        error: typeof data === 'string' ? data : (data as any)?.error ?? response.statusText,
      };
    }

    return { ok: true, status: response.status, data };
  } catch (error) {
    const duration = Math.round(performance.now() - startTime);
    logger.error(`[http] ${fetchOptions.method ?? 'GET'} ${path} failed (${duration}ms)`, error);

    if (error instanceof DOMException && error.name === 'AbortError') {
      return { ok: false, status: 0, data: null as T, error: 'Request aborted' };
    }

    return {
      ok: false,
      status: 0,
      data: null as T,
      error: error instanceof Error ? error.message : 'Network error',
    };
  }
}

export function resetRequestChain(): void {
  resetCorrelationId();
}

export const http = {
  get: <T>(path: string, options?: RequestOptions) =>
    apiRequest<T>(path, { ...options, method: 'GET' }),
  post: <T>(path: string, body?: unknown, options?: RequestOptions) =>
    apiRequest<T>(path, { ...options, method: 'POST', body }),
  put: <T>(path: string, body?: unknown, options?: RequestOptions) =>
    apiRequest<T>(path, { ...options, method: 'PUT', body }),
  patch: <T>(path: string, body?: unknown, options?: RequestOptions) =>
    apiRequest<T>(path, { ...options, method: 'PATCH', body }),
  delete: <T>(path: string, options?: RequestOptions) =>
    apiRequest<T>(path, { ...options, method: 'DELETE' }),
};
