import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import { initSentry } from './lib/sentry';
import { validateEnv } from './config/env';
import { logger } from './lib/logger';
import './styles/global.css';
import '../../tokens/global.css';

// Initialize Sentry error monitoring (P0-05)
initSentry();

// Validate environment configuration (P0-04)
const { valid, errors } = validateEnv();
if (!valid) {
  logger.warn('[env] Missing required environment variables:', { errors });
}

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
);
