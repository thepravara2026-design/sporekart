export interface ChartThemeColors {
  primary: string[];
  categorical: string[];
  semantic: { success: string; warning: string; danger: string; info: string };
  grid: string;
  text: string;
  background: string;
}

export function useChartTheme(): ChartThemeColors {
  const getCSS = (name: string) => getComputedStyle(document.documentElement).getPropertyValue(name).trim();

  const colors: ChartThemeColors = {
    primary: [
      getCSS('--color-data-viz-1') || '#2F6F4F',
      getCSS('--color-data-viz-2') || '#1565C0',
      getCSS('--color-data-viz-3') || '#F57F17',
      getCSS('--color-data-viz-4') || '#C62828',
      getCSS('--color-data-viz-5') || '#6A1B9A',
      getCSS('--color-data-viz-6') || '#00838F',
      getCSS('--color-data-viz-7') || '#E65100',
      getCSS('--color-data-viz-8') || '#33691E',
    ],
    categorical: [
      getCSS('--color-data-viz-1') || '#2F6F4F',
      getCSS('--color-data-viz-2') || '#1565C0',
      getCSS('--color-data-viz-3') || '#F57F17',
      getCSS('--color-data-viz-4') || '#C62828',
      getCSS('--color-data-viz-5') || '#6A1B9A',
      getCSS('--color-data-viz-6') || '#00838F',
      getCSS('--color-data-viz-7') || '#E65100',
      getCSS('--color-data-viz-8') || '#33691E',
    ],
    semantic: {
      success: getCSS('--color-success-500') || '#4CAF50',
      warning: getCSS('--color-warning-500') || '#FFB300',
      danger: getCSS('--color-danger-500') || '#EF5350',
      info: getCSS('--color-info-500') || '#2196F3',
    },
    grid: getCSS('--color-border-default') || '#E3E6E3',
    text: getCSS('--color-text-secondary') || '#6D6D6D',
    background: getCSS('--color-bg-surface-default') || '#FFFFFF',
  };

  return colors;
}
