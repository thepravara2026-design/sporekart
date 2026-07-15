import { useNavigate } from 'react-router-dom';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Icon } from '../../../design-system/icons/Icon';
import { Widget } from '../components/Widget';
import { ProfileSummary, QuickActions, NotificationsPreview, SummaryWidget, AccountCompletion, RecommendedActions, RecentActivity, SupportShortcuts } from '../components/Widgets';
import '../../auth.css';
import './customer.css';

export default function DashboardPage() {
  const navigate = useNavigate();

  return (
    <div>
      <section className="cw-welcome" aria-labelledby="welcome-heading">
        <div className="cw-welcome__text">
          <h1 id="welcome-heading">Welcome back, Jane!</h1>
          <p>Your dashboard is ready. Check recent orders, continue training, or explore new cultivars.</p>
        </div>
        <div className="cw-welcome__cta">
          <button
            type="button"
            onClick={() => navigate('/dashboard/products')}
            className="cw-btn cw-btn--primary cw-btn--lg"
          >
            Browse Products
            <Icon name="arrow-right" size={16} color="currentColor" style={{ marginLeft: 'var(--space-inline-xs)' }} />
          </button>
        </div>
      </section>

      <Grid className="cw-grid" columns="auto-fit" minColumnWidth="340px">
        <Widget
          title="Profile Summary"
          subtitle="Account overview & status"
          icon={<Icon name="user" size={18} color="currentColor" />}
          footer={
            <button type="button" className="cw-link" onClick={() => navigate('/dashboard/profile')}>
              Edit profile
            </button>
          }
        >
          <ProfileSummary />
        </Widget>

        <Widget
          title="Quick Actions"
          subtitle="Common tasks at a glance"
          icon={<Icon name="zap" size={18} color="currentColor" />}
        >
          <QuickActions />
        </Widget>

        <Widget
          title="Notifications"
          subtitle="3 unread"
          icon={<Icon name="bell" size={18} color="currentColor" />}
          footer={
            <button type="button" className="cw-link" onClick={() => navigate('/dashboard/notifications')}>
              View all
            </button>
          }
        >
          <NotificationsPreview />
        </Widget>

        <Widget
          title="Order Summary"
          subtitle="Recent activity"
          icon={<Icon name="shopping-bag" size={18} color="currentColor" />}
        >
          <SummaryWidget
            stats={[
              { label: 'Active Orders', value: '2', trend: '+1 this week' },
              { label: 'Total Spend', value: '$1,240', trend: '+12% MoM' },
              { label: 'Pending Shipments', value: '1' },
            ]}
          />
        </Widget>

        <Widget
          title="Training Progress"
          subtitle="Certifications & modules"
          icon={<Icon name="book-open" size={18} color="currentColor" />}
        >
          <SummaryWidget
            stats={[
              { label: 'Completed Modules', value: '7/12', trend: '+2 this month' },
              { label: 'Certificates', value: '3', trend: '+1' },
              { label: 'In Progress', value: '1' },
            ]}
          />
        </Widget>

        <Widget
          title="Wishlist"
          subtitle="5 items saved"
          icon={<Icon name="heart" size={18} color="currentColor" />}
        >
          <SummaryWidget
            stats={[
              { label: 'Saved Items', value: '5' },
              { label: 'On Sale', value: '2', trend: 'Limited time' },
              { label: 'Back in Stock', value: '1' },
            ]}
          />
        </Widget>

        <Widget
          title="Account Completion"
          subtitle="Finish setup for full access"
          icon={<Icon name="shield" size={18} color="currentColor" />}
        >
          <AccountCompletion />
        </Widget>

        <Widget
          title="Recommended Actions"
          subtitle="Personalized next steps"
          icon={<Icon name="sparkles" size={18} color="currentColor" />}
        >
          <RecommendedActions />
        </Widget>

        <Widget
          title="Recent Activity"
          subtitle="Your latest actions"
          icon={<Icon name="clock" size={18} color="currentColor" />}
        >
          <RecentActivity />
        </Widget>

        <Widget
          title="Support Shortcuts"
          subtitle="Quick help access"
          icon={<Icon name="help-circle" size={18} color="currentColor" />}
        >
          <SupportShortcuts />
        </Widget>
      </Grid>
    </div>
  );
}