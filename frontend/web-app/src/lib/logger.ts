import { captureError, captureMessage } from './sentry';
import { getCorrelationId } from './correlationId';

type LogLevel = 'debug' | 'info' | 'warn' | 'error';

const LOG_LEVELS: Record<LogLevel, number> = {
  debug: 0,
  info: 1,
  warn: 2,
  error: 3,
};

function getConfiguredLevel(): LogLevel {
  try {
    const level = (import.meta as any).env?.VITE_LOG_LEVEL;
    if (level && level in LOG_LEVELS) return level as LogLevel;
  } catch {}
  return 'info';
}

function shouldLog(level: LogLevel): boolean {
  return LOG_LEVELS[level] >= LOG_LEVELS[getConfiguredLevel()];
}

function formatMessage(level: LogLevel, message: string, data?: Record<string, unknown>): string {
  const cid = getCorrelationId();
  const ts = new Date().toISOString();
  const dataStr = data ? ` ${JSON.stringify(data)}` : '';
  return `[${ts}] [${level.toUpperCase()}] [cid:${cid}] ${message}${dataStr}`;
}

export const logger = {
  debug(message: string, data?: Record<string, unknown>): void {
    if (!shouldLog('debug')) return;
    console.debug(formatMessage('debug', message, data));
  },

  info(message: string, data?: Record<string, unknown>): void {
    if (!shouldLog('info')) return;
    console.info(formatMessage('info', message, data));
  },

  warn(message: string, data?: Record<string, unknown>): void {
    if (!shouldLog('warn')) return;
    console.warn(formatMessage('warn', message, data));
    captureMessage(`[WARN] ${message}`, 'warning');
  },

  error(message: string, error?: unknown, data?: Record<string, unknown>): void {
    if (!shouldLog('error')) return;
    console.error(formatMessage('error', message, data), error);
    captureError(error ?? new Error(message), { message, ...data });
  },
};
