import { useLocation } from 'react-router-dom';
import { PUBLIC_WEBSITE_ROUTES } from '../config';
import { PublicRoutePlaceholder } from './PublicRoutePlaceholder';

export default function PublicRoutePage() {
  const { pathname } = useLocation();
  const route = PUBLIC_WEBSITE_ROUTES.find((item) => item.path === pathname) ?? PUBLIC_WEBSITE_ROUTES[0];
  return <PublicRoutePlaceholder route={route} />;
}
