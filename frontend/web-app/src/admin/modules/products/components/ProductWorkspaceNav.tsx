import React from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

export interface ProductWorkspaceSection {
  id: string;
  label: string;
  icon: string;
}

export const PRODUCT_WORKSPACE_SECTIONS: ProductWorkspaceSection[] = [
  { id: 'overview', label: 'Overview', icon: 'grid' },
  { id: 'products', label: 'Products', icon: 'package' },
  { id: 'categories', label: 'Categories', icon: 'tag' },
  { id: 'collections', label: 'Collections', icon: 'bookmark' },
  { id: 'brands', label: 'Brands', icon: 'shield' },
  { id: 'pricing', label: 'Pricing', icon: 'dollar-sign' },
  { id: 'media', label: 'Media', icon: 'image' },
  { id: 'seo', label: 'SEO', icon: 'search' },
  { id: 'publishing', label: 'Publishing', icon: 'upload' },
  { id: 'activity', label: 'Activity', icon: 'activity' },
  { id: 'settings', label: 'Settings', icon: 'settings' },
  { id: 'help', label: 'Help', icon: 'file-text' },
];

interface ProductWorkspaceNavProps {
  activeSection: string;
  onSelect: (section: string) => void;
}

export const ProductWorkspaceNav = React.memo(function ProductWorkspaceNav({ activeSection, onSelect }: ProductWorkspaceNavProps) {
  return (
    <nav aria-label="Product workspace sections" style={{ display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' }}>
      {PRODUCT_WORKSPACE_SECTIONS.map((section) => {
        const isActive = section.id === activeSection;
        return (
          <button
            key={section.id}
            type="button"
            aria-current={isActive ? 'page' : undefined}
            onClick={() => onSelect(section.id)}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-inline-xs)',
              padding: '8px 12px',
              borderRadius: 'var(--radius-sm)',
              border: 'none',
              cursor: 'pointer',
              textAlign: 'left',
              fontFamily: 'var(--font-family-sans)',
              fontSize: 'var(--text-body-sm)',
              background: isActive ? 'var(--color-primary-alpha)' : 'transparent',
              color: isActive ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: isActive ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            }}
          >
            <Icon name={section.icon} size={16} />
            <span>{section.label}</span>
          </button>
        );
      })}
    </nav>
  );
});

export default ProductWorkspaceNav;
