import React from 'react';

export type SocialProvider = 'google' | 'apple' | 'facebook';

export interface SocialLoginProps {
  onSelect: (provider: SocialProvider) => void;
  disabled?: boolean;
}

const PROVIDERS: { id: SocialProvider; label: string; glyph: React.ReactNode }[] = [
  {
    id: 'google',
    label: 'Continue with Google',
    glyph: (
      <svg width="18" height="18" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
        <path fill="#EA4335" d="M12 10.2v3.9h5.5c-.24 1.4-1.6 4.1-5.5 4.1-3.3 0-6-2.7-6-6s2.7-6 6-6c1.9 0 3.1.8 3.8 1.5l2.6-2.5C16.7 3.2 14.6 2.2 12 2.2 6.9 2.2 2.8 6.3 2.8 11.4S6.9 20.6 12 20.6c5.4 0 9-3.8 9-9.2 0-.6-.06-1-.14-1.4H12z" />
      </svg>
    ),
  },
  {
    id: 'apple',
    label: 'Continue with Apple',
    glyph: (
      <svg width="18" height="18" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
        <path fill="#111827" d="M16.4 12.7c0-2 1.6-3 1.7-3-1-1.4-2.4-1.6-2.9-1.6-1.2-.1-2.4.7-3 .7-.6 0-1.6-.7-2.6-.7-1.3 0-2.6.8-3.3 2-1.4 2.4-.4 6 1 8 .7 1 1.4 2 2.4 2 1 0 1.3-.6 2.5-.6 1.1 0 1.5.6 2.5.6 1 0 1.7-1 2.3-2 .7-1.1 1-2.2 1-2.2-.1 0-2-.8-2-2.7zM14.5 6.3c.5-.7.9-1.6.8-2.5-.8 0-1.8.5-2.4 1.2-.5.6-.9 1.5-.8 2.4.9 0 1.8-.4 2.4-1.1z" />
      </svg>
    ),
  },
  {
    id: 'facebook',
    label: 'Continue with Facebook',
    glyph: (
      <svg width="18" height="18" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
        <path fill="#1877F2" d="M22 12a10 10 0 1 0-11.6 9.9v-7H7.9V12h2.5V9.8c0-2.5 1.5-3.9 3.8-3.9 1.1 0 2.2.2 2.2.2v2.4h-1.2c-1.2 0-1.6.7-1.6 1.5V12h2.7l-.4 2.9h-2.3v7A10 10 0 0 0 22 12z" />
      </svg>
    ),
  },
];

/**
 * Social login placeholders. Providers are NOT integrated (per Sprint 21 Part 7:
 * "Do NOT integrate providers"). The buttons are present, accessible, and wired
 * to a handler so they can be enabled once the backend is ready.
 */
export function SocialLogin({ onSelect, disabled = false }: SocialLoginProps) {
  return (
    <div className="auth-social" role="group" aria-label="Social sign-in options">
      {PROVIDERS.map((p) => (
        <button
          key={p.id}
          type="button"
          className="auth-social__btn"
          onClick={() => onSelect(p.id)}
          disabled={disabled}
          data-provider={p.id}
        >
          {p.glyph}
          {p.label}
        </button>
      ))}
    </div>
  );
}

export default SocialLogin;
