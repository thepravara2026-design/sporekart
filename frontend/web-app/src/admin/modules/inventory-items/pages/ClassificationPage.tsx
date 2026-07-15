import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';
import { CLASSIFICATION_TYPES, CLASSIFICATION_GRADES } from '../constants';

export const ClassificationPage = memo(function ClassificationPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Classification Framework" description="Assign and manage classification types and grades for inventory items. Classification drives reporting, analytics, and operational workflows." />

      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Classification Types</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {CLASSIFICATION_TYPES.map((c) => (
            <div key={c.value} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 10 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Icon name="bookmark" size={18} />
                </div>
                <h4 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{c.label}</h4>
              </div>
              <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{c.description}</p>
            </div>
          ))}
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Classification Grades</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {CLASSIFICATION_GRADES.map((g) => (
            <div key={g.value} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 10 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: g.value === 'a_plus' ? 'var(--color-success-alpha)' : g.value === 'a' ? 'var(--color-info-alpha)' : 'var(--color-neutral-alpha)', color: g.value === 'a_plus' ? 'var(--color-success)' : g.value === 'a' ? 'var(--color-info)' : 'var(--color-neutral)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Icon name={g.value === 'unclassified' ? 'help-circle' : 'award'} size={18} />
                </div>
                <h4 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{g.label}</h4>
              </div>
              <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{g.description}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
