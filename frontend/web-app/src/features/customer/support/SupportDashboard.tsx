import React from 'react';
import { useNavigate } from 'react-router-dom';
import { INITIAL_TICKETS, KB_ARTICLES } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

export const SupportDashboard: React.FC = () => {
  const navigate = useNavigate();

  const openTickets = INITIAL_TICKETS.filter(t => t.status !== 'Resolved');
  const resolvedTickets = INITIAL_TICKETS.filter(t => t.status === 'Resolved');

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Welcome Title */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
            Help & Support Center
          </h2>
          <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
            Get expert help with orders, mycology cultivation, or sterile lab techniques.
          </p>
        </div>

        <button 
          type="button" 
          className="cw-btn cw-btn--primary"
          onClick={() => navigate('/dashboard/support/contact')}
        >
          Create Support Ticket
        </button>
      </div>

      {/* Metrics Row */}
      <Grid columns="repeat(auto-fit, minmax(200px, 1fr))" gap="16px">
        {[
          { label: 'Active Support Tickets', value: openTickets.length, icon: 'message-circle', color: 'var(--color-primary)' },
          { label: 'Resolved Tickets', value: resolvedTickets.length, icon: 'check-circle', color: 'var(--color-text-success, #166534)' },
          { label: 'Knowledge Base Articles', value: KB_ARTICLES.length, icon: 'book-open', color: '#0ea5e9' },
        ].map((stat, i) => (
          <Card key={i} variant="outlined" padding="md" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <div style={{ 
              width: '40px', 
              height: '40px', 
              borderRadius: '50%', 
              background: 'var(--color-bg-primary-weak)', 
              color: stat.color, 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center' 
            }}>
              <Icon name={stat.icon} size={20} color="currentColor" />
            </div>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>
                {stat.label}
              </span>
              <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)' }}>
                {stat.value}
              </strong>
            </div>
          </Card>
        ))}
      </Grid>

      {/* Main split dashboard layout */}
      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Area: Recent tickets, quick actions, faqs preview */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Active tickets lists */}
          <Card variant="outlined" padding="lg">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                Recent Tickets
              </h3>
              <button 
                type="button" 
                className="cw-btn cw-btn--outlined cw-btn--xs"
                onClick={() => navigate('/dashboard/support/tickets')}
              >
                View Support History
              </button>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {INITIAL_TICKETS.slice(0, 2).map((ticket) => (
                <div 
                  key={ticket.id}
                  onClick={() => navigate(`/dashboard/support/tickets/${ticket.id}`)}
                  style={{
                    border: '1px solid var(--color-border-default)',
                    borderRadius: 'var(--radius-md)',
                    padding: '12px',
                    cursor: 'pointer',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    flexWrap: 'wrap',
                    gap: '12px',
                    transition: 'all 0.1s ease',
                  }}
                >
                  <div>
                    <div style={{ display: 'flex', gap: '8px', alignItems: 'center', flexWrap: 'wrap' }}>
                      <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)', fontWeight: 'bold' }}>{ticket.id}</span>
                      <span style={{ 
                        fontSize: '9px', 
                        fontWeight: 'bold', 
                        background: ticket.priority === 'High' ? 'var(--color-bg-danger-weak, #fee2e2)' : 'var(--color-bg-primary-weak)',
                        color: ticket.priority === 'High' ? '#dc2626' : 'var(--color-primary)',
                        padding: '1px 6px',
                        borderRadius: '2px'
                      }}>
                        {ticket.priority.toUpperCase()}
                      </span>
                    </div>
                    <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '6px 0 2px' }}>
                      {ticket.subject}
                    </h4>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                      Category: {ticket.category} · Agent: {ticket.assignedAgent}
                    </span>
                  </div>

                  <span style={{ 
                    fontSize: 'var(--text-caption)', 
                    fontWeight: 'bold', 
                    color: ticket.status === 'Resolved' ? 'var(--color-text-success, #166534)' : 'var(--color-primary)' 
                  }}>
                    {ticket.status.toUpperCase()} &rarr;
                  </span>
                </div>
              ))}
            </div>
          </Card>

          {/* Quick Actions and Help pages */}
          <Grid columns="1fr 1fr" gap="16px">
            {[
              { label: 'FAQ Directory', desc: 'Find quick solutions for standard queries.', route: '/dashboard/support/faq', icon: 'help-circle' },
              { label: 'Knowledge Base', desc: 'Read sterile lab techniques guides.', route: '/dashboard/support/help-center', icon: 'book-open' },
              { label: 'Contact Support Form', desc: 'Submit a technical assistance request.', route: '/dashboard/support/contact', icon: 'message-circle' },
              { label: 'Customer Feedback', desc: 'Send suggestions or report bugs.', route: '/dashboard/support/feedback', icon: 'edit' },
            ].map((action, i) => (
              <Card 
                key={i} 
                variant="outlined" 
                padding="md"
                onClick={() => navigate(action.route)}
                style={{ cursor: 'pointer', transition: 'transform 0.15s ease' }}
              >
                <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
                  <div style={{ color: 'var(--color-primary)', marginTop: '2px' }}>
                    <Icon name={action.icon} size={18} color="currentColor" />
                  </div>
                  <div>
                    <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                      {action.label}
                    </h4>
                    <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '4px 0 0', lineHeight: '1.3' }}>
                      {action.desc}
                    </p>
                  </div>
                </div>
              </Card>
            ))}
          </Grid>
        </div>

        {/* Right Area: KB shortcuts, helpline, AI mentor */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Featured KB articles */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
              Suggested Articles
            </h4>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {KB_ARTICLES.slice(0, 2).map((article) => (
                <div 
                  key={article.id}
                  onClick={() => navigate('/dashboard/support/help-center')}
                  style={{ 
                    borderBottom: '1px solid var(--color-border-default)', 
                    paddingBottom: '12px',
                    cursor: 'pointer',
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '4px'
                  }}
                >
                  <span style={{ fontSize: '9px', color: 'var(--color-primary)', fontWeight: 'bold' }}>
                    {article.category.toUpperCase()} · {article.readTime} read
                  </span>
                  <strong style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-primary)', lineHeight: '1.3' }}>
                    {article.title}
                  </strong>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                    {article.description}
                  </span>
                </div>
              ))}
            </div>
          </Card>

          {/* AI Helper Banner */}
          <Card 
            variant="outlined" 
            padding="md"
            style={{ 
              background: 'linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #fcfdf8 100%)', 
              border: '1px solid var(--color-border-default)' 
            }}
          >
            <div style={{ display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
              <span style={{ fontSize: '20px' }}>🤖</span>
              <div>
                <h5 style={{ fontSize: 'var(--text-caption)', fontWeight: 'bold', color: 'var(--color-text-primary)', margin: 0 }}>
                  AI Support Co-Pilot
                </h5>
                <p style={{ fontSize: '11px', color: 'var(--color-text-secondary)', margin: '4px 0 0', lineHeight: '1.4' }}>
                  Have a quick question? Our automated assistant can parse your order delivery schedules or log techniques queries instantly.
                </p>
                <button
                  type="button"
                  className="cw-btn cw-btn--primary cw-btn--xs"
                  onClick={() => navigate('/dashboard/support/faq')}
                  style={{ marginTop: '8px', fontSize: '9px' }}
                >
                  Launch Assistant
                </button>
              </div>
            </div>
          </Card>
        </div>
      </Grid>
    </div>
  );
};
export default SupportDashboard;
