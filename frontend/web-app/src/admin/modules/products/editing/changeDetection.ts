import type { ProductWizardData, WizardErrors } from '../creation/types';
import type { ChangedField, EditSectionId } from './types';

export const FIELD_SECTIONS: Record<keyof ProductWizardData, EditSectionId> = {
  name: 'basic',
  shortDescription: 'basic',
  description: 'basic',
  productType: 'basic',
  brand: 'basic',
  manufacturer: 'basic',
  sku: 'basic',
  barcode: 'basic',
  category: 'basic',
  collection: 'basic',
  tags: 'basic',
  status: 'basic',
  productFamily: 'classification',
  productGroup: 'classification',
  mushroomType: 'classification',
  growingMethod: 'classification',
  season: 'classification',
  productNature: 'classification',
  attributes: 'classification',
  packagingType: 'packaging',
  packageSize: 'packaging',
  unitsPerPack: 'packaging',
  weight: 'packaging',
  weightUnit: 'packaging',
  dimensions: 'packaging',
  packageWeight: 'packaging',
  packageWeightUnit: 'packaging',
  shelfLife: 'packaging',
  storageConditions: 'packaging',
  countryOfOrigin: 'packaging',
  packagingNotes: 'packaging',
  gst: 'packaging',
  hsnCode: 'packaging',
  mrp: 'pricing',
  price: 'pricing',
  wholesalePrice: 'pricing',
  discount: 'pricing',
  cost: 'pricing',
  currency: 'pricing',
  taxClass: 'pricing',
  stockKeepingUnit: 'pricing',
  priceNotes: 'pricing',
  metaTitle: 'seo',
  metaDescription: 'seo',
  slug: 'seo',
  keywords: 'seo',
  canonicalUrl: 'seo',
  ogTitle: 'seo',
  ogDescription: 'seo',
};

function valuesEqual(a: unknown, b: unknown): boolean {
  if (Array.isArray(a) && Array.isArray(b)) {
    return JSON.stringify(a) === JSON.stringify(b);
  }
  if (typeof a === 'object' && a !== null && typeof b === 'object' && b !== null) {
    return JSON.stringify(a) === JSON.stringify(b);
  }
  return a === b;
}

export function diffProducts(original: ProductWizardData, current: ProductWizardData): ChangedField[] {
  const changed: ChangedField[] = [];
  const keys = Object.keys(original) as (keyof ProductWizardData)[];
  for (const field of keys) {
    if (!valuesEqual(original[field], current[field])) {
      changed.push({
        field,
        label: field,
        section: FIELD_SECTIONS[field],
        oldValue: original[field],
        newValue: current[field],
      });
    }
  }
  return changed;
}

export function modifiedSections(changed: ChangedField[]): EditSectionId[] {
  const set = new Set<EditSectionId>();
  changed.forEach((c) => set.add(c.section));
  return Array.from(set);
}

export function formatValue(value: unknown): string {
  if (value === null || value === undefined || value === '') return '—';
  if (Array.isArray(value)) return value.length ? value.join(', ') : '—';
  if (typeof value === 'object') return JSON.stringify(value);
  return String(value);
}

// Re-export for convenience where both are needed.
export type { WizardErrors };
