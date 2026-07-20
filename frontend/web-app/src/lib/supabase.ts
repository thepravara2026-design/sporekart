import { createClient, type SupabaseClient, type Session, type User } from '@supabase/supabase-js';
import { useEnv } from '../config/env';
import { logger } from './logger';
import type { Role } from '../config/roles';
let _client: SupabaseClient | null = null;
let _session: Session | null = null;
let _user: User | null = null;
let _mockMode = false;

type AuthCallback = (session: Session | null, user: User | null) => void;
const _listeners: Set<AuthCallback> = new Set();

function notify() {
  _listeners.forEach((cb) => cb(_session, _user));
}

export function createMockSession(phone: string, role: Role = 'customer'): void {
  _mockMode = true;
  _session = {
    access_token: `mock_access_${Date.now()}`,
    refresh_token: `mock_refresh_${Date.now()}`,
    expires_in: 86400,
    expires_at: Math.floor(Date.now() / 1000) + 86400,
    token_type: 'bearer',
    user: {
      id: `mock_user_${Date.now()}`,
      aud: 'authenticated',
      email: `${phone.replace(/\D/g, '')}@mock.sporekart.com`,
      phone,
      role: 'authenticated',
      app_metadata: { provider: 'phone' },
      user_metadata: { role, phone, full_name: `User ${phone.slice(-4)}` },
      identities: [],
      created_at: new Date().toISOString(),
      updated_at: new Date().toISOString(),
    },
  } as unknown as Session;
  _user = _session.user;
  logger.info('[supabase] Mock session created:', { userId: _user?.id, role });
  notify();
}

export function clearMockSession(): void {
  if (!_mockMode) return;
  _mockMode = false;
  _session = null;
  _user = null;
  notify();
}

export function getSupabaseClient(): SupabaseClient | null {
  if (_client) return _client;

  const env = useEnv();
  if (!env.supabaseUrl || !env.supabaseAnonKey) {
    logger.warn('[supabase] Cannot initialize — missing SUPABASE_URL or SUPABASE_ANON_KEY');
    return null;
  }

  _client = createClient(env.supabaseUrl, env.supabaseAnonKey, {
    auth: {
      autoRefreshToken: true,
      persistSession: true,
      detectSessionInUrl: true,
      storage: {
        getItem: (key: string) => {
          try { return localStorage.getItem(key); } catch { return null; }
        },
        setItem: (key: string, value: string) => {
          try { localStorage.setItem(key, value); } catch {} },
        removeItem: (key: string) => {
          try { localStorage.removeItem(key); } catch {} },
      },
    },
  });

  _client.auth.onAuthStateChange((_event: string, session: Session | null) => {
    _session = session;
    _user = session?.user ?? null;
    logger.debug('[supabase] Auth state changed:', {
      event: _event,
      userId: _user?.id,
      hasSession: !!session,
    });
    notify();
  });

  // Restore session on init
  _client.auth.getSession().then(({ data: { session } }: { data: { session: Session | null } }) => {
    _session = session;
    _user = session?.user ?? null;
    notify();
  });

  logger.info('[supabase] Client initialized');
  return _client;
}

export function onAuthChange(callback: AuthCallback): () => void {
  _listeners.add(callback);
  // Immediately call with current state
  if (_session !== null) {
    callback(_session, _user);
  }
  return () => { _listeners.delete(callback); };
}

export async function signInWithOtp(channel: 'phone' | 'email', destination: string): Promise<void> {
  const client = getSupabaseClient();
  if (!client) throw new Error('Supabase not configured');

  if (channel === 'phone') {
    const { error } = await client.auth.signInWithOtp({ phone: destination });
    if (error) throw error;
  } else {
    const { error } = await client.auth.signInWithOtp({ email: destination });
    if (error) throw error;
  }
  logger.info('[auth] OTP sent:', { channel, destination });
}

export async function verifyOtp(channel: 'phone' | 'email', destination: string, token: string): Promise<Session> {
  const client = getSupabaseClient();
  if (!client) throw new Error('Supabase not configured');

  let result;
  if (channel === 'phone') {
    result = await client.auth.verifyOtp({ phone: destination, token, type: 'sms' });
  } else {
    result = await client.auth.verifyOtp({ email: destination, token, type: 'email' });
  }

  if (result.error) throw result.error;
  if (!result.data.session) throw new Error('No session returned from OTP verification');

  _session = result.data.session;
  _user = result.data.session.user;
  logger.info('[auth] OTP verified, session established:', { userId: _user?.id });
  return _session;
}

export async function signUp(email: string, password: string, metadata?: Record<string, unknown>): Promise<Session> {
  const client = getSupabaseClient();
  if (!client) throw new Error('Supabase not configured');

  const { data, error } = await client.auth.signUp({
    email,
    password,
    options: { data: metadata },
  });

  if (error) throw error;
  if (!data.session) throw new Error('Email confirmation required');

  _session = data.session;
  _user = data.session.user;
  logger.info('[auth] Sign up successful:', { userId: _user?.id });
  return _session;
}

export async function signOut(): Promise<void> {
  if (_mockMode) {
    clearMockSession();
    return;
  }

  const client = getSupabaseClient();
  if (!client) return;

  const { error } = await client.auth.signOut();
  if (error) logger.error('[auth] Sign out error:', error);

  _session = null;
  _user = null;
  logger.info('[auth] Signed out');
}

export function getSession(): Session | null {
  return _session;
}

export function getUser(): User | null {
  return _user;
}

export async function getAccessToken(): Promise<string | null> {
  const client = getSupabaseClient();
  if (!client) return null;

  const { data } = await client.auth.getSession();
  return data.session?.access_token ?? null;
}

export function isAuthenticated(): boolean {
  return _session !== null && !!_user;
}
