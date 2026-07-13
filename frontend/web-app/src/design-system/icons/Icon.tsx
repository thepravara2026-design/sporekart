import React, { useMemo } from 'react';
import { registry, IconProps } from './registry';

export type IconName = string;

export interface IconComponentProps extends IconProps {
  name: IconName;
}

const TOKEN_SIZES: Record<string, number> = {
  xs: 16,
  sm: 20,
  md: 24,
  lg: 28,
  xl: 32,
  '2xl': 40,
  '3xl': 48,
};

function resolveSize(size?: number | string): number {
  if (size === undefined || size === null) return 24;
  if (typeof size === 'number') return size;
  return TOKEN_SIZES[size] || 24;
}

export const Icon: React.FC<IconComponentProps> = ({
  name,
  size,
  color,
  className = '',
  'aria-label': ariaLabel,
}) => {
  const resolvedSize = useMemo(() => resolveSize(size), [size]);

  const IconComponent = registry.get(name);

  const svgProps = useMemo((): Record<string, unknown> => {
    const props: Record<string, unknown> = {
      size: resolvedSize,
      color: color || undefined,
      className: className ? `sk-icon ${className}` : 'sk-icon',
    };
    if (ariaLabel) {
      props['aria-label'] = ariaLabel;
      props.role = 'img';
    } else {
      props['aria-hidden'] = true;
      props['focusable'] = false;
    }
    return props;
  }, [resolvedSize, color, className, ariaLabel]);

  if (!IconComponent) {
    return (
      <span
        className={`sk-icon sk-icon--fallback ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: resolvedSize,
          height: resolvedSize,
          backgroundColor: 'var(--color-bg-surface-raised, #f0f0f0)',
          borderRadius: 'var(--radius-sm, 4px)',
          color: color || 'var(--color-icon-default, #999)',
          fontSize: Math.round(resolvedSize * 0.5),
          lineHeight: 1,
          userSelect: 'none',
          ...(ariaLabel ? {} : { 'aria-hidden': true as unknown }),
        } as React.CSSProperties}
        {...(ariaLabel ? { 'aria-label': ariaLabel } : { 'aria-hidden': true, 'focusable': false })}
      >
        ?
      </span>
    );
  }

  return <IconComponent {...(svgProps as IconProps)} />;
};

Icon.displayName = 'Icon';

export default Icon;
