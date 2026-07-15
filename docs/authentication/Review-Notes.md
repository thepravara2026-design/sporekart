# Authentication — Review Notes

**Sprint 21 · Part 7** · For design + product review (sprint stop)

## What to review
- **Visual:** `AuthLayout` split/stacked layout, brand panel, card elevation, spacing, typography, focus rings. Preview at `/preview/login`, `/preview/register`, `/preview/otp`, `/preview/session`, `/preview/auth-errors`.
- **Copy/microcopy:** titles, subtitles, button labels, error messages, legal consent wording.
- **Flow:** login → OTP → success; register → OTP → success; forgot-password success; session/error states.
- **Icons:** confirm the chosen registry icons match brand intent (`leaf`, `smartphone`, `mail`, `lock`, `shield`, `key`, `user-plus`, `log-in`, `log-out`, `slash`, `alert-circle`, `wifi`, `database`, `clock`, `check-circle`).

## Known placeholders (do NOT review as final)
- Social login buttons (Google/Apple/Facebook) return "not enabled yet" — integration pending platform auth.
- `authClient` is a UI-only stub; OTP `000000` is the reserved failure code; latency ~900ms.
- Post-auth redirect target is a placeholder (owned by enterprise shell).
- Role picker restricts self-serve to `customer`; grower/partner disabled (invite-only).

## Checklist
- [ ] Layout matches design language (color, type, radius, elevation, motion).
- [ ] Copy is final and on-brand; legal consent wording approved by legal.
- [ ] Responsive breakpoints (≥960px split, <960px stacked) accepted.
- [ ] Accessibility spot-check (keyboard + SR) passed.
- [ ] Error/gallery states (`/auth-error`) approved.
- [ ] Confirm real post-verify destination + session/error wiring owners.

## Decisions needed
1. Post-auth navigation target (enterprise shell).
2. Social provider branding + rollout order.
3. Role provisioning flow for grower/partner.
4. Whether a password/credential path is required (currently passwordless OTP).

## Sign-off
- Design: ________  Date: ________
- Product: ________  Date: ________
- Eng: ____________  Date: ________
