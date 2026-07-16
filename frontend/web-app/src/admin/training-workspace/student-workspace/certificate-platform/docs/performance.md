# Certificate Platform — Performance

## Bundle Size

| Module | Estimated Size |
|--------|---------------|
| Types | ~4 KB |
| Mock Data | ~10 KB |
| Context | ~4 KB |
| Components (15) | ~20 KB |
| Pages (9) | ~14 KB |
| Docs (16) | ~22 KB |
| **Total** | **~74 KB** |

## Memoization

- All 15 components use `React.memo`
- `useMemo` for filtered/sorted data
- `useCallback` for setter functions
- Context value wrapped in `useMemo`

## Lazy Loading

- `CertificateIndex` lazy-loaded in App.tsx
- All 9 sub-pages co-located in single bundle

## Render Optimization

- Mock data deterministic, generated once at module load
- Pagination limits DOM to 10-12 items per page
- Skeletons shown during initial render
- Wallet tab switching uses conditional rendering
