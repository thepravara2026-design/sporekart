let _correlationId: string | null = null;

export function generateCorrelationId(): string {
  const ts = Date.now().toString(36);
  const rand = Math.random().toString(36).substring(2, 10);
  return `${ts}-${rand}`;
}

export function setCorrelationId(id: string): void {
  _correlationId = id;
}

export function getCorrelationId(): string {
  if (!_correlationId) {
    _correlationId = generateCorrelationId();
  }
  return _correlationId;
}

export function resetCorrelationId(): void {
  _correlationId = generateCorrelationId();
}
