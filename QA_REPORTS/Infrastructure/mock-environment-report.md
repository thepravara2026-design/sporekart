# Mock Environment Report

**Pre-QA Sprint 2** | **Date:** 2026-07-17

---

## Isolation Verification

| Resource | Production | Mock | Status |
|----------|-----------|------|--------|
| **Database** | MongoDB Atlas | `mongodb://localhost:27017/sporekart-mock` | ✅ Isolated |
| **Payment** | Razorpay Live | Mock Razorpay keys (`rzp_mock_test_key`) | ✅ Isolated |
| **OTP** | Real SMS gateway | Any 6-digit code except 000000 accepted | ✅ Isolated |
| **Email** | SendGrid/SES | `MOCK_EMAIL_ENABLED=true`, mock from address | ✅ Isolated |
| **SMS** | Real SMS provider | `MOCK_SMS_PROVIDER=mock`, no real SMS sent | ✅ Isolated |
| **Shipping** | Shiprocket Live | Mock Shiprocket key (`mock_shiprocket_key`) | ✅ Isolated |
| **APIs** | Production endpoints | 16 mock service URLs on localhost | ✅ Isolated |
| **Auth** | Supabase/real auth | `MOCK_AUTH_ENABLED=true`, mock token | ✅ Isolated |
| **Storage** | Cloud storage | `MOCK_STORAGE_PROVIDER=local` | ✅ Isolated |
| **Analytics** | Production analytics | `MOCK_ANALYTICS_PROVIDER=local` | ✅ Isolated |

## Feature Flags

| Flag | Value | Purpose |
|------|-------|---------|
| `FF_MOCK_MODE` | `true` | Master mock mode switch |
| `FF_PRODUCTION_MODE` | `false` | Production disabled |
| `FF_PAYMENT_GATEWAY` | `mock` | Mock payments |
| `FF_SHIPPING_PROVIDER` | `mock` | Mock shipping |
| `FF_EMAIL_PROVIDER` | `mock` | Mock email |
| `FF_SMS_PROVIDER` | `mock` | Mock SMS |
| `FF_AUTH_PROVIDER` | `mock` | Mock authentication |
| `FF_STORAGE_PROVIDER` | `local` | Local storage |

## QA Configuration

| Setting | Value |
|---------|-------|
| `QA_MODE` | `true` |
| `QA_TEST_TIMEOUT` | `30000` |
| `QA_RETRIES` | `2` |
| `QA_VIDEO_RECORDING` | `on` |
| `QA_TRACE_RECORDING` | `on` |
| `QA_SCREENSHOT_MODE` | `on` |

## Risk Assessment

| Risk | Impact | Mitigation |
|------|--------|------------|
| Mock DB URL points to local MongoDB | Tests pass without MongoDB since mock data is client-side only | Verified — no DB dependency for frontend tests |
| Mock auth accepts any OTP | Acceptable for QA — tests validate UI behavior, not crypto | Documented as known mock limitation |
| Some .env.example files contain realistic-looking keys | Low risk (example files only) | Flag for review before production |

**Verdict:** Mock environment is fully isolated. No production services will be touched during QA Sprint 2.
