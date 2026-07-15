import PreviewScaffold from './PreviewScaffold';
import '../auth.css';

const A11Y = [
  'OTP inputs auto-focus, auto-advance, support paste and arrow-key navigation.',
  'Each digit has an aria-label; the group has an accessible name.',
  'Countdown timer is announced via aria-live="polite".',
  'Verification errors use role="alert" and a reduced-motion-safe shake.',
  'Success shows an animated check that respects prefers-reduced-motion.',
  'Resend control is disabled during the cooldown and re-enabled after.',
];

const RESP = [
  'OTP boxes use fixed sizing tokens and wrap gracefully on narrow screens.',
  'Meta row (timer + resend) stacks if width is constrained.',
  'Brand panel hidden below 900px; mobile brand mark shown.',
  'No layout shift when switching between idle, verifying and success states.',
];

export default function OtpPreview() {
  return (
    <PreviewScaffold
      title="OTP Verification — Preview"
      subtitle="Six-digit one-time-code entry with countdown, resend, paste support and success animation."
      route="/verify-otp?demo=1"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}
