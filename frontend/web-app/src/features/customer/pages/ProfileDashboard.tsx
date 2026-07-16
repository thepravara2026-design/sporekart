import { ProfileCard } from '../../../design-system/components/composite/ProfileCard';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import { LinearProgress } from '../../../design-system/components/feedback/LinearProgress';
import { useNavigate } from 'react-router-dom';
import '../profile.css';

const PROFILE_STATS = [
  { label: 'Account Status', value: 'Active', status: 'success' },
  { label: 'Verification Level', value: 'Full', status: 'success' },
  { label: 'Account Tier', value: 'Premium', status: 'default' },
  { label: 'Member Since', value: 'Jan 2024', status: 'default' },
];

export default function ProfileDashboard() {
  const navigate = useNavigate();
  const completionPercentage = 85;
  const recentActivity = [
    { action: 'Updated email address', time: '2 hours ago', icon: <Icon name="mail" size={14} color="currentColor" /> },
    { action: 'Added delivery address', time: '1 day ago', icon: <Icon name="map-pin" size={14} color="currentColor" /> },
    { action: 'Verified phone number', time: '3 days ago', icon: <Icon name="check-circle" size={14} color="currentColor" /> },
  ];

  return (
    <div className="sk-profile-page">
      <section className="sk-profile-header">
        <div className="sk-profile-header__content">
          <ProfileCard
            name="Jane Growell"
            role="Premium Customer"
            avatar="https://images.unsplash.com/photo-1494790108755-2616b332c300?w=150&h=150&fit=crop"
            status="online"
          />
          <div className="sk-profile-header__quick-stats">
            {PROFILE_STATS.map((stat, i) => (
              <div key={i} className="sk-profile-stat">
                <span className="sk-profile-stat__label">{stat.label}</span>
                <span className={`sk-profile-stat__value sk-profile-stat__value--${stat.status}`}>{stat.value}</span>
              </div>
            ))}
          </div>
        </div>
        <div className="sk-profile-header__actions">
          <Button variant="primary" onClick={() => navigate('/profile/edit')} size="lg">
            <Icon name="edit" size={18} color="currentColor" style={{ marginRight: 'var(--space-inline-xs)' }} />
            Edit Profile
          </Button>
        </div>
      </section>

      <div className="sk-profile-content">
        <section className="sk-profile-completion">
          <Card variant="default" padding="lg">
            <h2 className="sk-profile-section-title">Profile Completion</h2>
            <div className="sk-completion-wrapper">
              <div className="sk-completion-progress">
                <LinearProgress value={completionPercentage} size="lg" color="primary" />
                <span className="sk-completion-percentage">{completionPercentage}%</span>
              </div>
              <div className="sk-completion-details">
                <div className="sk-completion-missing">
                  <h3>Missing Information</h3>
                  <ul>
                    <li>Upload profile photo</li>
                    <li>Add payment method</li>
                    <li>Set communication preferences</li>
                  </ul>
                </div>
                <div className="sk-completion-benefits">
                  <h3>Complete Profile Benefits</h3>
                  <ul>
                    <li>Skip checkout on next order</li>
                    <li>Access to exclusive cultivars</li>
                    <li>Priority customer support</li>
                  </ul>
                </div>
              </div>
            </div>
          </Card>
        </section>

        <section className="sk-profile-recent-activity">
          <Card variant="default" padding="lg">
            <div className="sk-section-header">
              <h2 className="sk-profile-section-title">Recent Activity</h2>
              <Button variant="ghost" size="sm" onClick={() => navigate('/profile/activity')}>View All</Button>
            </div>
            <div className="sk-activity-timeline">
              {recentActivity.map((activity, i) => (
                <div key={i} className="sk-activity-item">
                  <div className="sk-activity-icon">{activity.icon}</div>
                  <div className="sk-activity-content">
                    <p className="sk-activity-action">{activity.action}</p>
                    <time className="sk-activity-time">{activity.time}</time>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </section>

        <section className="sk-profile-security">
          <Card variant="default" padding="lg">
            <h2 className="sk-profile-section-title">Security Status</h2>
            <div className="sk-security-grid">
              <div className="sk-security-item">
                <div className="sk-security-icon">
                  <Icon name="shield" size={20} color="var(--color-success)" />
                </div>
                <div className="sk-security-info">
                  <h3>Email Verified</h3>
                  <p>Completed</p>
                </div>
              </div>
              <div className="sk-security-item">
                <div className="sk-security-icon">
                  <Icon name="phone" size={20} color="var(--color-success)" />
                </div>
                <div className="sk-security-info">
                  <h3>Phone Verified</h3>
                  <p>Last updated 2 weeks ago</p>
                </div>
              </div>
              <div className="sk-security-item">
                <div className="sk-security-icon">
                  <Icon name="lock" size={20} color="var(--color-warning)" />
                </div>
                <div className="sk-security-info">
                  <h3>Password</h3>
                  <p>Updated 3 months ago</p>
                </div>
              </div>
            </div>
            <Button variant="primary" onClick={() => navigate('/profile/security')} style={{ marginTop: 'var(--space-stack-md)' }}>
              Manage Security
            </Button>
          </Card>
        </section>

        <section className="sk-profile-quick-actions">
          <h2 className="sk-profile-section-title">Quick Actions</h2>
          <div className="sk-quick-actions-grid">
            <button className="sk-quick-action" onClick={() => navigate('/profile/edit')}>
              <div className="sk-quick-action-icon">
                <Icon name="user" size={24} color="currentColor" />
              </div>
              <span className="sk-quick-action-label">Edit Personal Info</span>
            </button>
            <button className="sk-quick-action" onClick={() => navigate('/profile/security')}>
              <div className="sk-quick-action-icon">
                <Icon name="shield" size={24} color="currentColor" />
              </div>
              <span className="sk-quick-action-label">Security Settings</span>
            </button>
            <button className="sk-quick-action" onClick={() => navigate('/profile/preferences')}>
              <div className="sk-quick-action-icon">
                <Icon name="settings" size={24} color="currentColor" />
              </div>
              <span className="sk-quick-action-label">Preferences</span>
            </button>
            <button className="sk-quick-action" onClick={() => navigate('/profile/activity')}>              <div className="sk-quick-action-icon">
                <Icon name="activity" size={24} color="currentColor" />
              </div>
              <span className="sk-quick-action-label">Activity History</span>
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}
