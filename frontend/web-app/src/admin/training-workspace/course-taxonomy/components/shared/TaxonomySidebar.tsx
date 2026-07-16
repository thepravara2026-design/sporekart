import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useTaxonomyContext } from '../../state/TaxonomyContext';
import { TAXONOMY_SECTIONS, PANEL_LABELS, type TaxonomySection } from '../../data/taxonomyMockData';

const SECTION_ICONS: Record<TaxonomySection, string> = {
  overview: '\u2139\uFE0F',
  categories: '\uD83D\uDCC1',
  tags: '\uD83C\uDFF7\uFE0F',
  topics: '\uD83D\uDCDD',
  skills: '\uD83C\uDFAF',
  competencies: '\uD83E\uDDE0',
  languages: '\uD83C\uDF10',
  delivery: '\uD83D\uDCE3',
  explorer: '\uD83E\uDDF0',
  relationships: '\uD83E\uDDE7',
  discovery: '\uD83D\uDD0D',
  'learning-paths': '\uD83C\uDFDB\uFE0F',
};

export function TaxonomySidebar() {
  const { state, setSection } = useTaxonomyContext();

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
      aria-label="Taxonomy Navigation"
    >
      <Stack gap="xs">
        {TAXONOMY_SECTIONS.map((section) => {
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
              {(section === 'categories' || section === 'overview') && <Badge variant="success" size="sm" dot />}
            </button>
          );
        })}
      </Stack>
    </nav>
  );
}
