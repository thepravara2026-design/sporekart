import PreviewScaffold from './PreviewScaffold';
import '../auth.css';

const A11Y = [
  'Sequential fieldset-style grouping with labeled inputs and inline validation.',
  'Role select uses the enterprise Select with helper text and disabled invite-only options.',
  'Dual consent checkboxes are required and announced on error.',
  'Submit is a single primary action; error summary uses role="alert".',
  'Keyboard order: name → phone → email → role → consents → submit.',
];

const RESP = [
  'Long form scrolls within the form panel; nothing is cut off on mobile.',
  'Select and inputs are full-width with 44px touch targets.',
  'Brand panel hidden below 900px; mobile brand mark shown.',
  'Spacing scales with clamp() tokens for comfortable density on small screens.',
];

export default function RegisterPreview() {
  return (
    <PreviewScaffold
      title="Registration — Preview"
      subtitle="Customer self-registration collecting name, phone, optional email, role and consents before OTP."
      route="/register"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}
