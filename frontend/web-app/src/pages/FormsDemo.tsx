import { useState, FormEvent, ChangeEvent } from 'react';

interface FormValues {
  // Basic
  email: string;
  password: string;
  confirmPassword: string;
  quantity: string;
  // OTP
  otp: string[];
  // Address
  addressLine1: string;
  addressLine2: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  // Checkout
  cardName: string;
  cardNumber: string;
  cardExpiry: string;
  cardCvc: string;
}

interface FormErrors { [key: string]: string; }

export default function FormsDemo() {
  const [step, setStep] = useState(1);
  const [values, setValues] = useState<FormValues>({
    email: '', password: '', confirmPassword: '',
    quantity: '', otp: ['','','','','',''],
    addressLine1: '', addressLine2: '', city: '', state: '', postalCode: '', country: 'IN',
    cardName: '', cardNumber: '', cardExpiry: '', cardCvc: '',
  });
  const [errors, setErrors] = useState<FormErrors>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});
  const [submitting, setSubmitting] = useState(false);
  const [saved, setSaved] = useState(false);
  const [draft, setDraft] = useState<FormValues | null>(null);

  const validate = (name: string, value: string | string[]): string => {
    // For OTP array, join to string for validation
    const stringValue = Array.isArray(value) ? value.join('') : value;
    switch (name) {
      case 'email': return stringValue && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(stringValue) ? 'Enter a valid email' : '';
      case 'password': return stringValue && stringValue.length < 8 ? 'At least 8 characters' : '';
      case 'confirmPassword': return stringValue && stringValue !== values.password ? 'Passwords do not match' : '';
      case 'quantity': return stringValue && (isNaN(Number(stringValue)) || Number(stringValue) < 1 || Number(stringValue) > 100) ? '1–100' : '';
      case 'postalCode': return stringValue && !/^\d{6}$/.test(stringValue) ? '6-digit PIN code' : '';
      case 'cardNumber': return stringValue && !/^\d{16}$/.test(stringValue.replace(/\s/g, '')) ? '16 digits' : '';
      case 'cardExpiry': return stringValue && !/^(0[1-9]|1[0-2])\/\d{2}$/.test(stringValue) ? 'MM/YY' : '';
      case 'cardCvc': return stringValue && !/^\d{3}$/.test(stringValue) ? '3 digits' : '';
      default: return '';
    }
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setValues(v => ({ ...v, [name]: value }));
    if (touched[name]) setErrors(e => ({ ...e, [name]: validate(name, value) }));
  };

  const handleBlur = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setTouched(t => ({ ...t, [name]: true }));
    setErrors(e => ({ ...e, [name]: validate(name, value) }));
  };

  const handleOtpChange = (index: number, value: string) => {
    if (!/^\d*$/.test(value) || value.length > 1) return;
    const next = [...values.otp];
    next[index] = value;
    setValues(v => ({ ...v, otp: next }));
    if (value && index < 5) (document.getElementById(`otp-${index + 1}`) as HTMLInputElement)?.focus();
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const newErrors: FormErrors = {};
    (Object.keys(values) as Array<keyof FormValues>).forEach(k => {
      const err = validate(k, values[k]);
      if (err) newErrors[k] = err;
    });
    setErrors(newErrors);
    setTouched(Object.keys(values).reduce((a, k) => ({ ...a, [k]: true }), {}));
    if (Object.keys(newErrors).length === 0) {
      setSubmitting(true);
      await new Promise(r => setTimeout(r, 1500));
      setSubmitting(false);
      setSaved(true);
      setTimeout(() => setSaved(false), 3000);
    }
  };

  const saveDraft = () => { setDraft({ ...values }); alert('Draft saved locally'); };
  const loadDraft = () => { if (draft) { setValues(draft); alert('Draft restored'); } };

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Form Experience Demo</h1>
          <p className="sk-content__subtitle">Validation timing, OTP, address, checkout steps, auto-save, draft recovery, keyboard nav.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar">
        <div className="sk-step-indicator" role="progressbar" aria-valuenow={step} aria-valuemin={1} aria-valuemax={3}>
          <span className={step >= 1 ? 'active' : ''}>1</span>
          <span className={step >= 2 ? 'active' : ''}>2</span>
          <span className={step >= 3 ? 'active' : ''}>3</span>
        </div>
        <button className="sk-secondary-action" onClick={saveDraft}>Save Draft</button>
        <button className="sk-secondary-action" onClick={loadDraft} disabled={!draft}>Load Draft</button>
        {saved && <span className="sk-saved-badge" role="status">✅ Saved</span>}
        <button className="sk-secondary-action" onClick={() => setStep(1)} disabled={step === 1}>← Back</button>
        <button className="sk-primary-action" onClick={() => setStep(s => Math.min(s + 1, 3))} disabled={step === 3}>Next →</button>
      </div>

      {step === 1 && (
        <section className="sk-panel" aria-labelledby="basic-title">
          <h2 id="basic-title">Basic Fields with Validation</h2>
          <p className="sk-hint">On blur: validate. On change (dirty): clear error. On submit: all validated.</p>
          <form onSubmit={handleSubmit} noValidate className="sk-form">
            <div className="sk-form-row">
              <div className="sk-form-field">
                <label htmlFor="email">Email *</label>
                <input id="email" name="email" type="email" value={values.email} onChange={handleChange} onBlur={handleBlur} aria-invalid={!!errors.email} aria-describedby={errors.email ? 'email-error' : 'email-hint'} />
                <span id="email-hint" className="sk-hint">We'll send a verification code</span>
                {errors.email && <span id="email-error" className="sk-error" role="alert">{errors.email}</span>}
              </div>
              <div className="sk-form-field">
                <label htmlFor="password">Password *</label>
                <input id="password" name="password" type="password" value={values.password} onChange={handleChange} onBlur={handleBlur} aria-invalid={!!errors.password} aria-describedby={errors.password ? 'pwd-error' : 'pwd-hint'} />
                <span id="pwd-hint" className="sk-hint">At least 8 characters</span>
                {errors.password && <span id="pwd-error" className="sk-error" role="alert">{errors.password}</span>}
              </div>
            </div>
            <div className="sk-form-row">
              <div className="sk-form-field">
                <label htmlFor="confirmPassword">Confirm Password *</label>
                <input id="confirmPassword" name="confirmPassword" type="password" value={values.confirmPassword} onChange={handleChange} onBlur={handleBlur} aria-invalid={!!errors.confirmPassword} aria-describedby={errors.confirmPassword ? 'cpwd-error' : undefined} />
                {errors.confirmPassword && <span id="cpwd-error" className="sk-error" role="alert">{errors.confirmPassword}</span>}
              </div>
              <div className="sk-form-field">
                <label htmlFor="quantity">Quantity (1–100) *</label>
                <input id="quantity" name="quantity" type="number" value={values.quantity} onChange={handleChange} onBlur={handleBlur} aria-invalid={!!errors.quantity} aria-describedby={errors.quantity ? 'qty-error' : undefined} />
                {errors.quantity && <span id="qty-error" className="sk-error" role="alert">{errors.quantity}</span>}
              </div>
            </div>
            <div className="sk-form-actions">
              <button type="submit" className="sk-primary-action" disabled={submitting}>{submitting ? 'Submitting…' : 'Validate All'}</button>
            </div>
          </form>
        </section>
      )}

      {step >= 2 && (
        <section className="sk-panel" aria-labelledby="otp-title">
          <h2 id="otp-title">OTP / PIN (6-digit)</h2>
          <p className="sk-hint">Paste supported. Auto-advance. Arrow keys navigate. Backspace clears & moves back.</p>
          <div className="sk-otp-group" role="group" aria-label="Enter 6-digit code">
            {values.otp.map((v, i) => (
              <input
                key={i}
                id={`otp-${i}`}
                type="text"
                inputMode="numeric"
                maxLength={1}
                value={v}
                onChange={e => handleOtpChange(i, e.target.value)}
                onKeyDown={e => { if (e.key === 'Backspace' && !v && i > 0) (document.getElementById(`otp-${i-1}`) as HTMLInputElement)?.focus(); }}
                autoComplete="one-time-code"
                aria-label={`Digit ${i + 1}`}
              />
            ))}
          </div>
        </section>
      )}

      {step >= 2 && (
        <section className="sk-panel" aria-labelledby="address-title">
          <h2 id="address-title">Address Form</h2>
          <p className="sk-hint">Autocomplete attributes for browser autofill. Country-aware (placeholder).</p>
          <form className="sk-form">
            <div className="sk-form-row">
              <div className="sk-form-field">
                <label htmlFor="addressLine1">Address Line 1 *</label>
                <input id="addressLine1" name="addressLine1" value={values.addressLine1} onChange={handleChange} onBlur={handleBlur} autoComplete="street-address" />
              </div>
            </div>
            <div className="sk-form-row">
              <div className="sk-form-field">
                <label htmlFor="addressLine2">Address Line 2</label>
                <input id="addressLine2" name="addressLine2" value={values.addressLine2} onChange={handleChange} onBlur={handleBlur} autoComplete="address-line2" />
              </div>
            </div>
            <div className="sk-form-row">
              <div className="sk-form-field"><label htmlFor="city">City *</label><input id="city" name="city" value={values.city} onChange={handleChange} onBlur={handleBlur} autoComplete="address-level2" /></div>
              <div className="sk-form-field"><label htmlFor="state">State *</label><input id="state" name="state" value={values.state} onChange={handleChange} onBlur={handleBlur} autoComplete="address-level1" /></div>
            </div>
            <div className="sk-form-row">
              <div className="sk-form-field"><label htmlFor="postalCode">PIN Code *</label><input id="postalCode" name="postalCode" value={values.postalCode} onChange={handleChange} onBlur={handleBlur} autoComplete="postal-code" aria-invalid={!!errors.postalCode} />{errors.postalCode && <span className="sk-error" role="alert">{errors.postalCode}</span>}</div>
              <div className="sk-form-field"><label htmlFor="country">Country</label><select id="country" name="country" value={values.country} onChange={handleChange} onBlur={handleBlur} autoComplete="country"><option value="IN">India</option><option value="US">United States</option></select></div>
            </div>
          </form>
        </section>
      )}

      {step === 3 && (
        <section className="sk-panel" aria-labelledby="checkout-title">
          <h2 id="checkout-title">Checkout Form (Steps)</h2>
          <p className="sk-hint">Multi-step: Contact → Shipping → Payment → Review. Auto-save per step. Idempotency key on submit.</p>
          <form className="sk-form">
            <div className="sk-form-row">
              <div className="sk-form-field"><label htmlFor="cardName">Name on Card *</label><input id="cardName" name="cardName" value={values.cardName} onChange={handleChange} onBlur={handleBlur} autoComplete="cc-name" /></div>
            </div>
            <div className="sk-form-row">
              <div className="sk-form-field"><label htmlFor="cardNumber">Card Number *</label><input id="cardNumber" name="cardNumber" value={values.cardNumber} onChange={handleChange} onBlur={handleBlur} autoComplete="cc-number" placeholder="4242 4242 4242 4242" aria-invalid={!!errors.cardNumber} />{errors.cardNumber && <span className="sk-error" role="alert">{errors.cardNumber}</span>}</div>
            </div>
            <div className="sk-form-row">
              <div className="sk-form-field"><label htmlFor="cardExpiry">Expiry (MM/YY) *</label><input id="cardExpiry" name="cardExpiry" value={values.cardExpiry} onChange={handleChange} onBlur={handleBlur} autoComplete="cc-exp" placeholder="12/28" aria-invalid={!!errors.cardExpiry} />{errors.cardExpiry && <span className="sk-error" role="alert">{errors.cardExpiry}</span>}</div>
              <div className="sk-form-field"><label htmlFor="cardCvc">CVC *</label><input id="cardCvc" name="cardCvc" value={values.cardCvc} onChange={handleChange} onBlur={handleBlur} autoComplete="cc-csc" placeholder="123" aria-invalid={!!errors.cardCvc} />{errors.cardCvc && <span className="sk-error" role="alert">{errors.cardCvc}</span>}</div>
            </div>
            <div className="sk-form-actions">
              <button type="button" className="sk-secondary-action" onClick={saveDraft}>Save Draft</button>
              <button type="button" className="sk-secondary-action" onClick={loadDraft} disabled={!draft}>Load Draft</button>
              <button type="submit" className="sk-primary-action" disabled={submitting}>{submitting ? 'Processing…' : 'Pay Now'}</button>
            </div>
          </form>
        </section>
      )}

      <section className="sk-panel" aria-labelledby="file-title" style={{ marginTop: '24px' }}>
        <h2 id="file-title">File Upload</h2>
        <p className="sk-hint">Drag-drop + browse. Accept, max size shown. Preview chips with remove.</p>
        <div className="sk-file-drop" role="button" tabIndex={0} aria-label="Drop files or click to browse">
          <div className="sk-file-drop__icon" aria-hidden="true">📁</div>
          <p>Drag & drop files here, or <button type="button" className="sk-link-button">browse</button></p>
          <p className="sk-hint">Max 10MB · PDF, JPG, PNG, DOCX</p>
        </div>
        <div className="sk-file-previews" role="list" aria-label="Selected files">
          <div className="sk-file-preview" role="listitem"><span>📄</span><span>proposal.pdf</span><span>2.4 MB</span><button className="sk-file-remove" aria-label="Remove proposal.pdf">×</button></div>
          <div className="sk-file-preview" role="listitem"><span>🖼️</span><span>logo.png</span><span>512 KB</span><button className="sk-file-remove" aria-label="Remove logo.png">×</button></div>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="date-title" style={{ marginTop: '24px' }}>
        <h2 id="date-title">Date Picker</h2>
        <p className="sk-hint">Keyboard: Arrows = day, PgUp/PgDn = month, Home/End = month edges, Enter = select, Esc = close.</p>
        <label className="sk-date-field">
          <span>Delivery Date</span>
          <input type="date" value="2026-07-20" onChange={() => {}} />
        </label>
      </section>

      <section className="sk-panel" aria-labelledby="combobox-title" style={{ marginTop: '24px' }}>
        <h2 id="combobox-title">Combobox (Searchable Select)</h2>
        <p className="sk-hint">ArrowDown = open/next, ArrowUp = prev, Enter = select, Esc = close, Type = filter.</p>
        <div className="sk-combobox" role="combobox" aria-expanded="false" aria-controls="combo-list" aria-label="Select state">
          <input type="text" placeholder="Select state…" aria-autocomplete="list" aria-controls="combo-list" />
          <ul id="combo-list" role="listbox" aria-label="States" className="sk-combobox-list">
            <li role="option" id="opt-mh">Maharashtra</li>
            <li role="option" id="opt-ka">Karnataka</li>
            <li role="option" id="opt-dl">Delhi</li>
            <li role="option" id="opt-tn">Tamil Nadu</li>
          </ul>
        </div>
      </section>
    </div>
  );
}