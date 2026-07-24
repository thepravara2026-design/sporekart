import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const SELF = "'self'";
const UNSAFE_INLINE = "'unsafe-inline'";
const STRICT_DYNAMIC = "'strict-dynamic'";
const isDev = process.env.NODE_ENV !== 'production';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    host: true,
    headers: {
      'X-Content-Type-Options': 'nosniff',
      'X-Frame-Options': 'DENY',
      'Referrer-Policy': 'strict-origin-when-cross-origin',
      'Permissions-Policy': 'camera=(), microphone=(), geolocation=()',
      'Content-Security-Policy': [
        `default-src ${SELF}`,
        `script-src ${SELF}${isDev ? ` ${UNSAFE_INLINE}` : ` ${STRICT_DYNAMIC}`}`,
        `style-src ${SELF} 'unsafe-inline'`,
        `img-src ${SELF} data: blob: https:`,
        `font-src ${SELF} data:`,
        `connect-src ${SELF} https://*.supabase.co https://*.stripe.com wss://*.supabase.co`,
        `frame-src ${SELF} https://*.stripe.com`,
        `object-src 'none'`,
        `base-uri ${SELF}`,
        `form-action ${SELF}`,
      ].join('; '),
    },
  },
  build: {
    sourcemap: false,
  },
});
