import { Page, Locator } from '@playwright/test';

export class LoginPage {
  readonly page: Page;
  readonly phoneInput: Locator;
  readonly sendCodeButton: Locator;
  readonly otpInputs: Locator;
  readonly verifyButton: Locator;
  readonly termsCheckbox: Locator;
  readonly errorMessage: Locator;

  constructor(page: Page) {
    this.page = page;
    this.phoneInput = page.locator('input[type="tel"], input[name="phone"], input[placeholder*="phone" i]');
    this.sendCodeButton = page.locator('button:has-text("Send secure code")');
    this.otpInputs = page.locator('input[type="tel"][maxlength="1"], .otp-input, input[aria-label*="OTP" i]');
    this.verifyButton = page.locator('button:has-text("Verify")');
    this.termsCheckbox = page.locator('input[type="checkbox"]').first();
    this.errorMessage = page.locator('[role="alert"], .error-message, .validation-error');
  }

  async goto(): Promise<void> {
    await this.page.goto('/login');
    await this.page.waitForLoadState('networkidle');
  }

  async enterPhone(phone: string): Promise<void> {
    await this.phoneInput.fill(phone);
  }

  async checkTerms(): Promise<void> {
    if (!(await this.termsCheckbox.isChecked())) {
      await this.termsCheckbox.check();
    }
  }

  async uncheckTerms(): Promise<void> {
    if (await this.termsCheckbox.isChecked()) {
      await this.termsCheckbox.uncheck();
    }
  }

  async clickSendCode(): Promise<void> {
    await this.sendCodeButton.click();
  }

  async enterOtp(code: string): Promise<void> {
    const count = await this.otpInputs.count();
    for (let i = 0; i < count && i < code.length; i++) {
      await this.otpInputs.nth(i).fill(code[i]);
    }
  }

  async clickVerify(): Promise<void> {
    await this.verifyButton.click();
  }
}
