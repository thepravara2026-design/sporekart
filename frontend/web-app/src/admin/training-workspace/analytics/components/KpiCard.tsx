import { memo } from 'react';
import type { ReactNode } from 'react';
import { MetricTile } from '../../../../design-system/components/charts';

export interface KpiCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon?: ReactNode;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  color?: string;
  loading?: boolean;
  onClick?: () => void;
}

const KpiCard = memo(function KpiCard(props: KpiCardProps) {
  return (
    <MetricTile
      title={props.title}
      value={props.value}
      subtitle={props.subtitle}
      icon={props.icon}
      trend={props.trend}
      trendValue={props.trendValue}
      color={props.color}
      size="md"
      loading={props.loading}
      onClick={props.onClick}
    />
  );
});

export default KpiCard;
