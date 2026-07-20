import { test, expect } from '@playwright/test';

test.describe('Part 1 — Authentication Validation', () => {

  test.beforeEach(async ({ page }) => {
    // Navigate to the login page before each test
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    // Wait for LoginPage to render (lazy-loaded) - wait for form to be visible
    await expect(page.locator('text=Access your workspace')).toBeVisible({ timeout: 15000 });
  });

  test('Switching channels updates form input labels', async ({ page }) => {
    // 1. Check default is Phone channel
    const phoneInput = page.locator('input[type="tel"]');
    await expect(phoneInput).toBeVisible();
    await expect(page.locator('text=Phone number')).toBeVisible();

    // 2. Click Email radio/button
    await page.click('role=radio[name="Email"]');
    
    // 3. Verify it switches to Email channel input
    const emailInput = page.locator('input[type="email"]');
    await expect(emailInput).toBeVisible();
    await expect(page.locator('text=Email address')).toBeVisible();
  });

  test('Validation alerts are displayed for empty identifiers', async ({ page }) => {
    // 1. Terms accepted first to trigger identifier validation
    await page.click('text=I agree to the Terms of Service');
    
    // 2. Submit empty phone number
    await page.click('button[type="submit"]');

    // 3. Verify inline validation error
    await expect(page.locator('text=Enter your phone number to continue.')).toBeVisible();
  });

  test('Validation alerts are displayed for invalid formats', async ({ page }) => {
    // 1. Accept terms
    await page.click('text=I agree to the Terms of Service');

    // 2. Type invalid phone
    await page.fill('input[type="tel"]', 'abc');
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter a valid phone number.')).toBeVisible();

    // 3. Switch to Email and type invalid email
    await page.click('role=radio[name="Email"]');
    await page.fill('input[type="email"]', 'invalid-email');
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter a valid email address.')).toBeVisible();
  });

  test('Terms agreement gate blocks form submission', async ({ page }) => {
    // 1. Fill valid identifier but leave terms unchecked
    await page.fill('input[type="tel"]', '5551234567');
    await page.click('button[type="submit"]');

    // 2. Verify terms error message is visible and we stay on /login
    await expect(page.locator('text=Please accept the Terms & Privacy Policy to continue.')).toBeVisible();
    expect(page.url()).toContain('/login');
  });

  test('Happy path login redirects to OTP verification page', async ({ page }) => {
    // 1. Fill valid phone number
    await page.fill('input[type="tel"]', '5551234567');
    // 2. Agree to terms
    await page.click('text=I agree to the Terms of Service');
    // 3. Submit
    await page.click('button[type="submit"]');

    // 4. Verify redirected to OTP page
    await page.waitForURL('**/verify-otp');
    expect(page.url()).toContain('/verify-otp');
    await expect(page.locator('text=Enter your code')).toBeVisible();
    await expect(page.locator('text=•••• 4567')).toBeVisible(); // Verified destination mask
  });

  test('OTP verification fails deterministic code 000000', async ({ page }) => {
    // 1. Setup session by submitting login
    await page.fill('input[type="tel"]', '5551234567');
    await page.click('text=I agree to the Terms of Service');
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');

    // 2. Fill invalid OTP "000000" in OtpInputs
    const inputs = page.locator('.sk-otp-input');
    await expect(inputs).toHaveCount(6);
    for (let i = 0; i < 6; i++) {
      await inputs.nth(i).fill('0');
    }

    // 3. Trigger validation / wait for latency
    await page.click('button:has-text("Verify & continue")');

    // 4. Expect inline validation error
    await expect(page.locator('text=Incorrect code. Please try again.')).toBeVisible();
  });

  test('OTP verification success redirects and establishes session', async ({ page }) => {
    // 1. Setup session by submitting login
    await page.fill('input[type="tel"]', '5551234567');
    await page.click('text=I agree to the Terms of Service');
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');

    // 2. Fill valid OTP "123456" — OTP auto-submits on complete via onComplete callback
    const inputs = page.locator('.sk-otp-input');
    for (let i = 0; i < 6; i++) {
      await inputs.nth(i).fill((i + 1).toString());
    }

    // 3. OTP auto-submits; wait for success UI
    await expect(page.locator('text=Verified! Taking you to your workspace…')).toBeVisible({ timeout: 10000 });
  });

  test('Resend OTP cooldown timer and resend action', async ({ page }) => {
    await page.fill('input[type="tel"]', '5551234567');
    await page.click('text=I agree to the Terms of Service');
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');

    // 1. Verify Resend button is initially disabled due to cooldown
    const resendBtn = page.locator('button:has-text("Resend code")');
    await expect(resendBtn).toBeDisabled();
    await expect(page.locator('text=Resend available in')).toBeVisible();
  });

  test('RegisterPage submits and transitions to OTP', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');

    // 1. Fill registration details
    await page.getByLabel('Full name').fill('Alice Grower');
    await page.fill('input[type="tel"]', '5559876543');
    await page.fill('input[type="email"]', 'alice@grower.mock');
    
    // 2. Check consents
    await page.click('text=I consent to receive verification codes');
    await page.click('text=I have read and accept the Privacy Policy');

    // 3. Submit
    await page.click('button[type="submit"]');

    // 4. Verify redirects to OTP
    await page.waitForURL('**/verify-otp');
    expect(page.url()).toContain('/verify-otp');
    await expect(page.locator('text=Verify your phone')).toBeVisible();
  });

  test('ForgotPasswordPage submits and shows success state', async ({ page }) => {
    await page.goto('/forgot-password');
    await page.waitForLoadState('networkidle');

    // 1. Enter email address
    await page.fill('input[type="email"]', 'recover@sporekart.mock');
    await page.click('button[type="submit"]');

    // 2. Verify success message and redirect action button
    await expect(page.locator('text=We sent recovery instructions to')).toBeVisible();
    await expect(page.locator('button:has-text("Back to sign in")')).toBeVisible();
  });

  test('Social sign in buttons return unenabled warning', async ({ page }) => {
    // Click Google sign in
    await page.click('button:has-text("Google")');
    await expect(page.locator('text=Social login is not enabled yet.')).toBeVisible();
  });
});
