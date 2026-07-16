import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Button } from '../../../../../design-system/components/core/Button';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import {
  PRICING_TYPE_OPTIONS,
  REGISTRATION_STATUS_OPTIONS,
  DELIVERY_MODE_OPTIONS,
  LANGUAGE_OPTIONS,
  AVAILABILITY_OPTIONS,
} from '../../data/enrollmentMockData';

interface CommerceToolbarProps {
  showStatus?: boolean;
  showAvailability?: boolean;
}

export function CommerceToolbar({ showStatus = true, showAvailability = true }: CommerceToolbarProps) {
  const { state, setSearch, setFilters, resetFilters } = useEnrollmentContext();
  const { filters } = state;

  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, alignItems: 'end', marginBottom: 'var(--space-3)' }}>
      <div style={{ flex: '1 1 220px', minWidth: 180 }}>
        <Input type="search" placeholder="Search courses, pricing, policies..." value={state.search} onChange={(e) => setSearch(e.target.value)} fullWidth aria-label="Search" />
      </div>
      <div style={{ width: 170 }}>
        <Select options={PRICING_TYPE_OPTIONS} value={filters.pricingType} onChange={(v) => setFilters({ pricingType: v })} />
      </div>
      {showStatus && (
        <div style={{ width: 180 }}>
          <Select options={REGISTRATION_STATUS_OPTIONS} value={filters.registrationStatus} onChange={(v) => setFilters({ registrationStatus: v })} />
        </div>
      )}
      <div style={{ width: 160 }}>
        <Select options={DELIVERY_MODE_OPTIONS} value={filters.deliveryMode} onChange={(v) => setFilters({ deliveryMode: v })} />
      </div>
      <div style={{ width: 150 }}>
        <Select options={LANGUAGE_OPTIONS} value={filters.language} onChange={(v) => setFilters({ language: v })} />
      </div>
      {showAvailability && (
        <div style={{ width: 160 }}>
          <Select options={AVAILABILITY_OPTIONS} value={filters.availability} onChange={(v) => setFilters({ availability: v })} />
        </div>
      )}
      <Button variant="ghost" size="md" onClick={resetFilters}>Reset</Button>
    </div>
  );
}
