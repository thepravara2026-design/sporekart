import { QuickActionCard as BaseQuickActionCard } from '../../inventory/components';
import type { QuickAction } from '../types';

export function QuickActionCard({ action, onClick }: { action: QuickAction; onClick?: (action: QuickAction) => void }) {
  return <BaseQuickActionCard action={action as any} onClick={onClick as any} />;
}
