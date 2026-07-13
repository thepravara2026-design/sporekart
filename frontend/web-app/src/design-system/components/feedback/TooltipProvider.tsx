import React, { createContext, useContext, useCallback, useRef, useState } from 'react';

export interface TooltipContextValue {
  registerTooltip: (id: string) => void;
  unregisterTooltip: (id: string) => void;
  showTooltip: (id: string) => void;
  hideTooltip: (id: string) => void;
  visibleTooltip: string | null;
}

const TooltipContext = createContext<TooltipContextValue | null>(null);

export const useTooltipContext = () => {
  const ctx = useContext(TooltipContext);
  if (!ctx) {
    throw new Error('useTooltipContext must be used within a TooltipProvider');
  }
  return ctx;
};

export interface TooltipProviderProps {
  children: React.ReactNode;
}

export const TooltipProvider: React.FC<TooltipProviderProps> = ({ children }) => {
  const [visibleTooltip, setVisibleTooltip] = useState<string | null>(null);
  const tooltipRegistry = useRef<Set<string>>(new Set());

  const registerTooltip = useCallback((id: string) => {
    tooltipRegistry.current.add(id);
  }, []);

  const unregisterTooltip = useCallback((id: string) => {
    tooltipRegistry.current.delete(id);
    setVisibleTooltip((prev) => (prev === id ? null : prev));
  }, []);

  const showTooltip = useCallback((id: string) => {
    setVisibleTooltip(id);
  }, []);

  const hideTooltip = useCallback((id: string) => {
    setVisibleTooltip((prev) => (prev === id ? null : prev));
  }, []);

  const value: TooltipContextValue = {
    registerTooltip,
    unregisterTooltip,
    showTooltip,
    hideTooltip,
    visibleTooltip,
  };

  return (
    <TooltipContext.Provider value={value}>
      {children}
    </TooltipContext.Provider>
  );
};

TooltipProvider.displayName = 'TooltipProvider';
export default TooltipProvider;
