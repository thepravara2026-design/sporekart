import { useCart } from '../CartContext';
import { Icon } from '../../../design-system/icons/Icon';
import type { CartItem } from '../types';

function CartItemRow({ item, onRemove, onUpdateQty }: {
  item: CartItem;
  onRemove: () => void;
  onUpdateQty: (qty: number) => void;
}) {
  return (
    <div style={{ display: 'flex', gap: 12, padding: '12px 0', borderBottom: '1px solid var(--color-border-weak)' }}>
      <div style={{ flex: 1 }}>
        <div style={{ fontWeight: 600, fontSize: 14 }}>{item.name}</div>
        <div style={{ fontSize: 12, color: 'var(--color-text-secondary)' }}>
          ${item.price.toFixed(2)} × {item.quantity}
        </div>
        <div style={{ fontSize: 14, fontWeight: 600, marginTop: 4 }}>
          ${(item.price * item.quantity).toFixed(2)}
        </div>
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4, alignItems: 'center' }}>
        <button
          type="button"
          style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4 }}
          onClick={() => onUpdateQty(item.quantity + 1)}
          aria-label={`Increase quantity of ${item.name}`}
        >
          <Icon name="plus" size={16} color="currentColor" />
        </button>
        <span style={{ fontSize: 14 }}>{item.quantity}</span>
        <button
          type="button"
          style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4 }}
          onClick={() => onUpdateQty(item.quantity - 1)}
          aria-label={`Decrease quantity of ${item.name}`}
        >
          <Icon name="minus" size={16} color="currentColor" />
        </button>
        <button
          type="button"
          style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4, color: 'var(--color-danger)' }}
          onClick={onRemove}
          aria-label={`Remove ${item.name} from cart`}
        >
          <Icon name="trash-2" size={16} color="currentColor" />
        </button>
      </div>
    </div>
  );
}

export default function CartDrawer() {
  const { state, count, total, removeItem, updateQuantity, clearCart, setOpen } = useCart();

  if (!state.isOpen) return null;

  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        zIndex: 1000,
        display: 'flex',
        justifyContent: 'flex-end',
      }}
    >
      <div
        style={{ position: 'absolute', inset: 0, background: 'rgba(0,0,0,0.3)' }}
        onClick={() => setOpen(false)}
        aria-hidden="true"
      />
      <div
        role="dialog"
        aria-label="Shopping cart"
        style={{
          position: 'relative',
          width: 400,
          maxWidth: '100vw',
          height: '100%',
          background: 'var(--color-bg-surface-default)',
          boxShadow: '-4px 0 24px rgba(0,0,0,0.1)',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <div style={{ padding: '16px 20px', borderBottom: '1px solid var(--color-border-weak)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2 style={{ margin: 0, fontSize: 18, fontWeight: 600 }}>
            Cart ({count} items)
          </h2>
          <button
            type="button"
            onClick={() => setOpen(false)}
            style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4 }}
            aria-label="Close cart"
          >
            <Icon name="x" size={20} color="currentColor" />
          </button>
        </div>

        <div style={{ flex: 1, overflow: 'auto', padding: '0 20px' }}>
          {state.items.length === 0 ? (
            <div style={{ textAlign: 'center', padding: 40, color: 'var(--color-text-secondary)' }}>
              Your cart is empty
            </div>
          ) : (
            state.items.map((item) => (
              <CartItemRow
                key={item.id}
                item={item}
                onRemove={() => removeItem(item.id)}
                onUpdateQty={(qty) => updateQuantity(item.id, qty)}
              />
            ))
          )}
        </div>

        {state.items.length > 0 && (
          <div style={{ padding: '16px 20px', borderTop: '1px solid var(--color-border-weak)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 12 }}>
              <span style={{ fontWeight: 600 }}>Total</span>
              <span style={{ fontWeight: 600, fontSize: 18 }}>${total.toFixed(2)}</span>
            </div>
            <div style={{ display: 'flex', gap: 8 }}>
              <button
                type="button"
                onClick={clearCart}
                style={{
                  flex: 1,
                  padding: '10px',
                  border: '1px solid var(--color-border-default)',
                  borderRadius: 6,
                  background: 'transparent',
                  cursor: 'pointer',
                }}
              >
                Clear
              </button>
              <a
                href="/checkout"
                style={{
                  flex: 2,
                  padding: '10px',
                  border: 'none',
                  borderRadius: 6,
                  background: 'var(--color-primary)',
                  color: '#fff',
                  textAlign: 'center',
                  textDecoration: 'none',
                  fontWeight: 600,
                  cursor: 'pointer',
                  display: 'block',
                }}
                onClick={() => setOpen(false)}
              >
                Checkout
              </a>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
