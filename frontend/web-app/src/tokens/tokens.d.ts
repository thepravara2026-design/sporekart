/**
 * SporeKart Design Tokens - TypeScript Definitions
 * Auto-generated from token JSON files. Do not edit manually.
 */

export interface ColorToken {
  value: string;
  type: 'color';
  description?: string;
}

export interface DimensionToken {
  value: string;
  type: 'dimension';
  description?: string;
}

export interface DurationToken {
  value: string;
  type: 'duration';
  description?: string;
}

export interface EasingToken {
  value: string;
  type: 'easing';
  description?: string;
}

export interface FontFamilyToken {
  value: string;
  type: 'fontFamily';
  description?: string;
}

export interface FontSizeToken {
  value: string;
  type: 'fontSize';
  description?: string;
}

export interface FontWeightToken {
  value: string;
  type: 'fontWeight';
  description?: string;
}

export interface LineHeightToken {
  value: string;
  type: 'lineHeight';
  description?: string;
}

export interface LetterSpacingToken {
  value: string;
  type: 'letterSpacing';
  description?: string;
}

export interface OpacityToken {
  value: string | number;
  type: 'opacity';
  description?: string;
}

export interface ShadowToken {
  value: string;
  type: 'shadow';
  description?: string;
}

export interface BreakpointToken {
  value: string;
  type: 'breakpoint';
  description?: string;
}

export interface ZIndexToken {
  value: number;
  type: 'zIndex';
  description?: string;
}

export interface BorderWidthToken {
  value: string;
  type: 'borderWidth';
  description?: string;
}

export interface BorderStyleToken {
  value: string;
  type: 'borderStyle';
  description?: string;
}

export interface EasingToken {
  value: string;
  type: 'easing';
  description?: string;
}

export interface RadiusToken {
  value: string;
  type: 'radius';
  description?: string;
}

export interface SpacingToken {
  value: string;
  type: 'spacing';
  description?: string;
}

export interface SurfaceToken {
  value: number;
  type: 'surfaceLevel';
  description?: string;
}

// Primitive Color Scales
export interface ColorPrimitive {
  50: ColorToken;
  100: ColorToken;
  200?: ColorToken;
  300?: ColorToken;
  400?: ColorToken;
  500: ColorToken;
  600: ColorToken;
  700: ColorToken;
  800: ColorToken;
  900: ColorToken;
  950?: ColorToken;
}

export interface ColorPrimitives {
  green: ColorPrimitive;
  neutral: ColorPrimitive;
  success: Pick<ColorPrimitive, '50' | '100' | '500' | '600' | '700'>;
  warning: Pick<ColorPrimitive, '50' | '100' | '500' | '600' | '700'>;
  danger: Pick<ColorPrimitive, '50' | '100' | '500' | '600' | '700'>;
  info: Pick<ColorPrimitive, '50' | '100' | '500' | '600' | '700'>;
  dataViz: {
    categorical: Record<string, ColorToken>;
    sequential: { start: ColorToken; end: ColorToken };
    diverging: { negative: ColorToken; neutral: ColorToken; positive: ColorToken };
  };
}

// Semantic Color Aliases
export interface SemanticColors {
  bg: {
    primary: { default: ColorToken; hover: ColorToken; pressed: ColorToken; weak: ColorToken };
    surface: { default: ColorToken; raised: ColorToken; overlay: ColorToken; modal: ColorToken };
    background: ColorToken;
    overlay: ColorToken;
    skeleton: { base: ColorToken; highlight: ColorToken };
  };
  text: {
    primary: ColorToken;
    secondary: ColorToken;
    disabled: ColorToken;
    onPrimary: ColorToken;
    onSurface: ColorToken;
    success: ColorToken;
    warning: ColorToken;
    danger: ColorToken;
    info: ColorToken;
  };
  border: {
    default: ColorToken;
    strong: ColorToken;
    focus: ColorToken;
    error: ColorToken;
    success: ColorToken;
  };
  focus: {
    ring: ColorToken;
  };
  icon: {
    default: ColorToken;
    primary: ColorToken;
    danger: ColorToken;
    success: ColorToken;
    warning: ColorToken;
    info: ColorToken;
  };
}

// Typography
export interface TypographyTokens {
  fontFamily: {
    sans: FontFamilyToken;
    mono: FontFamilyToken;
  };
  fontSize: Record<string, FontSizeToken>;
  fontWeight: Record<string, FontWeightToken>;
  lineHeight: Record<string, LineHeightToken>;
  letterSpacing: Record<string, LetterSpacingToken>;
}

// Spacing
export interface SpacingTokens {
  base: DimensionToken;
  scale: Record<string, DimensionToken>;
  padding: Record<string, DimensionToken>;
  margin: Record<string, DimensionToken>;
  gap: Record<string, DimensionToken>;
  pagePaddingX: DimensionToken;
  pagePaddingY: DimensionToken;
  sectionGap: DimensionToken;
  componentGap: DimensionToken;
  groupGap: DimensionToken;
  inlineXs: DimensionToken;
  inlineSm: DimensionToken;
  inlineMd: DimensionToken;
  stackXs: DimensionToken;
  stackSm: DimensionToken;
  stackMd: DimensionToken;
  stackLg: DimensionToken;
  headerHeight: DimensionToken;
  sidebarWidth: DimensionToken;
  sidebarIconWidth: DimensionToken;
  breadcrumbGap: DimensionToken;
  tabGap: DimensionToken;
}

// Radius
export interface RadiusTokens {
  none: RadiusToken;
  xs: RadiusToken;
  sm: RadiusToken;
  md: RadiusToken;
  lg: RadiusToken;
  xl: RadiusToken;
  full: RadiusToken;
  btn: RadiusToken;
  input: RadiusToken;
  card: RadiusToken;
  modal: RadiusToken;
  tooltip: RadiusToken;
  badge: RadiusToken;
  avatar: RadiusToken;
  table: RadiusToken;
  dropdown: RadiusToken;
}

// Elevation
export interface ElevationTokens {
  shadow: Record<string, ShadowToken>;
  surface: Record<string, SurfaceToken>;
  backdrop: {
    light: ColorToken;
    heavy: ColorToken;
  };
}

// Breakpoints
export interface BreakpointTokens {
  xs: BreakpointToken;
  sm: BreakpointToken;
  md: BreakpointToken;
  lg: BreakpointToken;
  xl: BreakpointToken;
}

// Z-Index
export interface ZIndexTokens {
  base: ZIndexToken;
  sticky: ZIndexToken;
  header: ZIndexToken;
  sidebar: ZIndexToken;
  drawer: ZIndexToken;
  dropdown: ZIndexToken;
  tooltip: ZIndexToken;
  toast: ZIndexToken;
  modalBackdrop: ZIndexToken;
  modal: ZIndexToken;
  drawerPanel: ZIndexToken;
  commandPalette: ZIndexToken;
  max: ZIndexToken;
}

// Animation
export interface AnimationTokens {
  duration: Record<string, DurationToken>;
  easing: Record<string, EasingToken>;
  motionReduction: {
    reduce: DurationToken;
  };
}

// Sizing
export interface SizingTokens {
  icon: Record<string, DimensionToken>;
  illustration: Record<string, DimensionToken>;
  component: {
    btnHeight: Record<string, DimensionToken>;
    inputHeight: Record<string, DimensionToken>;
    tableRowHeight: Record<string, DimensionToken>;
  };
  container: {
    prose: DimensionToken;
    data: DimensionToken;
    full: DimensionToken;
  };
}

// Border
export interface BorderTokens {
  width: Record<string, BorderWidthToken>;
  style: Record<string, BorderStyleToken>;
  color: Record<string, ColorToken>;
}

// Opacity
export interface OpacityTokens {
  [key: string]: OpacityToken;
}

// Z-Index
export interface ZIndexTokens {
  [key: string]: ZIndexToken;
}

// Complete Token Set
export interface DesignTokens {
  color: {
    primitives: ColorPrimitives;
    semantic: SemanticColors;
  };
  typography: TypographyTokens;
  spacing: SpacingTokens;
  radius: RadiusTokens;
  elevation: ElevationTokens;
  breakpoints: BreakpointTokens;
  zIndex: ZIndexTokens;
  animation: AnimationTokens;
  sizing: SizingTokens;
  border: BorderTokens;
  opacity: OpacityTokens;
}

// CSS Custom Properties Mapping
export interface CSSVariables {
  '--color-green-600': string;
  '--color-bg-primary-default': string;
  '--color-bg-primary-hover': string;
  '--color-bg-primary-pressed': string;
  '--color-text-primary': string;
  '--color-text-secondary': string;
  '--spacing-base': string;
  '--spacing-padding-md': string;
  '--radius-btn': string;
  '--elevation-1': string;
  '--z-modal': string;
  '--duration-fast': string;
  '--focus-ring-color': string;
  '--font-family-sans': string;
  '--text-body': string;
  // ... all tokens
}

// Theme Contract
export type Theme = 'light' | 'dark' | 'high-contrast';

export interface ThemeContextValue {
  theme: Theme;
  setTheme: (theme: Theme) => void;
  resolvedTheme: 'light' | 'dark';
}