import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';
import {
  getCartState,
  getCartCount,
  getCartTotal,
  dispatchCart,
  subscribeCart,
} from './CartStore';
import type { CartItem, CartAction, CartState } from './types';

interface CartContextValue {
  state: CartState;
  count: number;
  total: number;
  dispatch: (action: CartAction) => void;
  addItem: (item: CartItem) => void;
  removeItem: (id: string) => void;
  updateQuantity: (id: string, quantity: number) => void;
  clearCart: () => void;
  toggleCart: () => void;
  setOpen: (open: boolean) => void;
}

const CartContext = createContext<CartContextValue | null>(null);

export function CartProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState(getCartState());
  const [count, setCount] = useState(getCartCount());
  const [total, setTotal] = useState(getCartTotal());

  useEffect(() => {
    const unsub = subscribeCart(() => {
      setState(getCartState());
      setCount(getCartCount());
      setTotal(getCartTotal());
    });
    return unsub;
  }, []);

  const value: CartContextValue = {
    state,
    count,
    total,
    dispatch: dispatchCart,
    addItem: (item) => dispatchCart({ type: 'ADD_ITEM', payload: item }),
    removeItem: (id) => dispatchCart({ type: 'REMOVE_ITEM', payload: id }),
    updateQuantity: (id, quantity) =>
      dispatchCart({ type: 'UPDATE_QUANTITY', payload: { id, quantity } }),
    clearCart: () => dispatchCart({ type: 'CLEAR_CART' }),
    toggleCart: () => dispatchCart({ type: 'TOGGLE_CART' }),
    setOpen: (open) => dispatchCart({ type: 'SET_OPEN', payload: open }),
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart(): CartContextValue {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart must be used within CartProvider');
  return ctx;
}
