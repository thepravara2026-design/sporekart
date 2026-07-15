import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { StatusBadge } from '../../../components/status';
import ErrorSummary from './ErrorSummary';
import type { ProductWizardData, WizardErrors, WizardStepId } from './types';
import {
  computeWarnings,
  computeSeoScore,
  formatCurrency,
  formatDimensions,
  formatWeight,
} from './validation';

export interface ReviewStepProps {
  data: ProductWizardData;
  errors: WizardErrors;
  onJump?: (step: WizardStepId) => void;
}

function Row({ label, value }: { label: string; value: React.ReactNode }) {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        gap: 'var(--space-inline-md)',
        padding: '6px 0',
        borderBottom: '1px solid var(--color-border-subtle)',
        fontSize: 'var(--text-body-sm)',
      }}
    >
      <span style={{ color: 'var(--color-text-secondary)' }}>{label}</span>
      <span style={{ color: 'var(--color-text-primary)', textAlign: 'right', fontWeight: 'var(--weight-medium)' }}>
        {value || <span style={{ color: 'var(--color-text-tertiary)' }}>—</span>}
      </span>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <Card variant="outlined" padding="md" style={{ marginBottom: 'var(--space-component-gap)' }}>
      <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>
        {title}
      </h3>
      {children}
    </Card>
  );
}

const ReviewStep: React.FC<ReviewStepProps> = ({ data, errors, onJump }) => {
  const warnings = computeWarnings(data);
  const errorCount = Object.keys(errors).length;
  const seoScore = computeSeoScore(data);

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)', marginBottom: 'var(--space-stack-md)' }}>
        <StatusBadge status="Review" variant="info" />
        {errorCount > 0 ? (
          <StatusBadge status={`${errorCount} error${errorCount > 1 ? 's' : ''}`} variant="danger" />
        ) : (
          <StatusBadge status="Ready to submit" variant="success" />
        )}
        {warnings.length > 0 && (
          <StatusBadge status={`${warnings.length} suggestion${warnings.length > 1 ? 's' : ''}`} variant="warning" />
        )}
        <StatusBadge status={`SEO ${seoScore}`} variant="neutral" />
      </div>

      {errorCount > 0 && (
        <ErrorSummary errors={errors} onJump={onJump} title="Please fix before submitting" />
      )}

      <Section title="Basic Information">
        <Row label="Name" value={data.name} />
        <Row label="Product Type" value={data.productType} />
        <Row label="SKU" value={data.sku} />
        <Row label="Short Description" value={data.shortDescription} />
        <Row label="Status" value={data.status} />
        <Row label="Brand" value={data.brand} />
        <Row label="Manufacturer" value={data.manufacturer} />
        <Row label="Barcode" value={data.barcode} />
        <Row label="Category" value={data.category} />
        <Row label="Collection" value={data.collection.join(', ')} />
        <Row label="Tags" value={data.tags.join(', ')} />
      </Section>

      <Section title="Classification">
        <Row label="Product Family" value={data.productFamily} />
        <Row label="Product Group" value={data.productGroup} />
        <Row label="Mushroom Type" value={data.mushroomType} />
        <Row label="Growing Method" value={data.growingMethod} />
        <Row label="Season" value={data.season} />
        <Row label="Product Nature" value={data.productNature} />
        <Row label="Attributes" value={data.attributes.join(', ')} />
      </Section>

      <Section title="Packaging">
        <Row label="Packaging Type" value={data.packagingType} />
        <Row label="Package Size" value={data.packageSize} />
        <Row label="Units Per Pack" value={data.unitsPerPack} />
        <Row label="Weight" value={formatWeight(data.weight, data.weightUnit)} />
        <Row label="Dimensions" value={formatDimensions(data.dimensions)} />
        <Row label="Package Weight" value={formatWeight(data.packageWeight, data.packageWeightUnit)} />
        <Row label="Shelf Life" value={data.shelfLife} />
        <Row label="Storage" value={data.storageConditions} />
        <Row label="Country of Origin" value={data.countryOfOrigin} />
        <Row label="Packaging Notes" value={data.packagingNotes} />
      </Section>

      <Section title="Pricing">
        <Row label="MRP" value={formatCurrency(data.mrp, data.currency)} />
        <Row label="Selling Price" value={formatCurrency(data.price, data.currency)} />
        <Row label="Wholesale Price" value={data.wholesalePrice ? formatCurrency(data.wholesalePrice, data.currency) : ''} />
        <Row label="Discount" value={data.discount ? `${data.discount}%` : ''} />
        <Row label="Cost" value={data.cost ? formatCurrency(data.cost, data.currency) : ''} />
        <Row label="Currency" value={data.currency} />
        <Row label="Tax Class" value={data.taxClass} />
        <Row label="GST %" value={data.gst ? `${data.gst}%` : ''} />
        <Row label="HSN Code" value={data.hsnCode} />
        <Row label="Stock Keeping Unit" value={data.stockKeepingUnit} />
        <Row label="Price Notes" value={data.priceNotes} />
      </Section>

      <Section title="SEO">
        <Row label="Meta Title" value={data.metaTitle} />
        <Row label="Meta Description" value={data.metaDescription} />
        <Row label="Slug" value={data.slug} />
        <Row label="Canonical URL" value={data.canonicalUrl} />
        <Row label="Open Graph Title" value={data.ogTitle} />
        <Row label="Open Graph Description" value={data.ogDescription} />
        <Row label="Keywords" value={data.keywords.join(', ')} />
        <Row label="SEO Score" value={`${seoScore}/100`} />
      </Section>

      {warnings.length > 0 && (
        <Card variant="outlined" padding="md" style={{ borderColor: 'var(--color-border-warning)' }}>
          <h3 style={{ margin: '0 0 var(--space-stack-xs)', color: 'var(--color-text-warning)', fontSize: 'var(--text-h5)' }}>
            Missing optional fields
          </h3>
          <ul style={{ margin: 0, paddingLeft: 'var(--space-inline-lg)', color: 'var(--color-text-secondary)' }}>
            {warnings.map((w) => (
              <li key={w.field} style={{ fontSize: 'var(--text-body-sm)' }}>{w.message}</li>
            ))}
          </ul>
        </Card>
      )}
    </div>
  );
};

export default React.memo(ReviewStep);
