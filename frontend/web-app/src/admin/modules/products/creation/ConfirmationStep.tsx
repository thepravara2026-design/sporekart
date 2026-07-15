import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status';
import type { SubmittedProduct } from './types';

export interface ConfirmationStepProps {
  product: SubmittedProduct | null;
  onCreateAnother: () => void;
  onGoToCatalog: () => void;
  onPreview: () => void;
}

const ConfirmationStep: React.FC<ConfirmationStepProps> = ({
  product,
  onCreateAnother,
  onGoToCatalog,
  onPreview,
}) => {
  return (
    <Card variant="elevated" padding="lg" style={{ maxWidth: 560, margin: '0 auto', textAlign: 'center' }}>
      <div
        style={{
          width: 64,
          height: 64,
          borderRadius: 'var(--radius-full)',
          background: 'var(--color-bg-success-weak)',
          color: 'var(--color-text-success)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          margin: '0 auto var(--space-stack-md)',
        }}
      >
        <Icon name="Check" size={32} />
      </div>

      <h2 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Created (Draft)
      </h2>
      <p style={{ margin: '0 0 var(--space-stack-md)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>
        Your product was saved as a draft in Mock Mode. No data was sent to a backend.
      </p>

      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
          padding: 'var(--space-stack-md)',
          background: 'var(--color-bg-surface-raised)',
          borderRadius: 'var(--radius-md)',
          marginBottom: 'var(--space-stack-md)',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
          <span style={{ color: 'var(--color-text-secondary)' }}>Product ID</span>
          <span style={{ fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{product?.id}</span>
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
          <span style={{ color: 'var(--color-text-secondary)' }}>Name</span>
          <span style={{ fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{product?.name}</span>
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
          <span style={{ color: 'var(--color-text-secondary)' }}>SKU</span>
          <span style={{ fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{product?.sku}</span>
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', alignItems: 'center' }}>
          <span style={{ color: 'var(--color-text-secondary)' }}>Status</span>
          <StatusBadge status="Draft" variant="neutral" />
        </div>
      </div>

      <div style={{ display: 'flex', gap: 'var(--space-inline-sm)', justifyContent: 'center', flexWrap: 'wrap' }}>
        <Button variant="primary" onClick={onCreateAnother} leftIcon={<Icon name="Plus" size={18} />}>
          Create Another
        </Button>
        <Button variant="secondary" onClick={onGoToCatalog} leftIcon={<Icon name="Package" size={18} />}>
          Go to Catalog
        </Button>
        <Button variant="ghost" onClick={onPreview} leftIcon={<Icon name="Eye" size={18} />}>
          Preview
        </Button>
      </div>
    </Card>
  );
};

export default React.memo(ConfirmationStep);
