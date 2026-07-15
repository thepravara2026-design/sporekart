import PreviewScaffold from './PreviewScaffold';
import '../auth.css';

const A11Y = [
  'Brand panel is decorative and hidden from assistive tech (aria-hidden).',
  'All inputs use associated labels and error alerts with role="alert".',
  'Channel toggle is a radiogroup with aria-checked and full keyboard support.',
  'Focus moves to the first field on load; visible focus ring via --color-focus-ring.',
  'Required consent checkboxes block submission with inline messages.',
  'Honors prefers-reduced-motion for spinners and transitions.',
];

const RESP = [
  'Split layout collapses to a single column below 900px; brand panel hidden.',
  'Form panel scrolls independently and centers content on tall viewports.',
  'Inputs and buttons are full-width with comfortable touch targets (44px).',
  'Mobile brand mark appears when the side panel is hidden.',
  'No layout shift (CLS): fixed card max-width and stable spacing tokens.',
];

export default function LoginPreview() {
  return (
    <PreviewScaffold
      title="Login — Preview"
      subtitle="Premium phone/email OTP sign-in with social placeholders, remember me and consent."
      route="/login"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}
