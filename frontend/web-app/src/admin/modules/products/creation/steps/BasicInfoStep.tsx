import React from 'react';
import { FormLayout, FormSection, FormRow } from '../../../../../design-system/components/forms';
import { Textarea, TagSelector } from '../../../../components/forms';
import ValidatedField from '../ValidatedField';
import ErrorSummary from '../ErrorSummary';
import { checkDuplicateName } from '../validation';
import type { ProductStatus } from '../types';

export interface StepProps {
  data: import('../types').ProductWizardData;
  errors: import('../types').WizardErrors;
  setField: (name: keyof import('../types').ProductWizardData, value: unknown) => void;
}

const STATUS_OPTIONS: ProductStatus[] = ['draft', 'active', 'archived'];

const BasicInfoStep: React.FC<StepProps> = ({ data, errors, setField }) => {
  const dupWarning = !errors.name && data.name.trim() ? checkDuplicateName(data.name) : undefined;

  return (
    <FormLayout>
      <FormSection title="Basic Information" description="Core details customers and merchandisers will see.">
        <ErrorSummary errors={errors} steps={['basic']} compact />

        <FormRow>
          <ValidatedField name="name" label="Product Name" required data={data} errors={errors} characterLimit={120} warning={dupWarning}>
            <Textarea
              value={data.name}
              onChange={(v) => setField('name', v)}
              placeholder="e.g. Organic Pink Oyster Mushroom Grow Kit"
              rows={1}
            />
          </ValidatedField>

          <ValidatedField name="sku" label="SKU" required data={data} errors={errors}>
            <Textarea value={data.sku} onChange={(v) => setField('sku', v)} placeholder="SKU-0001" rows={1} />
          </ValidatedField>
        </FormRow>

        <ValidatedField name="shortDescription" label="Short Description" data={data} errors={errors} characterLimit={160}>
          <Textarea
            value={data.shortDescription}
            onChange={(v) => setField('shortDescription', v)}
            placeholder="One-line summary for cards and search results."
            rows={2}
          />
        </ValidatedField>

        <ValidatedField name="description" label="Long Description" data={data} errors={errors} characterLimit={2000}>
          <Textarea
            value={data.description}
            onChange={(v) => setField('description', v)}
            placeholder="Detailed product description, growing instructions, etc."
            rows={5}
          />
        </ValidatedField>

        <FormRow>
          <ValidatedField name="productType" label="Product Type" required data={data} errors={errors}>
            <Textarea
              value={data.productType}
              onChange={(v) => setField('productType', v)}
              placeholder="e.g. growing_kit"
              rows={1}
            />
          </ValidatedField>
          <ValidatedField name="category" label="Category" required data={data} errors={errors}>
            <Textarea value={data.category} onChange={(v) => setField('category', v)} placeholder="e.g. Grow Kits" rows={1} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="brand" label="Brand" data={data} errors={errors}>
            <Textarea value={data.brand} onChange={(v) => setField('brand', v)} placeholder="SporeKart" rows={1} />
          </ValidatedField>
          <ValidatedField name="manufacturer" label="Manufacturer" data={data} errors={errors}>
            <Textarea
              value={data.manufacturer}
              onChange={(v) => setField('manufacturer', v)}
              placeholder="Cultivation partner"
              rows={1}
            />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="barcode" label="Barcode" data={data} errors={errors}>
            <Textarea
              value={data.barcode}
              onChange={(v) => setField('barcode', v)}
              placeholder="EAN / UPC (optional)"
              rows={1}
            />
          </ValidatedField>
          <ValidatedField name="collection" label="Collection" data={data} errors={errors}>
            <TagSelector
              tags={data.collection}
              onChange={(tags) => setField('collection', tags)}
              placeholder="Add collection + Enter"
              suggestions={['Bestsellers', 'New Arrivals', 'Seasonal', 'Limited Edition']}
            />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="tags" label="Tags" data={data} errors={errors}>
            <TagSelector
              tags={data.tags}
              onChange={(tags) => setField('tags', tags)}
              placeholder="Add tag + Enter"
              suggestions={['organic', 'heirloom', 'indoorgrow', 'beginner']}
            />
          </ValidatedField>
          <ValidatedField name="status" label="Status" data={data} errors={errors}>
            <select
              style={selectStyle}
              value={data.status}
              onChange={(e) => setField('status', e.target.value as ProductStatus)}
              aria-label="Status"
            >
              {STATUS_OPTIONS.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </ValidatedField>
        </FormRow>
      </FormSection>
    </FormLayout>
  );
};

const selectStyle: React.CSSProperties = {
  width: '100%',
  padding: '10px 12px',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-primary)',
  background: 'var(--color-bg-background)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-input)',

  boxSizing: 'border-box',
};

export default React.memo(BasicInfoStep);
