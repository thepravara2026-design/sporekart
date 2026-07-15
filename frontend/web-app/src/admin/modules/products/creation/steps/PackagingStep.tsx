import React from 'react';
import { FormLayout, FormSection, FormRow } from '../../../../../design-system/components/forms';
import { Textarea, NumberInput } from '../../../../components/forms';
import ValidatedField from '../ValidatedField';
import ErrorSummary from '../ErrorSummary';
import type { DimensionUnit, PackagingType, WeightUnit, ProductWizardData } from '../types';
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

const PACKAGING_OPTIONS: PackagingType[] = ['box', 'pouch', 'bottle', 'jar', 'bag', 'tube', 'bulk', 'other'];
const WEIGHT_UNITS: WeightUnit[] = ['g', 'kg', 'lb', 'oz'];
const DIM_UNITS: DimensionUnit[] = ['cm', 'mm', 'in'];

const PackagingStep: React.FC<StepProps> = ({ data, errors, setField }) => {
  const d = data as ProductWizardData;
  return (
    <FormLayout>
      <FormSection title="Packaging & Physical Details" description="Physical attributes used for shipping, storage, and logistics.">
        <ErrorSummary errors={errors} steps={['packaging']} compact />

        <FormRow>
          <ValidatedField name="packagingType" label="Packaging Type" required data={d} errors={errors}>
            <select style={selectStyle} value={d.packagingType} onChange={(e) => setField('packagingType', e.target.value as PackagingType)} aria-label="Packaging Type">
              {PACKAGING_OPTIONS.map((opt) => (
                <option key={opt} value={opt}>{opt}</option>
              ))}
            </select>
          </ValidatedField>
          <ValidatedField name="packageSize" label="Package Size" data={d} errors={errors} helperText="e.g. 20 × 15 × 10 cm">
            <Textarea value={d.packageSize} onChange={(v) => setField('packageSize', v)} placeholder="Describe package size" rows={1} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="unitsPerPack" label="Units Per Pack" required data={d} errors={errors}>
            <NumberInput value={d.unitsPerPack} onChange={(v) => setField('unitsPerPack', v)} min={1} placeholder="1" />
          </ValidatedField>
          <ValidatedField name="weight" label="Weight" required data={d} errors={errors}>
            <NumberInput value={d.weight} onChange={(v) => setField('weight', v)} min={0} step={0.1} placeholder="0" />
          </ValidatedField>
          <ValidatedField name="weightUnit" label="Weight Unit" data={d} errors={errors}>
            <select style={selectStyle} value={d.weightUnit} onChange={(e) => setField('weightUnit', e.target.value as WeightUnit)} aria-label="Weight Unit">
              {WEIGHT_UNITS.map((u) => (
                <option key={u} value={u}>{u}</option>
              ))}
            </select>
          </ValidatedField>
        </FormRow>

        <FormSection title="Dimensions" description="Outer package dimensions.">
          <FormRow>
            <ValidatedField name="dimensions" label="Length" data={d} errors={errors}>
              <NumberInput value={d.dimensions.length} onChange={(v) => setField('dimensions', { ...d.dimensions, length: v })} min={0} step={0.1} />
            </ValidatedField>
            <ValidatedField name="dimensions" label="Width" data={d} errors={errors}>
              <NumberInput value={d.dimensions.width} onChange={(v) => setField('dimensions', { ...d.dimensions, width: v })} min={0} step={0.1} />
            </ValidatedField>
            <ValidatedField name="dimensions" label="Height" data={d} errors={errors}>
              <NumberInput value={d.dimensions.height} onChange={(v) => setField('dimensions', { ...d.dimensions, height: v })} min={0} step={0.1} />
            </ValidatedField>
            <ValidatedField name="dimensions" label="Unit" data={d} errors={errors}>
              <select style={selectStyle} value={d.dimensions.unit} onChange={(e) => setField('dimensions', { ...d.dimensions, unit: e.target.value as DimensionUnit })} aria-label="Dimension Unit">
                {DIM_UNITS.map((u) => (
                  <option key={u} value={u}>{u}</option>
                ))}
              </select>
            </ValidatedField>
          </FormRow>
        </FormSection>

        <FormRow>
          <ValidatedField name="packageWeight" label="Package Weight" data={d} errors={errors}>
            <NumberInput value={d.packageWeight} onChange={(v) => setField('packageWeight', v)} min={0} step={0.1} placeholder="0" />
          </ValidatedField>
          <ValidatedField name="packageWeightUnit" label="Package Weight Unit" data={d} errors={errors}>
            <select style={selectStyle} value={d.packageWeightUnit} onChange={(e) => setField('packageWeightUnit', e.target.value as WeightUnit)} aria-label="Package Weight Unit">
              {WEIGHT_UNITS.map((u) => (
                <option key={u} value={u}>{u}</option>
              ))}
            </select>
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="shelfLife" label="Shelf Life" data={d} errors={errors}>
            <Textarea value={d.shelfLife} onChange={(v) => setField('shelfLife', v)} placeholder="e.g. 12 months" rows={1} />
          </ValidatedField>
          <ValidatedField name="countryOfOrigin" label="Country of Origin" data={d} errors={errors}>
            <Textarea value={d.countryOfOrigin} onChange={(v) => setField('countryOfOrigin', v)} placeholder="e.g. India" rows={1} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="manufacturer" label="Manufacturer" data={d} errors={errors}>
            <Textarea value={d.manufacturer} onChange={(v) => setField('manufacturer', v)} placeholder="Cultivation partner" rows={1} />
          </ValidatedField>
          <ValidatedField name="storageConditions" label="Storage Conditions" data={d} errors={errors}>
            <Textarea value={d.storageConditions} onChange={(v) => setField('storageConditions', v)} placeholder="e.g. Cool, dry place" rows={1} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="gst" label="GST %" data={d} errors={errors}>
            <NumberInput value={d.gst} onChange={(v) => setField('gst', v)} min={0} max={100} step={0.5} placeholder="0" />
          </ValidatedField>
          <ValidatedField name="hsnCode" label="HSN Code" data={d} errors={errors}>
            <Textarea value={d.hsnCode} onChange={(v) => setField('hsnCode', v)} placeholder="e.g. 1212" rows={1} />
          </ValidatedField>
        </FormRow>

        <ValidatedField name="packagingNotes" label="Packaging Notes" data={d} errors={errors} helperText="Future logistics reference / handling notes.">
          <Textarea value={d.packagingNotes} onChange={(v) => setField('packagingNotes', v)} placeholder="Special handling, fragile, etc." rows={2} />
        </ValidatedField>
      </FormSection>
    </FormLayout>
  );
};

export default React.memo(PackagingStep);
