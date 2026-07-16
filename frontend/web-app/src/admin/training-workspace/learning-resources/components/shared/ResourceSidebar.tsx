import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useResourceContext } from '../../state/ResourceContext';
import { RESOURCE_SECTIONS, PANEL_LABELS, type ResourceSection } from '../../data/resourceMockData';

const SECTION_ICONS: Record<ResourceSection, string> = {
  overview: '\u2139\uFE0F',
  library: '\uD83D\uDDD2\uFE0F',
  collections: '\uD83D\uDCC2',
  folders: '\uD83D\uDCC1',
  favorites: '\u2B50',
  recent: '\u23F2\uFE0F',
  archived: '\uD83D\uDDD3\uFE0F',
  preview: '\uD83D\uDC41\uFE0F',
  linking: '\uD83D\uDD17',
  storage: '\u2601\uFE0F',
};

export function ResourceSidebar() {
  const { state, setSection } = useResourceContext();

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
      aria-label="Resource Library Navigation"
    >
      <Stack gap="xs">
        {RESOURCE_SECTIONS.map((section) => {
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
              {section === 'overview' && <Badge variant="success" size="sm" dot />}
            </button>
          );
        })}
      </Stack>
    </nav>
  );
}
