import PreviewScaffold from './PreviewScaffold';
import '../auth.css';

const A11Y = [
  'Status screens use a single <main> landmark with a descriptive heading.',
  'Tone is conveyed by icon, color and text — not color alone.',
  'Primary and secondary actions are large, labeled buttons with visible focus.',
  'Spinner has role="status" and an accessible label.',
  'No time limits or redirects that trap keyboard users.',
];

const RESP = [
  'Centered, max-width 480px card on a full-viewport canvas.',
  'Icons and spacing scale using semantic tokens across breakpoints.',
  'Cards remain centered and readable from 320px to large displays.',
  'Action buttons stack vertically and stretch full width on mobile.',
];

export default function SessionPreview() {
  return (
    <PreviewScaffold
      title="Session & Loading — Preview"
      subtitle="Session expired, signed out, access denied and authentication loading screens."
      route="/session-expired"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}
