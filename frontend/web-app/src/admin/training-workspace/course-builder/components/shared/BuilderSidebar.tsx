import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useBuilderContext } from '../../state/BuilderContext';
import { PANEL_LABELS, PANEL_ICONS, type BuilderPanel } from '../../data/builderMockData';

const PANELS: BuilderPanel[] = ['overview', 'information', 'objectives', 'prerequisites', 'media', 'resources', 'seo', 'settings', 'preview'];

function PanelIcon({ icon }: { icon: string }) {
  const iconMap: Record<string, string> = {
    info: '\u2139\uFE0F',
    edit: '\u270F\uFE0F',
    layers: '\uD83D\uDCDA',
    target: '\uD83C\uDFAF',
    'check-circle': '\u2705',
    folder: '\uD83D\uDCC1',
    image: '\uD83D\uDDBC\uFE0F',
    search: '\uD83D\uDD0D',
    eye: '\uD83D\uDC41\uFE0F',
    send: '\uD83D\uDCE4',
    settings: '\u2699\uFE0F',
  };
  return <span style={{ fontSize: 16, width: 20, textAlign: 'center' }}>{iconMap[icon] || '\u2022'}</span>;
}

export function BuilderSidebar() {
  const { state, setPanel } = useBuilderContext();

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
      aria-label="Course Builder Navigation"
    >
      <Stack gap="xs">
        {PANELS.map((panel) => {
          const isActive = state.panel === panel;
          const hasContent = panel === 'overview' || getPanelCompletion(state, panel);
          return (
            <button
              key={panel}
              onClick={() => setPanel(panel)}
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
                transition: 'background 0.15s, color 0.15s',
              }}
              onMouseEnter={(e) => {
                if (!isActive) e.currentTarget.style.background = 'var(--color-bg-hover)';
              }}
              onMouseLeave={(e) => {
                if (!isActive) e.currentTarget.style.background = 'transparent';
              }}
            >
              <PanelIcon icon={PANEL_ICONS[panel]} />
              <span>{PANEL_LABELS[panel]}</span>
              {hasContent && <Badge variant="success" size="sm" dot />}
            </button>
          );
        })}
      </Stack>
    </nav>
  );
}

function getPanelCompletion(state: any, panel: BuilderPanel): boolean {
  switch (panel) {
    case 'information':
      return !!state.info.title;
    case 'objectives':
      return state.objectives.length > 0;
    case 'prerequisites':
      return state.prerequisites.length > 0;
    case 'media':
      return state.mediaPlaceholders.length > 0;
    case 'resources':
      return state.resources.length > 0;
    case 'seo':
      return !!state.seo.seoTitle;
    case 'settings':
      return !!state.settings.visibility;
    default:
      return false;
  }
}
