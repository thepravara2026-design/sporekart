import type { ProductLifecycleState } from './types';

type BadgeVariant = 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral';

export interface LifecycleStateMeta {
  state: ProductLifecycleState;
  label: string;
  variant: BadgeVariant;
  description: string;
}

export const PRODUCT_LIFECYCLE_STATES: LifecycleStateMeta[] = [
  {
    state: 'draft',
    label: 'Draft',
    variant: 'neutral',
    description: 'Product is being authored and is not yet submitted for review.',
  },
  {
    state: 'under_review',
    label: 'Under Review',
    variant: 'info',
    description: 'Product has been submitted and is awaiting editorial approval.',
  },
  {
    state: 'approved',
    label: 'Approved',
    variant: 'success',
    description: 'Product has passed review and is ready to be published.',
  },
  {
    state: 'published',
    label: 'Published',
    variant: 'success',
    description: 'Product is live and visible to customers.',
  },
  {
    state: 'scheduled',
    label: 'Scheduled',
    variant: 'info',
    description: 'Product is queued to publish at a future date.',
  },
  {
    state: 'active',
    label: 'Active',
    variant: 'success',
    description: 'Product is actively selling and in stock.',
  },
  {
    state: 'inactive',
    label: 'Inactive',
    variant: 'warning',
    description: 'Product is temporarily hidden from storefront.',
  },
  {
    state: 'archived',
    label: 'Archived',
    variant: 'neutral',
    description: 'Product is retired and no longer orderable.',
  },
  {
    state: 'deleted',
    label: 'Deleted',
    variant: 'danger',
    description: 'Product has been soft-deleted and is excluded from listings.',
  },
];

const lifecycleVariantMap: Record<ProductLifecycleState, BadgeVariant> = PRODUCT_LIFECYCLE_STATES.reduce(
  (acc, item) => {
    acc[item.state] = item.variant;
    return acc;
  },
  {} as Record<ProductLifecycleState, BadgeVariant>,
);

export function lifecycleToBadge(state: ProductLifecycleState): BadgeVariant {
  return lifecycleVariantMap[state] ?? 'neutral';
}

export function lifecycleLabel(state: ProductLifecycleState): string {
  return PRODUCT_LIFECYCLE_STATES.find((item) => item.state === state)?.label ?? state;
}
