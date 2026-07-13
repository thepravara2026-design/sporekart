import React from 'react';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

import { CalendarMonth } from '../../components/charts/calendar/CalendarMonth';
import { CalendarWeek } from '../../components/charts/calendar/CalendarWeek';
import { CalendarAgenda } from '../../components/charts/calendar/CalendarAgenda';
import { CalendarDateRange } from '../../components/charts/calendar/CalendarDateRange';

const today = new Date();
const tomorrow = new Date(today); tomorrow.setDate(today.getDate() + 1);
const dayAfter = new Date(today); dayAfter.setDate(today.getDate() + 2);
const nextWeek = new Date(today); nextWeek.setDate(today.getDate() + 7);
const lastWeek = new Date(today); lastWeek.setDate(today.getDate() - 7);

const sampleEvents = [
  { date: today, label: 'Team Standup', color: 'var(--color-bg-primary-default)' },
  { date: today, label: 'Lunch with Client', color: 'var(--color-data-viz-3)' },
  { date: tomorrow, label: 'Sprint Planning', color: 'var(--color-success-500)' },
  { date: tomorrow, label: 'Code Review', color: 'var(--color-data-viz-4)' },
  { date: dayAfter, label: 'Design Workshop', color: 'var(--color-warning-500)' },
  { date: nextWeek, label: 'Quarterly Review', color: 'var(--color-danger-500)' },
];

const weekEvents = [
  { date: today, label: 'Standup', startTime: '09:00', endTime: '09:30', color: 'var(--color-bg-primary-default)' },
  { date: today, label: 'Design Sync', startTime: '14:00', endTime: '15:00', color: 'var(--color-data-viz-3)' },
  { date: tomorrow, label: 'Sprint Planning', startTime: '10:00', endTime: '12:00', color: 'var(--color-success-500)' },
  { date: tomorrow, label: 'Lunch', startTime: '12:00', endTime: '13:00', color: 'var(--color-neutral-300)' },
  { date: dayAfter, label: 'Workshop', startTime: '13:00', endTime: '16:00', color: 'var(--color-warning-500)' },
];

const agendaEvents = [
  { date: today, label: 'Morning Standup', description: 'Daily sync with engineering team', time: '9:00 AM', color: 'var(--color-bg-primary-default)' },
  { date: today, label: 'Client Meeting', description: 'Q2 review with stakeholders', time: '2:00 PM', color: 'var(--color-data-viz-3)' },
  { date: tomorrow, label: 'Sprint Planning', description: 'Plan stories for sprint 12', time: '10:00 AM', color: 'var(--color-success-500)' },
  { date: tomorrow, label: 'Lunch & Learn', description: 'Intro to GraphQL', time: '12:00 PM', color: 'var(--color-warning-500)' },
  { date: dayAfter, label: 'Design Review', description: 'Review new dashboard mockups', time: '3:00 PM', color: 'var(--color-data-viz-4)' },
  { date: nextWeek, label: 'Quarterly Review', description: 'Executive presentation', time: '11:00 AM', color: 'var(--color-danger-500)' },
  { date: lastWeek, label: 'Past Event', description: 'This should not appear if filtered', time: '1:00 PM', color: 'var(--color-neutral-300)' },
];

export default function CalendarsPreview() {
  const [selectedDate, setSelectedDate] = React.useState<Date | undefined>(today);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Calendars</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Calendar month, week, agenda, and date range picker</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>CalendarMonth</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '16px' }}>
          <StateCard label="With navigation, events, and date selection">
            <CalendarMonth
              selectedDate={selectedDate}
              onDateSelect={(d) => setSelectedDate(d)}
              events={sampleEvents}
            />
          </StateCard>
          <StateCard label="Selected date indicator">
            <CalendarMonth
              year={today.getFullYear()}
              month={today.getMonth()}
              selectedDate={selectedDate}
              events={sampleEvents.slice(0, 2)}
            />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>CalendarWeek</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(600px, 1fr))', gap: '16px' }}>
          <StateCard label="Weekly view with time slots">
            <CalendarWeek
              year={today.getFullYear()}
              month={today.getMonth()}
              events={weekEvents}
            />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>CalendarAgenda</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '16px' }}>
          <StateCard label="Grouped agenda view">
            <CalendarAgenda
              events={agendaEvents}
              startDate={lastWeek}
              endDate={nextWeek}
            />
          </StateCard>
          <StateCard label="Limited items (max 3)">
            <CalendarAgenda
              events={agendaEvents}
              startDate={today}
              endDate={nextWeek}
              maxItems={3}
            />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>CalendarDateRange</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(500px, 1fr))', gap: '16px' }}>
          <StateCard label="Range selection with 2 months">
            <CalendarDateRange
              startDate={lastWeek}
              endDate={nextWeek}
              numberOfMonths={2}
            />
          </StateCard>
        </div>
      </section>
    </div>
  );
}
