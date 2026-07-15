# Authentication — OTP Verification

**Screen:** `VerifyOtpPage` · route `/verify-otp` · preview `/preview/otp`

## Purpose
Verify the one-time code sent during login or registration. Premium, focused, low-friction verification with clear feedback and recovery paths.

## Entry
- Requires `location.state` (`channel`, `destination`, `flow`). Use `?demo=1` to seed a destination for isolated preview/testing.

## Layout
- `AuthLayout` (form panel focused; brand panel present for context).
- Header: "Enter the code we sent" + destination chip (masked) + channel icon.
- `OtpInput` (`length={6}`, `size="lg"`), `role="group"`, per-digit `aria-label`.
- Helper line: "Code expires in {m:ss}" (live expiry countdown via `useCountdown`).
- `Resend code` button: disabled during 30s cooldown, shows countdown ("Resend in 29s").
- Inline `AuthAlert` on error.
- Primary `Button` ("Verify") with `loading`; auto-submits on 6-digit completion via `onComplete`.
- Footer: "Wrong number? Use a different account" (back to `/login`).

## Behavior
- Auto-advances focus; supports Backspace, Arrow keys, Home/End, and paste-to-fill.
- On 6 digits → `authClient.verifyOtp(channel, destination, code)`.
  - Stub accepts any 6-char code except `000000` (reserved failure).
  - `<4` chars rejected.
- On success → `StatusScreen` ("Verified") then redirect to the post-auth target.
- On failure → error alert; OTP cleared; focus returns to first digit.

## Validation / edge cases
- Expiry countdown (300s) surfaced; on expiry, prompt to resend.
- Resend cooldown (30s) enforced by `useCountdown`; button disabled + labeled.
- Demo mode seeds destination so the screen renders without prior navigation.

## Accessibility
- `OtpInput` is a labeled group with individual digit labels and `inputMode="numeric"` + `autoComplete="one-time-code"`.
- Errors use `role="alert"`; focus management returns to the code on failure.

## Open questions
- Real OTP delivery + verification owned by platform auth (swap `authClient.verifyOtp`).
- Confirm post-verify redirect target (enterprise shell).
