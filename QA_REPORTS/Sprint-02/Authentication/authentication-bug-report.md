# Authentication Validation — Defect Report

This report documents the defects identified during the validation of the Authentication workflows in SporeKart Sprint 2 Part 1.

---

## BUG-QA-2-001: Terms Agreement validation error message not rendered

* **Title:** Terms Agreement validation error message not rendered in login form
* **Description:** When the user enters a valid email or phone number but fails to check the "I agree to the Terms of Service and Privacy Policy" checkbox, submitting the form blocks navigation and highlights the checkbox border in red. However, the actual text description of the error ("Please accept the Terms & Privacy Policy to continue.") is never displayed on screen.
* **Environment:** Desktop/Mobile Chrome, Firefox, Safari (Mock Mode)
* **Preconditions:** User is on the `/login` page.
* **Steps to Reproduce:**
  1. Input a valid phone number (e.g., `5551234567`).
  2. Leave the Terms agreement checkbox unchecked.
  3. Click "Send secure code" button.
* **Expected Result:** Checkbox indicator turns red, and the validation text "Please accept the Terms & Privacy Policy to continue." is visible below the checkbox to provide feedback to screen readers and end users.
* **Actual Result:** Checkbox indicator turns red, but no error message text is displayed.
* **Severity:** Medium (Validation issue)
* **Priority:** High
* **Evidence:** Playwright Trace: `QA_REPORTS/Sprint-02/Authentication/authentication-evidence/traces/tc-auth-004.zip`
* **Possible Root Cause:** In `LoginPage.tsx`, the checkbox component is instantiated as follows:
  ```typescript
  <Checkbox
    label="I agree to the Terms of Service and Privacy Policy"
    checked={accepted}
    onChange={(e) => setAccepted(e.target.checked)}
    error={termsError ? true : undefined}
  />
  ```
  It passes the `error` flag, which colors the border red, but it does not supply the `helperText` property.
* **Recommendation:** Modify `LoginPage.tsx` to pass the error text in `helperText` when terms are rejected:
  ```typescript
  <Checkbox
    label="I agree to the Terms of Service and Privacy Policy"
    checked={accepted}
    onChange={(e) => setAccepted(e.target.checked)}
    error={termsError ? true : undefined}
    helperText={termsError}
  />
  ```

---

## BUG-QA-2-002: Registration consent & privacy validation messages not rendered

* **Title:** Registration consent and privacy validation errors not rendered in register form
* **Description:** Similar to `BUG-QA-2-001`, when submitting the registration form with missing consent/privacy checks, the submission is blocked and checkbox borders are colored red. However, the text details ("Please confirm the consent statement." and "You must accept the Privacy Policy.") are never rendered.
* **Environment:** Desktop/Mobile Chrome, Firefox, Safari (Mock Mode)
* **Preconditions:** User is on the `/register` page.
* **Steps to Reproduce:**
  1. Input name, valid phone, and email.
  2. Leave both the consent and privacy checkboxes unchecked.
  3. Click "Create account" button.
* **Expected Result:** Checkbox indicators turn red, and the respective text warnings are rendered.
* **Actual Result:** Checkbox indicators turn red, but no error messages are displayed.
* **Severity:** Medium (Validation issue)
* **Priority:** High
* **Evidence:** Playwright Log: `QA_REPORTS/Sprint-02/Authentication/authentication-evidence/logs/register-submit.log`
* **Possible Root Cause:** In `RegisterPage.tsx`, the checkbox instances pass `error={errors.consent ? true : undefined}` but do not pass the error message string as `helperText`.
* **Recommendation:** Update `RegisterPage.tsx` to supply the `helperText` property to the consent and privacy checkbox components:
  ```typescript
  <Checkbox
    label="I consent to receive verification codes..."
    checked={consent}
    onChange={(e) => setConsent(e.target.checked)}
    error={errors.consent ? true : undefined}
    helperText={errors.consent}
  />
  ```
