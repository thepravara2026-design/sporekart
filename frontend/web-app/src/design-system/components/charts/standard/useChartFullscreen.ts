import { useCallback } from 'react';

export function useChartFullscreen() {
  const toggleFullscreen = useCallback((element: HTMLElement | null) => {
    if (!element) return;
    if (document.fullscreenElement) {
      document.exitFullscreen();
    } else {
      element.requestFullscreen();
    }
  }, []);

  return { toggleFullscreen };
}
