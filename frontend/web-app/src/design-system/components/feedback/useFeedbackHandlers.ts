import { useCallback, useEffect, useState } from 'react';

let idCounter = 0;

export function generateId(prefix = 'sk'): string {
  idCounter += 1;
  return `${prefix}-${idCounter}-${Math.random().toString(36).substring(2, 9)}`;
}

export function handleOverlayClick(
  e: React.MouseEvent,
  onClose: () => void
): void {
  if (e.target === e.currentTarget) {
    onClose();
  }
}

export function handleEscape(
  onClose: () => void
): (e: KeyboardEvent) => void {
  return (e: KeyboardEvent) => {
    if (e.key === 'Escape') {
      onClose();
    }
  };
}

export function useKeyboardNavigation(
  itemCount: number,
  options?: { vertical?: boolean; loop?: boolean }
) {
  const [focusedIndex, setFocusedIndex] = useState(-1);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      const isVertical = options?.vertical ?? true;
      const shouldLoop = options?.loop ?? true;

      const nextKey = isVertical ? 'ArrowDown' : 'ArrowRight';
      const prevKey = isVertical ? 'ArrowUp' : 'ArrowLeft';

      if (e.key === nextKey) {
        e.preventDefault();
        setFocusedIndex((prev) => {
          if (prev >= itemCount - 1) return shouldLoop ? 0 : itemCount - 1;
          return prev + 1;
        });
      } else if (e.key === prevKey) {
        e.preventDefault();
        setFocusedIndex((prev) => {
          if (prev <= 0) return shouldLoop ? itemCount - 1 : 0;
          return prev - 1;
        });
      }
    },
    [itemCount, options?.vertical, options?.loop]
  );

  return { focusedIndex, setFocusedIndex, handleKeyDown };
}

export function useAnnounce() {
  const announce = useCallback((message: string) => {
    const announcer = document.getElementById('sk-announcer');
    if (announcer) {
      announcer.textContent = '';
      setTimeout(() => {
        announcer.textContent = message;
      }, 50);
    }
  }, []);

  useEffect(() => {
    if (document.getElementById('sk-announcer')) return;
    const el = document.createElement('div');
    el.id = 'sk-announcer';
    el.setAttribute('role', 'status');
    el.setAttribute('aria-live', 'polite');
    el.setAttribute('aria-atomic', 'true');
    el.style.cssText =
      'position:absolute;width:1px;height:1px;padding:0;margin:-1px;overflow:hidden;clip:rect(0,0,0,0);white-space:nowrap;border:0;clip-path:inset(50%)';
    document.body.appendChild(el);
  }, []);

  return announce;
}
