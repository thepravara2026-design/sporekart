# Performance & CLS Metrics

Performance validation and optimizations.

## Optimizations

1. **Lazy Loading**: Router elements are imported dynamically using React `lazy` and loaded inside a `Suspense` wrapper.
2. **Minimal Layout Shift (CLS)**: Skeletons (`ShimmerLoader`) mimic card structures to preserve geometry, reducing layout shifts.
3. **No Heavy Packages**: Built using native CSS, custom SVG animations, and standard React hooks without bundling external mapping libraries.
