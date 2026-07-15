import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { INITIAL_TICKETS, SupportTicket } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { Grid } from '../../../design-system/components/layout/Grid';

export const TicketsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [tickets, setTickets] = useState<SupportTicket[]>(INITIAL_TICKETS);
  const [replyText, setReplyText] = useState('');
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Active ticket search
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<'all' | 'Open' | 'Resolved'>('all');

  const selectedTicket = tickets.find((t) => t.id === id);

  const handleSendReply = (e: React.FormEvent) => {
    e.preventDefault();
    if (!replyText.trim() || !selectedTicket) return;

    const newMessage = {
      sender: 'customer' as const,
      text: replyText,
      timestamp: 'Just Now'
    };

    setTickets(prev =>
      prev.map(t =>
        t.id === selectedTicket.id
          ? {
              ...t,
              messages: [...t.messages, newMessage],
              updatedDate: '2026-07-14'
            }
          : t
      )
    );

    setReplyText('');
    setToastMessage('Reply sent successfully!');
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleToggleStatus = (ticketId: string, currentStatus: string) => {
    const newStatus = currentStatus === 'Resolved' ? 'Open' : 'Resolved';
    setTickets(prev =>
      prev.map(t =>
        t.id === ticketId
          ? { ...t, status: newStatus as any, updatedDate: '2026-07-14' }
          : t
      )
    );
    setToastMessage(`Ticket marked as ${newStatus}!`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  // Filter tickets for list view
  const filteredTickets = tickets.filter(t => {
    const matchesSearch = t.subject.toLowerCase().includes(searchTerm.toLowerCase()) || 
                          t.id.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'all' || t.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Case 1: Ticket Details View */}
      {selectedTicket ? (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* Back button */}
          <div>
            <button
              type="button"
              onClick={() => navigate('/dashboard/support/tickets')}
              style={{
                background: 'none',
                border: 'none',
                padding: 0,
                fontSize: 'var(--text-body-sm)',
                fontWeight: 'var(--weight-semibold)',
                color: 'var(--color-text-secondary)',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
              }}
            >
              <Icon name="arrow-left" size={16} color="currentColor" />
              Back to Tickets List
            </button>
          </div>

          {/* Ticket Header Card */}
          <Card variant="outlined" padding="md">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '16px' }}>
              <div>
                <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', fontWeight: 'bold' }}>
                    {selectedTicket.id}
                  </span>
                  <span style={{ 
                    fontSize: '9px', 
                    fontWeight: 'bold', 
                    background: selectedTicket.priority === 'High' ? '#fee2e2' : 'var(--color-bg-primary-weak)',
                    color: selectedTicket.priority === 'High' ? '#dc2626' : 'var(--color-primary)',
                    padding: '1px 6px',
                    borderRadius: '2px'
                  }}>
                    {selectedTicket.priority.toUpperCase()} PRIORITY
                  </span>
                </div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '6px 0 4px' }}>
                  {selectedTicket.subject}
                </h3>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  Assigned Expert: <strong>{selectedTicket.assignedAgent}</strong> · Category: {selectedTicket.category}
                </span>
              </div>

              <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                <span style={{ 
                  fontSize: 'var(--text-caption)', 
                  fontWeight: 'bold', 
                  color: selectedTicket.status === 'Resolved' ? 'var(--color-text-success, #166534)' : 'var(--color-primary)' 
                }}>
                  Status: {selectedTicket.status.toUpperCase()}
                </span>
                
                <button
                  type="button"
                  className="cw-btn cw-btn--outlined cw-btn--sm"
                  onClick={() => handleToggleStatus(selectedTicket.id, selectedTicket.status)}
                >
                  {selectedTicket.status === 'Resolved' ? 'Reopen Ticket' : 'Mark Resolved'}
                </button>
              </div>
            </div>
          </Card>

          {/* Conversation Timeline Chat box */}
          <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
            {/* Left Area: Chat messages and reply input */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
              <Card variant="outlined" padding="lg" style={{ minHeight: '300px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                {/* Messages Feed */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px', overflowY: 'auto', marginBottom: '24px' }}>
                  {selectedTicket.messages.map((msg, i) => {
                    const isCustomer = msg.sender === 'customer';
                    return (
                      <div 
                        key={i} 
                        style={{ 
                          alignSelf: isCustomer ? 'flex-end' : 'flex-start',
                          maxWidth: '75%',
                          display: 'flex',
                          flexDirection: 'column',
                          alignItems: isCustomer ? 'flex-end' : 'flex-start'
                        }}
                      >
                        <div 
                          style={{
                            background: isCustomer ? 'var(--color-bg-primary-default)' : '#f1f5f9',
                            color: isCustomer ? '#fff' : 'var(--color-text-primary)',
                            padding: '12px 16px',
                            borderRadius: isCustomer ? '16px 16px 2px 16px' : '16px 16px 16px 2px',
                            fontSize: 'var(--text-body-sm)',
                            lineHeight: '1.4'
                          }}
                        >
                          {msg.text}
                        </div>
                        <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)', marginTop: '4px' }}>
                          {msg.timestamp}
                        </span>
                      </div>
                    );
                  })}
                </div>

                {/* Reply Form */}
                <form onSubmit={handleSendReply} style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '16px' }}>
                  <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
                    <textarea
                      rows={2}
                      value={replyText}
                      onChange={(e) => setReplyText(e.target.value)}
                      placeholder="Type your message reply..."
                      style={{
                        flex: 1,
                        padding: '8px 12px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--color-border-default)',
                        fontSize: 'var(--text-body-sm)',
                        background: 'var(--color-bg-surface-default)',
                        color: 'var(--color-text-primary)',
                        outline: 'none',
                        resize: 'none'
                      }}
                    />
                    <button
                      type="submit"
                      className="cw-btn cw-btn--primary"
                      style={{ height: '40px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                    >
                      Send
                    </button>
                  </div>
                </form>
              </Card>
            </div>

            {/* Right Area: Sidebar files / logs */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
              <Card variant="outlined" padding="md">
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
                  Ticket Information
                </h4>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  <div>
                    <span>CREATED AT</span>
                    <strong style={{ display: 'block', color: 'var(--color-text-primary)' }}>{selectedTicket.createdDate}</strong>
                  </div>
                  <div>
                    <span>LAST UPDATE</span>
                    <strong style={{ display: 'block', color: 'var(--color-text-primary)' }}>{selectedTicket.updatedDate}</strong>
                  </div>
                  <div>
                    <span>PREFERRED CHANNEL</span>
                    <strong style={{ display: 'block', color: 'var(--color-text-primary)' }}>Email Support</strong>
                  </div>
                </div>
              </Card>

              <Card variant="outlined" padding="md">
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
                  Attachment files (0)
                </h4>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>
                  No screenshots or document logs attached to this ticket yet.
                </p>
              </Card>
            </div>
          </Grid>

        </div>
      ) : (
        /* Case 2: Tickets List View */
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* Header row */}
          <div>
            <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
              Support Request History
            </h2>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
              Review, update, or reopen your support tickets and lab assistance logs.
            </p>
          </div>

          {/* Filters controls bar */}
          <div 
            style={{ 
              display: 'flex', 
              justifyContent: 'space-between', 
              alignItems: 'center', 
              gap: '16px', 
              flexWrap: 'wrap',
              borderBottom: '1px solid var(--color-border-default)',
              paddingBottom: '12px'
            }}
          >
            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
              {[
                { id: 'all', label: 'All Tickets' },
                { id: 'Open', label: 'Open' },
                { id: 'Resolved', label: 'Resolved' },
              ].map((tab) => (
                <button
                  key={tab.id}
                  type="button"
                  onClick={() => setStatusFilter(tab.id as any)}
                  style={{
                    border: 'none',
                    background: statusFilter === tab.id ? 'var(--color-bg-primary-weak)' : 'transparent',
                    padding: '6px 12px',
                    borderRadius: 'var(--radius-sm)',
                    fontSize: 'var(--text-body-sm)',
                    fontWeight: statusFilter === tab.id ? 'var(--weight-bold)' : 'var(--weight-medium)',
                    color: statusFilter === tab.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                    cursor: 'pointer'
                  }}
                >
                  {tab.label}
                </button>
              ))}
            </div>

            <div style={{ position: 'relative', width: '100%', maxWidth: '240px' }}>
              <span style={{ position: 'absolute', left: '10px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
                <Icon name="search" size={14} color="currentColor" />
              </span>
              <input
                type="text"
                placeholder="Search ticket subjects..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                style={{
                  width: '100%',
                  padding: '6px 12px 6px 32px',
                  border: '1px solid var(--color-border-default)',
                  borderRadius: 'var(--radius-md)',
                  fontSize: 'var(--text-body-sm)',
                  outline: 'none',
                  background: 'var(--color-bg-surface-default)',
                  color: 'var(--color-text-primary)'
                }}
              />
            </div>
          </div>

          {/* Tickets lists */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {filteredTickets.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
                <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>📂</div>
                <div>
                  <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No tickets found</h3>
                  <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Submit a ticket if you need grow assistance.</p>
                </div>
              </div>
            ) : (
              filteredTickets.map((ticket) => (
                <Card
                  key={ticket.id}
                  variant="outlined"
                  padding="md"
                  onClick={() => navigate(`/dashboard/support/tickets/${ticket.id}`)}
                  style={{
                    cursor: 'pointer',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    flexWrap: 'wrap',
                    gap: '16px',
                    transition: 'all 0.1s ease',
                  }}
                >
                  <div>
                    <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', fontWeight: 'bold' }}>{ticket.id}</span>
                      <span style={{ 
                        fontSize: '9px', 
                        fontWeight: 'bold', 
                        background: ticket.priority === 'High' ? '#fee2e2' : 'var(--color-bg-primary-weak)',
                        color: ticket.priority === 'High' ? '#dc2626' : 'var(--color-primary)',
                        padding: '1px 6px',
                        borderRadius: '2px'
                      }}>
                        {ticket.priority}
                      </span>
                      <span style={{ fontSize: '11px', color: 'var(--color-text-secondary)' }}>
                        Created: {ticket.createdDate}
                      </span>
                    </div>

                    <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '8px 0 2px' }}>
                      {ticket.subject}
                    </h4>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                      Category: {ticket.category} · Agent: {ticket.assignedAgent}
                    </span>
                  </div>

                  <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
                    <span style={{ 
                      fontSize: 'var(--text-caption)', 
                      fontWeight: 'bold', 
                      color: ticket.status === 'Resolved' ? 'var(--color-text-success, #166534)' : 'var(--color-primary)' 
                    }}>
                      {ticket.status.toUpperCase()}
                    </span>
                    <Icon name="chevron-right" size={16} color="var(--color-text-secondary)" />
                  </div>
                </Card>
              ))
            )}
          </div>

        </div>
      )}

    </div>
  );
};
export default TicketsPage;
