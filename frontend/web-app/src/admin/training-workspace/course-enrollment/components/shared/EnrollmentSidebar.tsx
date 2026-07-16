import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { ENROLLMENT_SECTIONS, PANEL_LABELS, type EnrollmentSection } from '../../data/enrollmentMockData';

const SECTION_ICONS: Record<EnrollmentSection, string> = {
  overview: '\uD83D\uDCCA',
  pricing: '\uD83D\uDCB0',
  enrollment: '\uD83D\uDCDD',
  capacity: '\uD83D\uDCBA',
  policies: '\uD83D\uDCCB',
  eligibility: '\u2705',
  lifecycle: '\uD83D\uDD04',
  waitlist: '\u23F3',
  payment: '\uD83D\uDCB3',
};

export function EnrollmentSidebar() {
  const { state, setSection } = useEnrollmentContext();

  return (
    <nav
      style={{
        width: 220,
        minWidth: 220,
        borderRight: '1px solid var(--color-border-default)',
        overflowY: 'auto',
        padding: 'var(--space-2)',
        background: 'var(--color-bg-secondary)',
      }}
      aria-label="Pricing & Enrollment Navigation"
    >
      <Stack gap="xs">
        {ENROLLMENT_SECTIONS.map((section) => {
          const isActive = state.section === section;
          return (
            <button
              key={section}
              onClick={() => setSection(section)}
              role="tab"
              aria-selected={isActive}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 8,
                padding: '8px 12px',
                border: 'none',
                borderRadius: 'var(--radius-sm)',
                background: isActive ? 'var(--color-primary-100)' : 'transparent',
                color: isActive ? 'var(--color-primary-700)' : 'var(--color-text-primary)',
                cursor: 'pointer',
                fontSize: 'var(--font-size-sm)',
                fontWeight: isActive ? 600 : 400,
                textAlign: 'left',
                width: '100%',
              }}
              onMouseEnter={(e) => { if (!isActive) e.currentTarget.style.background = 'var(--color-bg-hover)'; }}
              onMouseLeave={(e) => { if (!isActive) e.currentTarget.style.background = 'transparent'; }}
            >
              <span style={{ fontSize: 16, width: 20, textAlign: 'center' }}>{SECTION_ICONS[section]}</span>
              <span style={{ flex: 1 }}>{PANEL_LABELS[section]}</span>
              {section === 'payment' && <Badge variant="warning" size="sm">soon</Badge>}
            </button>
          );
        })}
      </Stack>
    </nav>
  );
}
