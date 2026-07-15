import React from 'react';
import { FormLayout, FormSection, FormRow } from '../../../../../design-system/components/forms';
import { Textarea, TagSelector } from '../../../../components/forms';
import ValidatedField from '../ValidatedField';
import ErrorSummary from '../ErrorSummary';
import type { ProductNature, Season, ProductWizardData } from '../types';
import type { StepProps } from './BasicInfoStep';

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

const SEASONS: Season[] = ['spring', 'summer', 'monsoon', 'autumn', 'winter', 'all'];
const NATURES: ProductNature[] = ['retail', 'wholesale', 'training', 'equipment', 'consumable'];

const ClassificationStep: React.FC<StepProps> = ({ data, errors, setField }) => {
  const d = data as ProductWizardData;
  return (
    <FormLayout>
      <FormSection title="Classification" description="Categorize the product so it surfaces in the right places. Architecture stays generic for future categories.">
        <ErrorSummary errors={errors} steps={['classification']} compact />

        <FormRow>
          <ValidatedField name="productFamily" label="Product Family" data={d} errors={errors}>
            <Textarea value={d.productFamily} onChange={(v) => setField('productFamily', v)} placeholder="e.g. Mushroom Kits" rows={1} />
          </ValidatedField>
          <ValidatedField name="productGroup" label="Product Group" data={d} errors={errors}>
            <Textarea value={d.productGroup} onChange={(v) => setField('productGroup', v)} placeholder="e.g. Gourmet" rows={1} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="mushroomType" label="Mushroom Type" data={d} errors={errors}>
            <Textarea value={d.mushroomType} onChange={(v) => setField('mushroomType', v)} placeholder="e.g. Pink Oyster" rows={1} />
          </ValidatedField>
          <ValidatedField name="growingMethod" label="Growing Method" data={d} errors={errors}>
            <Textarea value={d.growingMethod} onChange={(v) => setField('growingMethod', v)} placeholder="e.g. Substrate Bag" rows={1} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="season" label="Season" data={d} errors={errors}>
            <select style={selectStyle} value={d.season} onChange={(e) => setField('season', e.target.value as Season)} aria-label="Season">
              {SEASONS.map((s) => (
                <option key={s} value={s}>{s}</option>
              ))}
            </select>
          </ValidatedField>
          <ValidatedField name="productNature" label="Product Nature" data={d} errors={errors}>
            <select style={selectStyle} value={d.productNature} onChange={(e) => setField('productNature', e.target.value as ProductNature)} aria-label="Product Nature">
              {NATURES.map((n) => (
                <option key={n} value={n}>{n}</option>
              ))}
            </select>
          </ValidatedField>
        </FormRow>

        <ValidatedField name="attributes" label="Attributes" data={d} errors={errors} helperText="Free-form attributes such as color, strain, or size. Generic extension point for future categories.">
          <TagSelector
            tags={d.attributes}
            onChange={(tags) => setField('attributes', tags)}
            placeholder="Add attribute + Enter"
            suggestions={['heirloom', 'organic', 'high-yield', 'medicinal']}
          />
        </ValidatedField>
      </FormSection>
    </FormLayout>
  );
};

export default React.memo(ClassificationStep);
