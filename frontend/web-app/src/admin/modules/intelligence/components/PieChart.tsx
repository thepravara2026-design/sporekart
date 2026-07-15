import { memo } from 'react';
import { DonutChart } from './DonutChart';

interface PieChartProps {
  data: { label: string; value: number; color: string }[];
  size?: number;
}

export const PieChart = memo(function PieChart(props: PieChartProps) {
  return <DonutChart {...props} holeSize={0} />;
});
