import React, { createContext, useContext, useState, useCallback, useMemo } from 'react';
import { generateId } from './useFeedbackHandlers';
import { ToastContainer } from './ToastContainer';
import type { ToastOptions, ToastItem } from './Toast';
import type { ToastPosition } from './ToastContainer';

export interface ToastQueueProps {
  maxVisible?: number;
  maxQueueSize?: number;
  defaultDuration?: number;
  pauseOnHover?: boolean;
  position?: ToastPosition;
  children: React.ReactNode;
}

interface ToastQueueContextValue {
  enqueue: (toast: ToastOptions) => string;
  dequeue: (id: string) => void;
  clearAll: () => void;
}

const ToastQueueContext = createContext<ToastQueueContextValue | undefined>(undefined);

export function ToastQueue({
  maxVisible = 5,
  maxQueueSize = 50,
  defaultDuration = 5000,
  pauseOnHover = true,
  position = 'top-right',
  children,
}: ToastQueueProps) {
  const [queue, setQueue] = useState<ToastItem[]>([]);

  const enqueue = useCallback(
    (opts: ToastOptions) => {
      const id = generateId('toast');
      const item: ToastItem = {
        id,
        ...opts,
        duration: opts.duration ?? defaultDuration,
      };
      setQueue((prev) => {
        const next = [...prev, item];
        if (next.length > maxQueueSize) {
          return next.slice(next.length - maxQueueSize);
        }
        return next;
      });
      return id;
    },
    [defaultDuration, maxQueueSize]
  );

  const dequeue = useCallback((id: string) => {
    setQueue((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const clearAll = useCallback(() => {
    setQueue([]);
  }, []);

  const value = useMemo(
    () => ({ enqueue, dequeue, clearAll }),
    [enqueue, dequeue, clearAll]
  );

  const visibleToasts = queue.slice(0, maxVisible);

  return (
    <ToastQueueContext.Provider value={value}>
      {children}
      <ToastContainer
        toasts={visibleToasts}
        onClose={dequeue}
        position={position}
        pauseOnHover={pauseOnHover}
      />
    </ToastQueueContext.Provider>
  );
}

export function useToastQueue(): ToastQueueContextValue {
  const context = useContext(ToastQueueContext);
  if (!context) {
    throw new Error('useToastQueue must be used within a ToastQueue');
  }
  return context;
}

ToastQueue.displayName = 'ToastQueue';
