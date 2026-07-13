import { useState, useRef, useEffect } from 'react';
import { Message, Intent } from '../types';
import IntentViewer from './IntentViewer';

const SAMPLE_INTENTS: Intent[] = [
  { name: 'sales.query', confidence: 0.94, priority: 'High', entities: [{ name: 'period', value: 'Q2' }, { name: 'metric', value: 'revenue' }] },
  { name: 'report.generate', confidence: 0.87, priority: 'Medium', entities: [{ name: 'format', value: 'PDF' }] },
  { name: 'data.sync', confidence: 0.76, priority: 'Low', entities: [{ name: 'source', value: 'Salesforce' }] },
];

function ConversationPanel() {
  const [messages, setMessages] = useState<Message[]>([
    { id: '1', role: 'assistant', content: 'Hello! How can I help you today?', timestamp: new Date().toISOString() },
  ]);
  const [input, setInput] = useState('');
  const [selectedIntent, setSelectedIntent] = useState<Intent>(SAMPLE_INTENTS[0]);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = () => {
    if (!input.trim()) return;

    const userMsg: Message = {
      id: String(Date.now()),
      role: 'user',
      content: input,
      timestamp: new Date().toISOString(),
    };

    const assistantMsg: Message = {
      id: String(Date.now() + 1),
      role: 'assistant',
      content: `I processed your request: "${input}". The relevant data has been analyzed.`,
      timestamp: new Date().toISOString(),
      intent: selectedIntent,
      taskStatus: 'Completed',
    };

    setMessages([...messages, userMsg, assistantMsg]);
    setInput('');
  };

  return (
    <div className="chat-page">
      <h2>Conversation</h2>
      <div className="chat-layout">
        <div className="chat-panel">
          <div className="message-list">
            {messages.map((msg) => (
              <div key={msg.id} className={`message-bubble ${msg.role}`}>
                <div className="message-content">{msg.content}</div>
                {msg.taskStatus && (
                  <div className="message-task">
                    Task: <span className={`status-badge ${msg.taskStatus.toLowerCase()}`}>{msg.taskStatus}</span>
                  </div>
                )}
                <div className="message-time">
                  {new Date(msg.timestamp).toLocaleTimeString()}
                </div>
              </div>
            ))}
            <div ref={messagesEndRef} />
          </div>
          <div className="chat-input-area">
            <input
              type="text"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSend()}
              placeholder="Type your message..."
            />
            <button className="btn btn-primary" onClick={handleSend}>
              Send
            </button>
          </div>
        </div>
        <div className="chat-sidebar">
          <h3>Current Intent</h3>
          <select
            value={selectedIntent.name}
            onChange={(e) => {
              const found = SAMPLE_INTENTS.find((i) => i.name === e.target.value);
              if (found) setSelectedIntent(found);
            }}
          >
            {SAMPLE_INTENTS.map((i) => (
              <option key={i.name} value={i.name}>
                {i.name}
              </option>
            ))}
          </select>
          <IntentViewer intent={selectedIntent} />
        </div>
      </div>
    </div>
  );
}

export default ConversationPanel;
