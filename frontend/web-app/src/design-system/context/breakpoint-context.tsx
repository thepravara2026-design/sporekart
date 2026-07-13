import React, { createContext, useContext, useEffect, useState } from 'react';

export type Breakpoint = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl';

export interface BreakpointInfo {
  name: Breakpoint;
  minWidth: number;
  maxWidth?: number;
}

export const BREAKPOINTS: Record<Breakpoint, { minWidth: number; maxWidth?: number }> = {
  xs: { minWidth: 0, maxWidth: 479 },
  sm: { minWidth: 480, maxWidth: 639 },
  md: { minWidth: 640, maxWidth: 767 },
  lg: { minWidth: 768, maxWidth: 1023 },
  xl: { minWidth: 1024, maxWidth: 1535 },
  '2xl': { minWidth: 1536, maxWidth: undefined },
};

const BREAKPOINT_ORDER: Breakpoint[] = ['xs', 'sm', 'md', 'lg', 'xl', '2xl'];

export interface BreakpointContextValue {
  currentBreakpoint: Breakpoint;
  isMobile: boolean;
  isTablet: boolean;
  isDesktop: boolean;
  isWide: boolean;
  width: number;
  height: number;
  isAbove: (bp: Breakpoint) => boolean;
  isBelow: (bp: Breakpoint) => boolean;
  isBetween: (min: Breakpoint, max: Breakpoint) => boolean;
}

export const BreakpointContext = createContext<BreakpointContextValue | undefined>(undefined);

export function getCurrentBreakpoint(): Breakpoint {
  if (typeof window === 'undefined') return 'xl';
  const width = window.innerWidth;
  let bp: Breakpoint = 'xs';
  for (const bpName of BREAKPOINT_ORDER) {
    const bpInfo = BREAKPOINTS[bpName];
    if (width >= bpInfo.minWidth) {
      bp = bpName;
    } else {
      break;
    }
  }
  return bp;
}

export function BreakpointProvider({ children }: { children: React.ReactNode }) {
  const [dimensions, setDimensions] = useState({ width: 0, height: 0 });
  const [currentBreakpoint, setCurrentBreakpoint] = useState<Breakpoint>('xl');

  useEffect(() => {
    const updateDimensions = () => {
      const width = window.innerWidth;
      const height = window.innerHeight;
      setDimensions({ width, height });
      
      let bp: Breakpoint = 'xs';
      for (const bpName of BREAKPOINT_ORDER) {
        const bpInfo = BREAKPOINTS[bpName];
        if (width >= bpInfo.minWidth) {
          bp = bpName;
        } else {
          break;
        }
      }
      setCurrentBreakpoint(bp);
    };

    updateDimensions();
    window.addEventListener('resize', updateDimensions);
    return () => window.removeEventListener('resize', updateDimensions);
  }, []);

  const isAbove = (bp: Breakpoint): boolean => {
    const currentIndex = BREAKPOINT_ORDER.indexOf(currentBreakpoint);
    const targetIndex = BREAKPOINT_ORDER.indexOf(bp);
    return currentIndex >= targetIndex;
  };

  const isBelow = (bp: Breakpoint): boolean => {
    const currentIndex = BREAKPOINT_ORDER.indexOf(currentBreakpoint);
    const targetIndex = BREAKPOINT_ORDER.indexOf(bp);
    return currentIndex <= targetIndex;
  };

  const isBetween = (min: Breakpoint, max: Breakpoint): boolean => {
    const currentIndex = BREAKPOINT_ORDER.indexOf(currentBreakpoint);
    const minIndex = BREAKPOINT_ORDER.indexOf(min);
    const maxIndex = BREAKPOINT_ORDER.indexOf(max);
    return currentIndex >= minIndex && currentIndex <= maxIndex;
  };

  const value = {
    currentBreakpoint,
    isMobile: currentBreakpoint === 'xs' || currentBreakpoint === 'sm',
    isTablet: currentBreakpoint === 'md' || currentBreakpoint === 'lg',
    isDesktop: currentBreakpoint === 'xl',
    isWide: currentBreakpoint === '2xl',
    width: dimensions.width,
    height: dimensions.height,
    isAbove,
    isBelow,
    isBetween,
  };

  return (
    <BreakpointContext.Provider value={value}>
      {children}
    </BreakpointContext.Provider>
  );
}

export function useBreakpoint(): import('./breakpoint-context').BreakpointContextValue {
  const context = useContext(BreakpointContext);
  if (!context) {
    throw new Error('useBreakpoint must be used within a BreakpointProvider');
  }
  return context;
}