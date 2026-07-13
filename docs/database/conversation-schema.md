# Enterprise AI Conversation Platform Database Schema

**Version:** 1.0.0
**Migration:** V16__sprint17_conversation.sql
**Engine:** PostgreSQL (H2 compatible for dev/test)

---

## Tables

### conversation_sessions

Stores all conversation sessions with status tracking.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| user_id | VARCHAR(255) | NOT NULL | Owner user ID |
| title | VARCHAR(500) | | Session title |
| status | VARCHAR(50) | NOT NULL | ACTIVE, ARCHIVED, CLOSED |
| metadata | TEXT | | JSON metadata |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update timestamp |
| expires_at | TIMESTAMP | | Session expiration |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_conv_sessions_user` (user_id), `idx_conv_sessions_status` (status)

### conversation_messages

Stores all messages within conversation sessions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to conversation_sessions |
| role | VARCHAR(50) | NOT NULL | USER, ASSISTANT, SYSTEM, TOOL, CONTEXT, KNOWLEDGE |
| content | TEXT | NOT NULL | Message content |
| metadata | TEXT | | JSON metadata |
| status | VARCHAR(50) | NOT NULL | PENDING, SENT, DELIVERED, FAILED |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_conv_messages_session` (session_id), `idx_conv_messages_role` (role)

### conversation_memories

Stores short-term and long-term conversation memories.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to conversation_sessions |
| memory_type | VARCHAR(50) | NOT NULL | SHORT_TERM, LONG_TERM |
| summary | TEXT | | Memory summary |
| keywords | VARCHAR(1000) | | Comma-separated keywords |
| relevance_score | DOUBLE | NOT NULL, DEFAULT 0.0 | Relevance score (0-1) |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| expires_at | TIMESTAMP | | Memory expiration (24h for STM) |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_conv_memories_session` (session_id), `idx_conv_memories_type` (memory_type)

### conversation_contexts

Stores context entries assembled for conversation sessions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to conversation_sessions |
| source | VARCHAR(255) | NOT NULL | Context source identifier |
| content | TEXT | NOT NULL | Context content |
| weight | DOUBLE | NOT NULL, DEFAULT 1.0 | Context weight |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_conv_contexts_session` (session_id)

### conversation_session_archive

Archive table for closed/deleted sessions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| original_session_id | UUID | NOT NULL | Original session ID |
| user_id | VARCHAR(255) | NOT NULL | Owner user ID |
| title | VARCHAR(500) | | Session title |
| status | VARCHAR(50) | NOT NULL | Final status |
| session_metadata | TEXT | | Original metadata |
| archived_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Archive timestamp |

### conversation_message_archive

Archive table for messages from closed sessions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| original_message_id | UUID | NOT NULL | Original message ID |
| session_id | UUID | NOT NULL | Session ID |
| role | VARCHAR(50) | NOT NULL | Message role |
| content | TEXT | NOT NULL | Message content |
| metadata | TEXT | | Original metadata |
| status | VARCHAR(50) | NOT NULL | Message status |
| archived_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Archive timestamp |

### conversation_tags

Tags for organizing conversation sessions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to conversation_sessions |
| tag | VARCHAR(100) | NOT NULL | Tag value |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_conv_tags_session` (session_id)

### conversation_participants

Tracks participants in conversation sessions (future multi-user support).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to conversation_sessions |
| user_id | VARCHAR(255) | NOT NULL | Participant user ID |
| role | VARCHAR(50) | NOT NULL, DEFAULT 'MEMBER' | Participant role |
| joined_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Join timestamp |
| left_at | TIMESTAMP | | Leave timestamp |

**Indexes:** `idx_conv_participants_session` (session_id), `idx_conv_participants_user` (user_id)
