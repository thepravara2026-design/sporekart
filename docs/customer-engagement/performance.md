# Performance Targets

This document tracks performance optimizations and layout shifts.

## Optimizations

1. **Lazy Loading**: Router paths are loaded dynamically using `lazy` and `Suspense` fallback screens.
2. **Minimal Layout Shift (CLS)**: Skeletons preserve layout geometry, minimizing page shifts.
3. **Optimized Event Bundles**: Search, filtering, and point redemption are processed efficiently in local state.
