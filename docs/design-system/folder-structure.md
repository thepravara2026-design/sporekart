# Folder Structure

```
src/design-system/
├── index.ts                          # Public API — re-exports every component, hook, utility
│
├── tokens/                           # Design token definitions
│   ├── colors.ts                     #   Primitive color tokens
│   ├── typography.ts                 #   Font family, size, weight, line-height
│   ├── spacing.ts                    #   Space scale (4px base)
│   ├── radius.ts                     #   Border radius scale
│   ├── elevation.ts                  #   Shadow / z-depth scale
│   ├── breakpoints.ts               #   Responsive breakpoint values
│   ├── animation.ts                  #   Duration and easing tokens
│   ├── opacity.ts                    #   Opacity scale
│   ├── sizing.ts                     #   Width/height scale
│   ├── zIndex.ts                     #   Z-index scale
│   ├── border.ts                     #   Border width tokens
│   ├── semantic.ts                   #   Semantic alias tokens (mapping primitives → meaning)
│   ├── component.ts                  #   Component-specific token overrides
│   └── index.ts                      #   Token barrel + CSS variable generation
│
├── primitives/                       # Atomic, unstyled components
│   ├── Box/                          #   Generic layout container
│   ├── Text/                         #   Typography primitive
│   ├── Icon/                         #   Icon renderer
│   ├── VisuallyHidden/               #   Screen-reader-only content
│   └── index.ts
│
├── core/                             # Single-purpose interactive components
│   ├── Button/
│   ├── Input/
│   ├── Select/
│   ├── Checkbox/
│   ├── Radio/
│   ├── Switch/
│   ├── Textarea/
│   ├── Badge/
│   ├── Tooltip/
│   └── index.ts
│
├── composite/                        # Multi-part compound components
│   ├── DatePicker/
│   ├── DataTable/
│   ├── FormGroup/
│   ├── Card/
│   ├── Accordion/
│   ├── Tabs/
│   ├── Breadcrumb/
│   ├── Pagination/
│   ├── Stepper/
│   └── index.ts
│
├── feedback/                         # Overlay & notification components
│   ├── Toast/
│   ├── Dialog/
│   ├── NotificationBanner/
│   ├── ProgressBar/
│   ├── Skeleton/
│   ├── Spinner/
│   └── index.ts
│
├── layout/                           # Page structure components
│   ├── PageShell/
│   ├── Grid/
│   ├── Stack/
│   ├── Container/
│   ├── Sidebar/
│   ├── Header/
│   └── index.ts
│
├── hooks/                            # Shared React hooks
│   ├── useTheme.ts
│   ├── useMediaQuery.ts
│   ├── useReducedMotion.ts
│   ├── useBreakpoint.ts
│   ├── useId.ts
│   └── index.ts
│
├── utils/                            # Utility functions
│   ├── cx.ts                         #   Classname merging (clsx + twMerge)
│   ├── polymorphic.ts                #   Polymorphic component types
│   ├── mergeRefs.ts                  #   Ref merging utility
│   └── index.ts
│
├── providers/                        # Context providers
│   ├── ThemeProvider/
│   ├── ToastProvider/
│   ├── DialogProvider/
│   ├── NotificationProvider/
│   ├── ErrorBoundary/
│   ├── FeatureFlagProvider/
│   ├── PerformanceProvider/
│   ├── AccessibilityProvider/
│   ├── LocalizationProvider/
│   └── index.ts
│
├── styles/                           # Global CSS
│   ├── reset.css                     #   CSS reset / normalize
│   ├── tokens.css                    #   CSS custom properties
│   ├── global.css                    #   Global styles (scrollbar, selection, focus)
│   ├── utilities.css                 #   Utility classes
│   └── animations.css                #   Keyframe animations
│
├── icons/                            # Icon system
│   ├── registry.ts                   #   Icon registry (name → component map)
│   ├── types.ts                      #   Icon props and types
│   ├── createIcon.tsx                #   Icon factory
│   ├── assets/                       #   Raw SVG assets
│   └── index.ts
│
└── playground/                       # Development playground routes
    ├── index.tsx                     #   Playground index/list
    ├── icons.tsx                     #   Icon gallery
    ├── colors.tsx                    #   Token color swatches
    ├── typography.tsx                #   Type scale demo
    └── index.ts
```
