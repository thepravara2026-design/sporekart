import PreviewScaffold from './PreviewScaffold';
import '../auth.css';

const A11Y = [
  '401/403/404/Auth/Network/Server each use a distinct tone and icon.',
  'Error text explains the problem and the next action, not just a code.',
  'Support CTA is a clear, labeled secondary action.',
  'Headings convey the error; no reliance on color alone.',
  'All screens expose a path back to sign-in or home.',
];

const RESP = [
  'Consistent centered status card across all error variants.',
  'Tone colors (warning/danger) adapt to theme and large displays.',
  'Action buttons stack and stretch full width on mobile.',
  'No console errors; pure presentational components.',
];

export default function AuthErrorsPreview() {
  return (
    <PreviewScaffold
      title="Authentication Errors — Preview"
      subtitle="401, 403, authentication, network and server error screens with retry and support CTAs."
      route="/auth-error"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}
