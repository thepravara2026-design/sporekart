import React from 'react';

/**
 * Fixes the systemic "CSS-string passed to React `style` prop" crash in the
 * Design System. DS components build styles as a CSS-string (often with `&`
 * pseudo-selectors). React 18 rejects a string for `style` and throws.
 *
 * This helper mints a unique, stable class for the instance, rewrites `&` to
 * that class (so `&:hover` -> `.{class}:hover`), and returns a real <style>
 * element plus the class name. Components drop `style={...}` and apply the
 * returned `className` instead. Tokens, hover and focus are preserved.
 */
export function useScopedStyle(css: string): {
  className: string;
  styleEl: React.ReactNode;
} {
  const rawId = React.useId();
  const className = `ds-${rawId.replace(/[^a-zA-Z0-9]/g, '')}`;
  const normalized = css.replace(/&/g, `.${className}`);
  const styleEl = (
    <style key={className}>{`.${className}{${normalized}}`}</style>
  );
  return { className, styleEl };
}
