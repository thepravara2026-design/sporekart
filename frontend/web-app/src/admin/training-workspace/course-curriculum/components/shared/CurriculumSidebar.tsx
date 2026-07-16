import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { CURRICULUM_SECTIONS, PANEL_LABELS, type CurriculumSection } from '../../data/curriculumMockData';

const SECTION_ICONS: Record<CurriculumSection, string> = {
  overview: '\u2139\uFE0F',
  builder: '\uD83D\uDDD2\uFE0F',
  module: '\uD83D\uDCC2',
  lesson: '\uD83C\uDFAF',
  topic: '\uD83D\uDCDD',
  activities: '\uD83E\uDDE0',
  templates: '\uD83D\uDCDA',
  'learning-paths': '\uD83C\uDFDB\uFE0F',
  completion: '\uD83C\uDFC1',
  resources: '\uD83D\uDCC1',
  dragdrop: '\uD83D\uDDDC\uFE0F',
  preview: '\uD83D\uDC41\uFE0F',
};

export function CurriculumSidebar() {
  const { state, setSection } = useCurriculumContext();

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
      aria-label="Curriculum Navigation"
    >
      <Stack gap="xs">
        {CURRICULUM_SECTIONS.map((section) => {
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
              {(section === 'overview' || section === 'builder') && <Badge variant="success" size="sm" dot />}
            </button>
          );
        })}
      </Stack>
    </nav>
  );
}
