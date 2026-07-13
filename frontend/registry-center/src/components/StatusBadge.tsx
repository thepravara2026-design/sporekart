interface StatusBadgeProps {
  status: string;
}

function classify(status: string): string {
  const s = status.toLowerCase();
  if (s.includes('ok') || s.includes('active') || s.includes('healthy') || s === 'up') return 'ok';
  if (s.includes('error') || s.includes('fail') || s.includes('down') || s === 'bad') return 'bad';
  if (s.includes('warn') || s.includes('pending') || s.includes('syncing') || s.includes('degraded'))
    return 'warn';
  return 'neutral';
}

export default function StatusBadge({ status }: StatusBadgeProps) {
  const cls = classify(status);
  return <span className={`badge badge-${cls}`}>{status}</span>;
}
