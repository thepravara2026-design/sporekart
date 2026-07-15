import React from 'react';
import { FormLayout, FormSection, FormRow } from '../../../../../design-system/components/forms';
import { Textarea, NumberInput, CurrencyInput } from '../../../../components/forms';
import ValidatedField from '../ValidatedField';
import ErrorSummary from '../ErrorSummary';
import type { CurrencyCode, TaxClass, ProductWizardData } from '../types';
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

const CURRENCIES: CurrencyCode[] = ['INR', 'USD', 'EUR', 'GBP'];
const TAX_CLASSES: TaxClass[] = ['standard', 'reduced', 'zero', 'exempt'];

const PricingStep: React.FC<StepProps> = ({ data, errors, setField }) => {
  const d = data as ProductWizardData;
  return (
    <FormLayout>
      <FormSection title="Pricing & Tax" description="Set selling price, cost, and tax classification. Mock only — no calculations or pricing engine.">
        <ErrorSummary errors={errors} steps={['pricing']} compact />

        <FormRow>
          <ValidatedField name="mrp" label="MRP" data={d} errors={errors}>
            <CurrencyInput value={d.mrp} onChange={(v) => setField('mrp', v)} currency={d.currency} min={0} />
          </ValidatedField>
          <ValidatedField name="price" label="Selling Price" required data={d} errors={errors}>
            <CurrencyInput value={d.price} onChange={(v) => setField('price', v)} currency={d.currency} min={0} />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="wholesalePrice" label="Wholesale Price" data={d} errors={errors} helperText="Optional. Shown to wholesale buyers.">
            <CurrencyInput value={d.wholesalePrice} onChange={(v) => setField('wholesalePrice', v)} currency={d.currency} min={0} />
          </ValidatedField>
          <ValidatedField name="discount" label="Discount %" data={d} errors={errors}>
            <NumberInput value={d.discount} onChange={(v) => setField('discount', v)} min={0} max={100} step={0.5} placeholder="0" />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="cost" label="Cost" data={d} errors={errors} helperText="Optional unit cost for margin analysis.">
            <CurrencyInput value={d.cost} onChange={(v) => setField('cost', v)} currency={d.currency} min={0} />
          </ValidatedField>
          <ValidatedField name="currency" label="Currency" data={d} errors={errors}>
            <select style={selectStyle} value={d.currency} onChange={(e) => setField('currency', e.target.value as CurrencyCode)} aria-label="Currency">
              {CURRENCIES.map((c) => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="taxClass" label="Tax Class" data={d} errors={errors}>
            <select style={selectStyle} value={d.taxClass} onChange={(e) => setField('taxClass', e.target.value as TaxClass)} aria-label="Tax Class">
              {TAX_CLASSES.map((t) => (
                <option key={t} value={t}>{t}</option>
              ))}
            </select>
          </ValidatedField>
          <ValidatedField name="gst" label="GST %" data={d} errors={errors}>
            <NumberInput value={d.gst} onChange={(v) => setField('gst', v)} min={0} max={100} step={0.5} placeholder="0" />
          </ValidatedField>
        </FormRow>

        <FormRow>
          <ValidatedField name="stockKeepingUnit" label="Stock Keeping Unit" data={d} errors={errors} helperText="Internal inventory reference (optional).">
            <Textarea value={d.stockKeepingUnit} onChange={(v) => setField('stockKeepingUnit', v)} placeholder="INV-0001" rows={1} />
          </ValidatedField>
          <ValidatedField name="hsnCode" label="HSN Code" data={d} errors={errors}>
            <Textarea value={d.hsnCode} onChange={(v) => setField('hsnCode', v)} placeholder="e.g. 1212" rows={1} />
          </ValidatedField>
        </FormRow>

        <ValidatedField name="priceNotes" label="Price Notes" data={d} errors={errors} helperText="Future pricing engine placeholder.">
          <Textarea value={d.priceNotes} onChange={(v) => setField('priceNotes', v)} placeholder="Promo rules, region overrides, etc." rows={2} />
        </ValidatedField>
      </FormSection>
    </FormLayout>
  );
};

export default React.memo(PricingStep);
