# Session Management Validation — Defect Report

This report documents the defects identified during the validation of the Session Management workflows in SporeKart Sprint 2 Part 2.

---

## BUG-QA-2-003: Focus is not managed or trapped inside SessionTimeoutWarning dialog

* **Title:** Focus is not managed or trapped inside SessionTimeoutWarning dialog modal
* **Description:** When the idle session warning dialog mounts (`SessionTimeoutWarning`), keyboard focus is not moved automatically to the primary "Stay signed in" button, nor is keyboard focus trapped within the modal wrapper. This allows tab key navigation to escape the dialog onto invisible elements in the background page, violating WCAG A11y standards for modal alerts.
* **Environment:** Desktop Chrome, Firefox, Safari (Mock Mode)
* **Preconditions:** User is signed in and has a session active in the admin workspace.
* **Steps to Reproduce:**
  1. Trigger an idle state session warning (wait for timeout or trigger `state === 'timeout_warning'`).
  2. The alertdialog overlay mounts on top of the workspace.
  3. Try pressing the Tab key multiple times.
* **Expected Result:** Keyboard focus is immediately moved to the "Stay signed in" button when the dialog opens, and focus remains trapped within the dialog's buttons ("Stay signed in" and "Sign out") until dismissed.
* **Actual Result:** Keyboard focus remains on the background document and can tab through links/inputs located underneath the modal overlay.
* **Severity:** Medium (Accessibility violation / focus trap lack)
* **Priority:** High
* **Evidence:** Playwright Spec: `shared-testing/tests/session-management.spec.ts` (validation checks failed on focus boundaries).
* **Possible Root Cause:** In `SessionTimeoutWarning.tsx`, the overlay container renders as a simple `div` without any focus manipulation or `useEffect` hook to target the primary button or capture Tab keypresses.
* **Recommendation:**
  1. Add a ref to the primary button and call `.focus()` when the modal opens:
     ```typescript
     const stayBtnRef = useRef<HTMLButtonElement>(null);
     useEffect(() => {
       if (open) stayBtnRef.current?.focus();
     }, [open]);
     ```
  2. Integrate a basic focus trap callback that prevents `keydown` Tab actions from leaving the button container boundaries when `open` is true.
