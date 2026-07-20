import type { CartItem, CartState, CartAction } from './types';

const STORAGE_KEY = 'sk_cart';

function loadCart(): CartItem[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) return JSON.parse(stored);
  } catch {}
  return [];
}

function saveCart(items: CartItem[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
  } catch {}
}

let _state: CartState = { items: loadCart(), isOpen: false };
let _listeners: Array<() => void> = [];

function notify(): void {
  _listeners.forEach((l) => l());
}

export function getCartState(): CartState {
  return { ..._state, items: [..._state.items] };
}

export function getCartCount(): number {
  return _state.items.reduce((sum, item) => sum + item.quantity, 0);
}

export function getCartTotal(): number {
  return _state.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
}

export function dispatchCart(action: CartAction): void {
  switch (action.type) {
    case 'ADD_ITEM': {
      const existing = _state.items.find(
        (i) => i.productId === action.payload.productId,
      );
      if (existing) {
        _state.items = _state.items.map((i) =>
          i.productId === action.payload.productId
            ? { ...i, quantity: i.quantity + action.payload.quantity }
            : i,
        );
      } else {
        _state.items = [..._state.items, action.payload];
      }
      break;
    }
    case 'REMOVE_ITEM':
      _state.items = _state.items.filter((i) => i.id !== action.payload);
      break;
    case 'UPDATE_QUANTITY':
      _state.items = _state.items.map((i) =>
        i.id === action.payload.id
          ? { ...i, quantity: Math.max(0, action.payload.quantity) }
          : i,
      );
      _state.items = _state.items.filter((i) => i.quantity > 0);
      break;
    case 'CLEAR_CART':
      _state.items = [];
      break;
    case 'TOGGLE_CART':
      _state.isOpen = !_state.isOpen;
      break;
    case 'SET_OPEN':
      _state.isOpen = action.payload;
      break;
  }
  saveCart(_state.items);
  notify();
}

export function subscribeCart(listener: () => void): () => void {
  _listeners.push(listener);
  return () => {
    _listeners = _listeners.filter((l) => l !== listener);
  };
}
