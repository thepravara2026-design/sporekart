# Search & Command Palette Foundation — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Architecture only. This sprint defines the *foundation* (data
> model, triggers, scopes, keyboard map). Implementation belongs to later sprints.
> The prototype wires the shell (open/close, focus trap, static command list) so
> the interaction is reviewable, but no real search/index backend is built.

---

## 1. Components

### 1.1 Global Search
- **Trigger:** header search field + `Cmd/Ctrl+K` focuses it.
- **Scope:** across workspaces the user can access (role-filtered).
- **Results:** navigation targets, records (orders, products, trainings), and
  help articles. Results are grouped by type.
- **Future:** fuzzy match, recency boost, typo tolerance, semantic/AI suggestions.

### 1.2 Command Palette
- **Trigger:** `Cmd/Ctrl+K` (or header search submit when empty).
- **Modes:** *Navigate* (jump to workspace/section), *Actions* (quick actions),
  *Recent* (recent items), *Favorites* (pinned).
- **Behavior:** modal dialog, focus trap, Arrow/Enter/Esc, restores focus on close.
- **Future:** command history, parameterised commands, plugin commands.

### 1.3 Quick Actions
- High-frequency tasks available anywhere: New Order, New Product, New Training,
  Ask AI, Raise Ticket. Surfaced via header (+) and the palette's Actions mode.

### 1.4 Recent Items
- Last N visited records (order, product, training) per user; shown in palette
  Recent mode and as a "Continue" strip on relevant home pages (later sprints).

### 1.5 Favorites / Pinned Pages
- User-pinnable workspace sections; rendered as a compact "Pinned" group at the
  top of the sidebar (later sprints). Palette Favorites mode lists them.

---

## 2. Keyboard Shortcuts (foundation map)

| Shortcut | Action |
|----------|--------|
| `Cmd/Ctrl + K` | Open command palette / focus search |
| `Esc` | Close palette / dismiss overlay |
| `/` | Focus global search (when not in input) |
| `g` then `o` | Go to Orders (sequence shortcuts, future) |
| `?` | Open keyboard help (future) |
| `j` / `k` | Move down/up list (palette + lists, future) |

(Many are deferred to implementation sprints; the map is declared now so the
shell reserves them.)

---

## 3. Data Model (for later implementation)

```ts
type SearchEntity =
  | { kind: 'nav';     route: string; title: string; workspace: string }
  | { kind: 'order';   id: string;   ref: string;  route: string }
  | { kind: 'product'; id: string;   name: string; route: string }
  | { kind: 'training';id: string;   title: string;route: string }
  | { kind: 'help';    id: string;   title: string; route: string };

interface Command {
  id: string;
  label: string;
  group: 'Navigate' | 'Actions' | 'Recent' | 'Favorites';
  run: () => void;       // navigation or action
  roles?: string[];      // visibility
}
```

---

## 4. Prototype Scope
- Command palette shell opens/closes, traps focus, lists **static** navigate +
  quick-action commands derived from `navigation.ts`.
- No indexing, no network, no persistence. This proves the interaction and the
  role-filtered command set; real search is a later sprint.
