import React from 'react';
import Card from '../../../../design-system/components/composite/Card';
import Icon from '../../../../design-system/icons/Icon';
import StatusBadge from '../../../components/status/StatusBadge';
import type { ProductWizardData, WizardErrors } from '../creation/types';
import { computeWarnings, computeSeoScore, FIELD_LABELS } from '../creation/validation';
import type { ChangedField, EditSectionId } from './types';

export interface ReviewPanelProps {
  data: ProductWizardData;
  errors: WizardErrors;
  changed: ChangedField[];
  modifiedSections: EditSectionId[];
}

interface Readiness {
  label: string;
  score: number;
  ok: boolean;
}

function ReadinessRow({ readiness }: { readiness: Readiness }) {
  const variant = readiness.score >= 80 ? 'success' : readiness.score >= 50 ? 'warning' : 'danger';
  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 'var(--space-3)',
        padding: 'var(--space-2) 0',
        borderBottom: 'var(--border-width-thin) solid var(--color-border)',
      }}
    >
      <span style={{ color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)' }}>
        {readiness.label}
      </span>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
        <div
          role="progressbar"
          aria-valuenow={readiness.score}
          aria-valuemin={0}
          aria-valuemax={100}
          style={{
            width: 120,
            height: 8,
            borderRadius: 'var(--radius-full)',
            background: 'var(--color-bg-surface-raised)',
            overflow: 'hidden',
          }}
        >
          <div
            style={{
              width: `${readiness.score}%`,
              height: '100%',
              background:
                readiness.score >= 80
                  ? 'var(--color-success)'
                  : readiness.score >= 50
                    ? 'var(--color-warning)'
                    : 'var(--color-danger)',
            }}
          />
        </div>
        <StatusBadge status={`${readiness.score}%`} variant={variant} size="sm" />
      </div>
    </div>
  );
}

const ReviewPanelBase: React.FC<ReviewPanelProps> = ({ data, errors, changed, modifiedSections }) => {
  const warnings = React.useMemo(() => computeWarnings(data), [data]);
  const seoScore = React.useMemo(() => computeSeoScore(data), [data]);

  const errorCount = Object.keys(errors).length;
  const warningCount = warnings.length;

  const packagingReady = ['packagingType', 'packageSize', 'unitsPerPack', 'weight', 'countryOfOrigin'].every(
    (f) => hasValueField(data, f as keyof ProductWizardData),
  );
  const publishingReady =
    data.status !== undefined && ['active', 'archived', 'draft'].includes(data.status);

  const readinessList: Readiness[] = [
    { label: 'SEO Readiness', score: seoScore, ok: seoScore >= 80 },
    { label: 'Packaging Readiness', score: packagingReady ? 100 : 45, ok: packagingReady },
    { label: 'Publishing Readiness', score: publishingReady ? 100 : 40, ok: publishingReady },
    { label: 'Validation Health', score: errorCount === 0 ? 100 : Math.max(20, 100 - errorCount * 20), ok: errorCount === 0 },
  ];

  const completeness = Math.min(
    100,
    Math.round((readinessList.reduce((s, r) => s + r.score, 0) / readinessList.length) * 0.75 + (changed.length > 0 ? 25 : 0)),
  );

  return (
    <Card variant="outlined" padding="lg" aria-label="Product review and readiness">
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', marginBottom: 'var(--space-4)' }}>
        <Icon name="CheckCircle" size={18} />
        <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
          Review & Readiness
        </h3>
      </div>

      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 'var(--space-4)',
          marginBottom: 'var(--space-4)',
          padding: 'var(--space-3)',
          borderRadius: 'var(--radius-md)',
          background: 'var(--color-bg-surface-raised)',
        }}
      >
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <span style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', color: 'var(--color-primary)' }}>{completeness}</span>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Completeness</span>
        </div>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)' }}>
          <StatusBadge status={`${errorCount} errors`} variant={errorCount === 0 ? 'success' : 'danger'} size="sm" />
          <StatusBadge status={`${warningCount} warnings`} variant={warningCount === 0 ? 'success' : 'warning'} size="sm" />
          <StatusBadge status={`${changed.length} changed`} variant={changed.length === 0 ? 'neutral' : 'info'} size="sm" />
        </div>
      </div>

      <h4 style={{ margin: '0 0 var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
        Readiness
      </h4>
      {readinessList.map((r) => (
        <ReadinessRow key={r.label} readiness={r} />
      ))}

      <h4 style={{ margin: 'var(--space-4) 0 var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
        Validation Summary
      </h4>
      {errorCount === 0 ? (
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          No blocking validation errors. The product passes all required-field checks.
        </p>
      ) : (
        <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 'var(--space-1)' }}>
          {Object.entries(errors).map(([field, message]) => (
            <li key={field} style={{ display: 'flex', gap: 'var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
              <Icon name="AlertTriangle" size={16} style={{ color: 'var(--color-danger)', flexShrink: 0, marginTop: 2 }} />
              <span>
                <strong>{FIELD_LABELS[field as keyof ProductWizardData] ?? field}:</strong> {message}
              </span>
            </li>
          ))}
        </ul>
      )}

      <h4 style={{ margin: 'var(--space-4) 0 var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
        Missing / Recommended
      </h4>
      {warningCount === 0 ? (
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          All optional fields are populated. Great job.
        </p>
      ) : (
        <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 'var(--space-1)' }}>
          {warnings.slice(0, 8).map((w) => (
            <li key={w.field} style={{ display: 'flex', gap: 'var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
              <Icon name="Info" size={16} style={{ color: 'var(--color-warning)', flexShrink: 0, marginTop: 2 }} />
              <span>{w.message}</span>
            </li>
          ))}
          {warnings.length > 8 && (
            <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>+ {warnings.length - 8} more</li>
          )}
        </ul>
      )}

      {modifiedSections.length > 0 && (
        <>
          <h4 style={{ margin: 'var(--space-4) 0 var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
            Modified Sections
          </h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)' }}>
            {modifiedSections.map((s) => (
              <StatusBadge key={s} status={s} variant="info" size="sm" />
            ))}
          </div>
        </>
      )}
    </Card>
  );
};

function hasValueField(data: ProductWizardData, field: keyof ProductWizardData): boolean {
  const value = data[field];
  if (value === null || value === undefined) return false;
  if (typeof value === 'string') return value.trim().length > 0;
  if (Array.isArray(value)) return value.length > 0;
  return true;
}

export const ReviewPanel = React.memo(ReviewPanelBase);
export default ReviewPanel;
