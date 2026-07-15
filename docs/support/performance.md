# Optimization Metrics

Performance specifications for the Customer Support workspace.

## Details

- **Code Splitting**: All pages (dashboard, tickets, FAQ, KB, contact, feedback) are lazy-loaded.
- **Search Queries**: Article and ticket keyword filtering runs on local memoized components state to prevent lag.
- **Image Optimization**: Svg-based vectors and CSS properties prevent layout shifts (CLS).
