import React from 'react';
import type { ProductWizardData } from '../creation/types';
import { FIELD_LABELS, formatCurrency } from '../creation/validation';
import { FIELD_SECTIONS, formatValue } from './changeDetection';
import type { EditSectionId } from './types';
import Card from '../../../../design-system/components/composite/Card';

export interface CompareViewProps {
  current: ProductWizardData;
  compare: ProductWizardData;
  compareLabel: string;
}

const SECTION_ORDER: EditSectionId[] = ['basic', 'classification', 'packaging', 'pricing', 'seo'];

const SECTION_LABELS: Record<EditSectionId, string> = {
  overview: 'Overview',
  basic: 'Basic Info',
  classification: 'Classification',
  packaging: 'Packaging & Physical',
  pricing: 'Pricing',
  seo: 'SEO',
  publishing: 'Publishing',
  activity: 'Activity',
  history: 'History',
  settings: 'Settings',
};

const PRICING_FIELDS = new Set<keyof ProductWizardData>(['mrp', 'price', 'wholesalePrice', 'cost']);

function formatField(field: keyof ProductWizardData, data: ProductWizardData): string {
  if (PRICING_FIELDS.has(field)) {
    const value = data[field];
    if (typeof value === 'number') {
      return formatCurrency(value, data.currency);
    }
  }
  return formatValue(data[field]);
}

const CompareViewBase: React.FC<CompareViewProps> = ({ current, compare, compareLabel }) => {
  const fields = React.useMemo(
    () => Object.keys(current) as (keyof ProductWizardData)[],
    [current],
  );

  const grouped = React.useMemo(() => {
    const map = new Map<EditSectionId, (keyof ProductWizardData)[]>();
    for (const field of fields) {
      const section = FIELD_SECTIONS[field];
      if (!map.has(section)) map.set(section, []);
      map.get(section)!.push(field);
    }
    return map;
  }, [fields]);

  const modifiedCount = React.useMemo(() => {
    let count = 0;
    for (const field of fields) {
      if (formatValue(current[field]) !== formatValue(compare[field])) count += 1;
    }
    return count;
  }, [fields, current, compare]);

  return (
    <section aria-label="Compare Versions">
      <div
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          alignItems: 'center',
          gap: 'var(--space-3)',
          marginBottom: 'var(--space-3)',
        }}
      >
        <h2
          style={{
            fontSize: 'var(--text-heading-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: 0,
          }}
        >
          Comparison: Current vs {compareLabel}
        </h2>
        <span
          style={{
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
          }}
        >
          Added 0 · Removed 0 · Modified {modifiedCount}
        </span>
      </div>

      <div
        style={{
          display: 'flex',
          gap: 'var(--space-4)',
          marginBottom: 'var(--space-3)',
          fontSize: 'var(--text-body-sm)',
        }}
      >
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2)' }}>
          <span
            style={{
              display: 'inline-block',
              width: 12,
              height: 12,
              borderRadius: 3,
              background: 'var(--color-bg-warning-weak)',
            }}
          />
          Modified
        </span>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2)' }}>
          <span
            style={{
              display: 'inline-block',
              width: 12,
              height: 12,
              borderRadius: 3,
              background: 'transparent',
              border: 'var(--border-width-thin) solid var(--color-border)',
            }}
          />
          Same
        </span>
      </div>

      <Card variant="ghost" padding="none">
        <div
          style={{
            overflowX: 'auto',
          }}
        >
          <table
            style={{
              width: '100%',
              borderCollapse: 'collapse',
              fontSize: 'var(--text-body-sm)',
            }}
          >
            <caption style={{ textAlign: 'left', padding: 'var(--space-2) var(--space-4)', color: 'var(--color-text-secondary)' }}>
              Comparison: Current vs {compareLabel}
            </caption>
            <thead>
              <tr>
                <th
                  scope="col"
                  style={{
                    textAlign: 'left',
                    padding: 'var(--space-3) var(--space-4)',
                    color: 'var(--color-text-secondary)',
                    borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                  }}
                >
                  Field
                </th>
                <th
                  scope="col"
                  style={{
                    textAlign: 'left',
                    padding: 'var(--space-3) var(--space-4)',
                    color: 'var(--color-text-secondary)',
                    borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                  }}
                >
                  Current
                </th>
                <th
                  scope="col"
                  style={{
                    textAlign: 'left',
                    padding: 'var(--space-3) var(--space-4)',
                    color: 'var(--color-text-secondary)',
                    borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                  }}
                >
                  {compareLabel}
                </th>
              </tr>
            </thead>
            <tbody>
              {SECTION_ORDER.map((section) => {
                const sectionFields = grouped.get(section);
                if (!sectionFields || sectionFields.length === 0) return null;
                return (
                  <React.Fragment key={section}>
                    <tr>
                      <th
                        scope="colgroup"
                        colSpan={3}
                        style={{
                          textAlign: 'left',
                          padding: 'var(--space-3) var(--space-4)',
                          background: 'var(--color-bg-surface-raised)',
                          color: 'var(--color-text-primary)',
                          fontWeight: 'var(--weight-semibold)',
                          borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                        }}
                      >
                        {SECTION_LABELS[section]}
                      </th>
                    </tr>
                    {sectionFields.map((field) => {
                      const currentVal = formatField(field, current);
                      const compareVal = formatField(field, compare);
                      const isModified = currentVal !== compareVal;
                      return (
                        <tr key={field}>
                          <th
                            scope="row"
                            style={{
                              textAlign: 'left',
                              fontWeight: 'var(--weight-regular)',
                              padding: 'var(--space-2) var(--space-4)',
                              color: 'var(--color-text-secondary)',
                              borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                              width: '30%',
                            }}
                          >
                            {FIELD_LABELS[field] ?? field}
                          </th>
                          <td
                            style={{
                              padding: 'var(--space-2) var(--space-4)',
                              color: 'var(--color-text-primary)',
                              borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                              verticalAlign: 'top',
                            }}
                          >
                            {currentVal}
                          </td>
                          <td
                            style={{
                              padding: 'var(--space-2) var(--space-4)',
                              color: 'var(--color-text-primary)',
                              borderBottom: 'var(--border-width-thin) solid var(--color-border)',
                              verticalAlign: 'top',
                              background: isModified ? 'var(--color-bg-warning-weak)' : 'transparent',
                            }}
                          >
                            {compareVal}
                            {isModified && (
                              <span
                                style={{
                                  marginLeft: 'var(--space-2)',
                                  fontSize: 'var(--text-caption)',
                                  color: 'var(--color-text-warning)',
                                }}
                              >
                                Modified
                              </span>
                            )}
                          </td>
                        </tr>
                      );
                    })}
                  </React.Fragment>
                );
              })}
            </tbody>
          </table>
        </div>
      </Card>
    </section>
  );
};

export const CompareView = React.memo(CompareViewBase);
export default CompareView;
