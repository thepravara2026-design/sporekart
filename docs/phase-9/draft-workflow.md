# Draft Workflow

> Companion to [sprint-24-part-3.md](./sprint-24-part-3.md) and [product-creation-wizard.md](./product-creation-wizard.md). Code: `src/admin/modules/products/creation/useWizardState.ts`. Mock Mode.

## 1. Overview

`useWizardState` is the single owner of wizard + draft state. It exposes the live `draft`, a typed `update()` patcher, validation errors, navigation helpers, and the draft persistence lifecycle. Persistence is **mock-only** (`sessionStorage`).

> **Persistence is mock-only.** There is **no backend, no API** in this sprint. A real create/update mutation is explicitly **Phase 1** — see the hand-off note in §6.

## 2. Key scheme

| Key | Contents |
|-----|----------|
| `sporekart:product:draft:v1` | The full `Draft` JSON (all 7 steps' authoring data). |
| `sporekart:product:draft:meta:v1` | `{ savedAt: string; step: number; dirty: boolean }` — last-saved timestamp, resume step index, dirty flag. |

Keys are namespaced (`sporekart:product:`) and versioned (`v1`) so a future schema bump can coexist without clobbering old drafts.

## 3. Lifecycle methods

```ts
// src/admin/modules/products/creation/useWizardState.ts (public surface)
export function useWizardState() {
  return {
    draft: Draft,                       // live authoring data
    draftMeta: { savedAt: string|null; step: number; dirty: boolean },
    errors: FieldErrors,                // validateStep(active)
    visited: Set<WizardStepId>,         // free-navigation tracking

    update: (patch: Partial<Draft>) => void,   // patches draft, sets dirty=true
    resume: () => void,                        // hydrate from sessionStorage on mount
    saveDraft: () => void,                     // write draft + meta, dirty=false, savedAt=now
    discard: () => void,                       // clear keys, reset state, dirty=false

    goNext: () => void,                 // validateStep(active); advance if clean
    goBack: () => void,                 // step--
    stepValid: (i: number) => boolean,  // validateStep(STEP_ORDER[i], draft) is empty
  };
}
```

| Method | Behaviour |
|--------|-----------|
| `update(patch)` | Shallow-merges `patch` into `draft` (deep-merges nested `pricing`/`seo` via helper). Flips `dirty = true`. Triggers preview re-render. |
| `resume()` | On mount, reads both keys; if present, hydrates `draft`, sets `step = meta.step`, marks steps `visited`. If absent, renders `states/DraftMissingState.tsx` when a resume was explicitly requested. |
| `saveDraft()` | Serialises `draft` → `sporekart:product:draft:v1`; writes `meta` with `savedAt = new Date().toISOString()`, `dirty = false`. Updates `DraftIndicator`. |
| `discard()` | Removes both keys, resets `draft` to `initialDraft()`, `step = 0`, `dirty = false`, `visited = { basic }`. Confirmed via `Dialog`. |
| `goNext()` / `goBack()` | Move `step`; add to `visited`; recompute `errors` via `validateStep`. |

## 4. Unsaved-changes detection

- Every `update()` sets `draftMeta.dirty = true`.
- `DraftIndicator.tsx` reads `draftMeta` and shows:
  - **Unsaved** (amber `StatusBadge`) when `dirty === true`.
  - **Saved · HH:MM** (neutral `StatusBadge`) when `dirty === false` and `savedAt` exists.
  - **No draft** when neither key exists.
- A `beforeunload` listener (registered by `ProductCreationWizard`) warns the user on tab close while `dirty` is true.

```tsx
// src/admin/modules/products/creation/DraftIndicator.tsx (excerpt)
<StatusBadge
  status={meta.dirty ? 'Unsaved changes' : (meta.savedAt ? `Saved · ${fmtTime(meta.savedAt)}` : 'No draft')}
  variant={meta.dirty ? 'warning' : 'neutral'}
/>
```

## 5. Auto-save (future placeholder — not yet implemented)

Auto-save is documented as a **future placeholder** and is **not wired** in this sprint. Manual *Save draft* (`sessionStorage`) remains the only persistence path today:

```ts
// Future (Phase 1): replace with a debounced saveDraft() on draft change
// autoSave is intentionally NOT implemented in Mock Mode.
```

This keeps the integration point visible so Phase 1 only adds a real debounced call — no step/UI change required.

## 6. Phase 1 hand-off (real backend)

> Not implemented this sprint. Documented contract only.

1. Introduce `creation/api.ts`: `saveDraft(product)`, `loadDraft(id)`, `discardDraft(id)`, `createProduct(draft)`.
2. Keep `useWizardState`'s public surface identical; internally swap `sessionStorage` calls for API calls.
3. `ConfirmationStep` reads the server id from `createProduct` instead of the client generator.
4. Wire auto-save (currently a future placeholder) as a debounced `saveDraft`.

Because steps and validation depend only on `draft`/`update`/`errors`, **no step component or validator needs to change** when the backend lands.

(End of file)
