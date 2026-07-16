import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { AUDIENCE_OPTIONS } from '../data/communicationOptions';
import { FUTURE_AUDIENCE_SCOPES } from '../data/communicationTypes';
import type { AudienceScope } from '../data/communicationTypes';

export interface AudienceSelectorProps {
  selected: AudienceScope[];
  onToggle: (scope: AudienceScope) => void;
  label?: string;
}

/**
 * Audience targeting selector (mock). Future scopes are shown disabled with a
 * "future" affordance to communicate the roadmap without enabling them.
 */
const AudienceSelector = memo(function AudienceSelector({
  selected,
  onToggle,
  label = 'Target audience',
}: AudienceSelectorProps) {
  return (
    <fieldset style={{ border: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
      <legend style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', padding: 0 }}>
        {label}
      </legend>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)' }}>
        {AUDIENCE_OPTIONS.map((opt) => {
          const future = FUTURE_AUDIENCE_SCOPES.includes(opt.value);
          const active = selected.includes(opt.value);
          return (
            <button
              key={opt.value}
              type="button"
              disabled={future}
              aria-pressed={active}
              onClick={() => !future && onToggle(opt.value)}
              style={{
                display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)',
                padding: '6px 12px', borderRadius: 'var(--radius-pill, 999px)',
                border: `1px solid ${active ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
                background: active ? 'var(--color-bg-primary-weak)' : 'var(--color-bg-surface-default)',
                color: future ? 'var(--color-text-disabled)' : active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-body-sm)', fontWeight: 500,
                cursor: future ? 'not-allowed' : 'pointer', opacity: future ? 0.7 : 1,
              }}
            >
              {active && <Icon name="check" size={14} />}
              {opt.label}
            </button>
          );
        })}
      </div>
    </fieldset>
  );
});

export default AudienceSelector;
