import React from 'react';
import type { SeoEntry, ValidationIssue } from '../types';

interface ContentValidationProps {
  entries: SeoEntry[];
}

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };

function IssueRow({ issue }: { issue: ValidationIssue }) {
  const icon = issue.severity === 'error' ? '❌' : issue.severity === 'warning' ? '⚠️' : 'ℹ️';
  const color = issue.severity === 'error' ? 'var(--color-accent-red)' : issue.severity === 'warning' ? 'var(--color-accent-orange)' : 'var(--color-accent-blue)';
  return (
    <div style={{ display: 'flex', gap: 8, padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)' }}>
      <span>{icon}</span>
      <div>
        <div style={{ color: 'var(--color-text-primary)' }}><strong>{issue.field}</strong>: {issue.message}</div>
        <div style={{ color, fontSize: 11, textTransform: 'uppercase' }}>{issue.severity}</div>
      </div>
    </div>
  );
}

export const ContentValidation: React.FC<ContentValidationProps> = React.memo(({ entries }) => {
  const allErrors = entries.flatMap((e) => e.validationErrors.map((v) => ({ ...v, product: e.productName })));
  const allWarnings = entries.flatMap((e) => e.validationWarnings.map((v) => ({ ...v, product: e.productName })));
  const totalIssues = allErrors.length + allWarnings.length;

  return (
    <div style={sect}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Content Validation</h2>
        <span style={{
          padding: '4px 12px', borderRadius: 12, fontSize: 'var(--text-body-xs)', fontWeight: 600,
          background: totalIssues > 0 ? 'var(--color-accent-orange)20' : 'var(--color-accent-green)20',
          color: totalIssues > 0 ? 'var(--color-accent-orange)' : 'var(--color-accent-green)',
        }}>
          {totalIssues} issue{totalIssues !== 1 ? 's' : ''} found
        </span>
      </div>

      {entries.length === 0 ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>No entries to validate</div>
      ) : (
        <>
          <div style={grid}>
            {entries.map((e) => {
              const entryIssues = [...e.validationErrors, ...e.validationWarnings];
              if (entryIssues.length === 0) return null;
              return (
                <div key={e.id} style={card}>
                  <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>{e.productName}</div>
                  {entryIssues.map((issue, i) => <IssueRow key={i} issue={issue} />)}
                </div>
              );
            })}
          </div>

          <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>All Issues Summary</h3>
            <div style={{ display: 'flex', gap: 24 }}>
              <div><span style={{ color: 'var(--color-accent-red)', fontWeight: 700 }}>{allErrors.length}</span> <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>errors</span></div>
              <div><span style={{ color: 'var(--color-accent-orange)', fontWeight: 700 }}>{allWarnings.length}</span> <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>warnings</span></div>
              <div><span style={{ color: 'var(--color-text-primary)', fontWeight: 700 }}>{entries.length}</span> <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>entries scanned</span></div>
            </div>
          </div>
        </>
      )}
    </div>
  );
});
