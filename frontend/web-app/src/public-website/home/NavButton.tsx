import { useNavigate } from 'react-router-dom';
import { Button, type ButtonProps } from '../../design-system/components/core/Button';

export function NavButton({ to, ...props }: ButtonProps & { to: string }) {
  const navigate = useNavigate();
  return <Button {...props} onClick={() => navigate(to)} />;
}
