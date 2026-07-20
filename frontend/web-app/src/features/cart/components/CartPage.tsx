import { useCart } from '../CartContext';
import CartDrawer from './CartDrawer';
import type { CartItem } from '../types';

function CartItemRow({ item, onRemove, onUpdateQty }: {
  item: CartItem;
  onRemove: () => void;
  onUpdateQty: (qty: number) => void;
}) {
  return (
    <tr>
      <td style={{ padding: '12px 8px' }}>{item.name}</td>
      <td style={{ padding: '12px 8px' }}>${item.price.toFixed(2)}</td>
      <td style={{ padding: '12px 8px' }}>
        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <button type="button" onClick={() => onUpdateQty(item.quantity - 1)} style={{ padding: '4px 8px', cursor: 'pointer' }}>-</button>
          <span>{item.quantity}</span>
          <button type="button" onClick={() => onUpdateQty(item.quantity + 1)} style={{ padding: '4px 8px', cursor: 'pointer' }}>+</button>
        </div>
      </td>
      <td style={{ padding: '12px 8px', fontWeight: 600 }}>
        ${(item.price * item.quantity).toFixed(2)}
      </td>
      <td style={{ padding: '12px 8px' }}>
        <button type="button" onClick={onRemove} style={{ padding: '4px 8px', cursor: 'pointer', color: 'var(--color-danger)' }}>Remove</button>
      </td>
    </tr>
  );
}

export default function CartPage() {
  const { state, count: _count, total, removeItem, updateQuantity, clearCart } = useCart();

  return (
    <div style={{ maxWidth: 800, margin: '0 auto', padding: '24px 16px' }}>
      <CartDrawer />
      <h1 style={{ margin: '0 0 24px', fontSize: 24, fontWeight: 700 }}>Shopping Cart</h1>

      {state.items.length === 0 ? (
        <div style={{ textAlign: 'center', padding: 60, color: 'var(--color-text-secondary)' }}>
          <p>Your cart is empty</p>
          <a href="/products" style={{ color: 'var(--color-primary)' }}>Continue shopping</a>
        </div>
      ) : (
        <>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '2px solid var(--color-border-weak)' }}>
                <th style={{ textAlign: 'left', padding: '12px 8px' }}>Product</th>
                <th style={{ textAlign: 'left', padding: '12px 8px' }}>Price</th>
                <th style={{ textAlign: 'left', padding: '12px 8px' }}>Quantity</th>
                <th style={{ textAlign: 'left', padding: '12px 8px' }}>Subtotal</th>
                <th style={{ width: 80 }}></th>
              </tr>
            </thead>
            <tbody>
              {state.items.map((item) => (
                <CartItemRow
                  key={item.id}
                  item={item}
                  onRemove={() => removeItem(item.id)}
                  onUpdateQty={(qty) => updateQuantity(item.id, qty)}
                />
              ))}
            </tbody>
          </table>

          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 24, paddingTop: 24, borderTop: '2px solid var(--color-border-weak)' }}>
            <button type="button" onClick={clearCart} style={{ padding: '8px 16px', cursor: 'pointer' }}>Clear Cart</button>
            <div style={{ textAlign: 'right' }}>
              <div style={{ fontSize: 20, fontWeight: 700, marginBottom: 12 }}>Total: ${total.toFixed(2)}</div>
              <a href="/checkout" style={{
                display: 'inline-block',
                padding: '12px 32px',
                background: 'var(--color-primary)',
                color: '#fff',
                textDecoration: 'none',
                borderRadius: 6,
                fontWeight: 600,
              }}>Proceed to Checkout</a>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
