# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: auth-validation.spec.ts >> Part 1 — Authentication Validation >> OTP verification fails deterministic code 000000
- Location: tests\auth-validation.spec.ts:80:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
Call log:
  - navigating to "http://localhost:5173/login", waiting until "load"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | test.describe('Part 1 — Authentication Validation', () => {
  4   | 
  5   |   test.beforeEach(async ({ page }) => {
  6   |     // Navigate to the login page before each test
> 7   |     await page.goto('/login');
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  8   |     await page.waitForLoadState('networkidle');
  9   |     // Wait for LoginPage to render (lazy-loaded) - wait for form to be visible
  10  |     await expect(page.locator('text=Access your workspace')).toBeVisible({ timeout: 15000 });
  11  |   });
  12  | 
  13  |   test('Switching channels updates form input labels', async ({ page }) => {
  14  |     // 1. Check default is Phone channel
  15  |     const phoneInput = page.locator('input[type="tel"]');
  16  |     await expect(phoneInput).toBeVisible();
  17  |     await expect(page.locator('text=Phone number')).toBeVisible();
  18  | 
  19  |     // 2. Click Email radio/button
  20  |     await page.click('role=radio[name="Email"]');
  21  |     
  22  |     // 3. Verify it switches to Email channel input
  23  |     const emailInput = page.locator('input[type="email"]');
  24  |     await expect(emailInput).toBeVisible();
  25  |     await expect(page.locator('text=Email address')).toBeVisible();
  26  |   });
  27  | 
  28  |   test('Validation alerts are displayed for empty identifiers', async ({ page }) => {
  29  |     // 1. Terms accepted first to trigger identifier validation
  30  |     await page.click('text=I agree to the Terms of Service');
  31  |     
  32  |     // 2. Submit empty phone number
  33  |     await page.click('button[type="submit"]');
  34  | 
  35  |     // 3. Verify inline validation error
  36  |     await expect(page.locator('text=Enter your phone number to continue.')).toBeVisible();
  37  |   });
  38  | 
  39  |   test('Validation alerts are displayed for invalid formats', async ({ page }) => {
  40  |     // 1. Accept terms
  41  |     await page.click('text=I agree to the Terms of Service');
  42  | 
  43  |     // 2. Type invalid phone
  44  |     await page.fill('input[type="tel"]', 'abc');
  45  |     await page.click('button[type="submit"]');
  46  |     await expect(page.locator('text=Enter a valid phone number.')).toBeVisible();
  47  | 
  48  |     // 3. Switch to Email and type invalid email
  49  |     await page.click('role=radio[name="Email"]');
  50  |     await page.fill('input[type="email"]', 'invalid-email');
  51  |     await page.click('button[type="submit"]');
  52  |     await expect(page.locator('text=Enter a valid email address.')).toBeVisible();
  53  |   });
  54  | 
  55  |   test('Terms agreement gate blocks form submission', async ({ page }) => {
  56  |     // 1. Fill valid identifier but leave terms unchecked
  57  |     await page.fill('input[type="tel"]', '5551234567');
  58  |     await page.click('button[type="submit"]');
  59  | 
  60  |     // 2. Verify terms error message is visible and we stay on /login
  61  |     await expect(page.locator('text=Please accept the Terms & Privacy Policy to continue.')).toBeVisible();
  62  |     expect(page.url()).toContain('/login');
  63  |   });
  64  | 
  65  |   test('Happy path login redirects to OTP verification page', async ({ page }) => {
  66  |     // 1. Fill valid phone number
  67  |     await page.fill('input[type="tel"]', '5551234567');
  68  |     // 2. Agree to terms
  69  |     await page.click('text=I agree to the Terms of Service');
  70  |     // 3. Submit
  71  |     await page.click('button[type="submit"]');
  72  | 
  73  |     // 4. Verify redirected to OTP page
  74  |     await page.waitForURL('**/verify-otp');
  75  |     expect(page.url()).toContain('/verify-otp');
  76  |     await expect(page.locator('text=Enter your code')).toBeVisible();
  77  |     await expect(page.locator('text=•••• 4567')).toBeVisible(); // Verified destination mask
  78  |   });
  79  | 
  80  |   test('OTP verification fails deterministic code 000000', async ({ page }) => {
  81  |     // 1. Setup session by submitting login
  82  |     await page.fill('input[type="tel"]', '5551234567');
  83  |     await page.click('text=I agree to the Terms of Service');
  84  |     await page.click('button[type="submit"]');
  85  |     await page.waitForURL('**/verify-otp');
  86  | 
  87  |     // 2. Fill invalid OTP "000000" in OtpInputs
  88  |     const inputs = page.locator('.sk-otp-input');
  89  |     await expect(inputs).toHaveCount(6);
  90  |     for (let i = 0; i < 6; i++) {
  91  |       await inputs.nth(i).fill('0');
  92  |     }
  93  | 
  94  |     // 3. Trigger validation / wait for latency
  95  |     await page.click('button:has-text("Verify & continue")');
  96  | 
  97  |     // 4. Expect inline validation error
  98  |     await expect(page.locator('text=Incorrect code. Please try again.')).toBeVisible();
  99  |   });
  100 | 
  101 |   test('OTP verification success redirects and establishes session', async ({ page }) => {
  102 |     // 1. Setup session by submitting login
  103 |     await page.fill('input[type="tel"]', '5551234567');
  104 |     await page.click('text=I agree to the Terms of Service');
  105 |     await page.click('button[type="submit"]');
  106 |     await page.waitForURL('**/verify-otp');
  107 | 
```