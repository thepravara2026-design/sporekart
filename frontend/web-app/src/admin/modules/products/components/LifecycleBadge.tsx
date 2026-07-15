import React from 'react';
import { StatusBadge } from '../../../components/status';
import { lifecycleLabel, lifecycleToBadge } from '../lifecycle';
import type { ProductLifecycleState } from '../types';

interface LifecycleBadgeProps {
  state: ProductLifecycleState;
  size?: 'sm' | 'md' | 'lg';
}

export const LifecycleBadge = React.memo(function LifecycleBadge({ state, size = 'sm' }: LifecycleBadgeProps) {
  return <StatusBadge status={lifecycleLabel(state)} variant={lifecycleToBadge(state)} size={size} />;
});

export default LifecycleBadge;
