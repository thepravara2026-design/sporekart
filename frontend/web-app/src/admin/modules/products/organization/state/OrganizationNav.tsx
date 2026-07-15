import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { OrgSectionId } from '../types';
import { ORG_SECTION_LABELS, ORG_SECTION_ICONS } from '../types';

const SECTIONS: OrgSectionId[] = ['overview', 'categories', 'collections', 'brands', 'tags', 'hierarchy', 'assignments', 'bulk', 'analytics', 'settings', 'help'];

interface OrganizationNavProps {
  activeSection: OrgSectionId;
  onSelect: (section: OrgSectionId) => void;
}

export const OrganizationNav = React.memo(function OrganizationNav({ activeSection, onSelect }: OrganizationNavProps) {
  return (
    <nav aria-label="Organization sections" style={{ display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' }}>
      {SECTIONS.map((id) => {
        const isActive = id === activeSection;
        return (
          <button
            key={id}
            type="button"
            aria-current={isActive ? 'page' : undefined}
            onClick={() => onSelect(id)}
            style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
              padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
              textAlign: 'left', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-body-sm)', width: '100%',
              background: isActive ? 'var(--color-primary-alpha)' : 'transparent',
              color: isActive ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: isActive ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            }}
          >
            <Icon name={ORG_SECTION_ICONS[id]} size={16} />
            <span>{ORG_SECTION_LABELS[id]}</span>
          </button>
        );
      })}
    </nav>
  );
});

export default OrganizationNav;
