import React from 'react';
import {
  PRODUCT_LIFECYCLE_STATES,
  lifecycleToBadge,
  lifecycleLabel,
} from '../lifecycle';
import type { ProductLifecycleState } from '../types';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import StatusBadge from '../../../components/status/StatusBadge';

export interface LifecyclePanelProps {
  state: ProductLifecycleState;
  available: ProductLifecycleState[];
  canApprove: boolean;
  canArchive: boolean;
  canRestore: boolean;
  canDelete: boolean;
  onTransition: (target: ProductLifecycleState, note?: string) => void;
}

function isDraftRestorePath(state: ProductLifecycleState, target: ProductLifecycleState): boolean {
  return target === 'draft' && (state === 'archived' || state === 'deleted');
}

function isRejectPath(state: ProductLifecycleState, target: ProductLifecycleState): boolean {
  return target === 'draft' && state === 'under_review';
}

function isTransitionDisabled(
  state: ProductLifecycleState,
  target: ProductLifecycleState,
  props: LifecyclePanelProps,
): boolean {
  if (target === 'approved') return !props.canApprove;
  if (target === 'archived') return !props.canArchive;
  if (target === 'deleted') return !props.canDelete;
  if (isDraftRestorePath(state, target)) return !props.canRestore;
  if (isRejectPath(state, target)) return false;
  return false;
}

const LifecyclePanel: React.FC<LifecyclePanelProps> = (props) => {
  const { state, available, onTransition } = props;
  const current = PRODUCT_LIFECYCLE_STATES.find((s) => s.state === state);

  return (
    <Card>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-4)',
        }}
      >
        <h3
          style={{
            margin: 0,
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-lg)',
            fontWeight: 'var(--weight-semibold)',
          }}
        >
          Lifecycle
        </h3>

        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-3)',
            flexWrap: 'wrap',
          }}
        >
          <StatusBadge status={lifecycleLabel(state)} variant={lifecycleToBadge(state)} />
          {current?.description ? (
            <span
              style={{
                color: 'var(--color-text-secondary)',
                fontSize: 'var(--text-sm)',
                fontWeight: 'var(--weight-regular)',
              }}
            >
              {current.description}
            </span>
          ) : null}
        </div>

        <div
          style={{
            display: 'flex',
            flexDirection: 'column',
            gap: 'var(--space-2)',
          }}
        >
          <span
            style={{
              color: 'var(--color-text-primary)',
              fontSize: 'var(--text-sm)',
              fontWeight: 'var(--weight-medium)',
            }}
          >
            Available transitions
          </span>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)' }}>
            {available.length === 0 ? (
              <span
                style={{
                  color: 'var(--color-text-secondary)',
                  fontSize: 'var(--text-sm)',
                }}
              >
                No transitions available from this state.
              </span>
            ) : (
              available.map((target) => {
                const disabled = isTransitionDisabled(state, target, props);
                const restorePath = isDraftRestorePath(state, target);
                const rejectPath = isRejectPath(state, target);
                return (
                  <Button
                    key={target}
                    variant={rejectPath ? 'ghost' : 'secondary'}
                    disabled={disabled}
                    leftIcon={
                      restorePath ? <Icon name="RefreshCw" size={16} /> : undefined
                    }
                    onClick={() => onTransition(target)}
                  >
                    {lifecycleLabel(target)}
                  </Button>
                );
              })
            )}
          </div>
        </div>

        <p
          style={{
            margin: 0,
            color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-xs)',
          }}
        >
          Some transitions require approval/restore permissions.
        </p>
      </div>
    </Card>
  );
};

export default React.memo(LifecyclePanel);
