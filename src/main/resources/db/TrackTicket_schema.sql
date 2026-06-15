-- USERS TABLE
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL
);

-- PROJECTS TABLE
CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- TICKETS TABLE
CREATE TABLE tickets (
                         id SERIAL PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         project_id INT NOT NULL REFERENCES projects(id),
                         status VARCHAR(20) NOT NULL CHECK (status IN ('open', 'in progress', 'closed')),
                         created_at TIMESTAMP DEFAULT NOW(),
                         updated_at TIMESTAMP DEFAULT NOW()
);

-- MANY-TO-MANY: TICKET ASSIGNEES
CREATE TABLE ticket_assignees (
                                  ticket_id INT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                                  user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  PRIMARY KEY (ticket_id, user_id)
);

-- INDEXES

-- Fast filtering by status
CREATE INDEX idx_tickets_status ON tickets(status);

-- Fast text search on title + description
CREATE INDEX idx_tickets_text_search
    ON tickets USING GIN (to_tsvector('english', title || ' ' || description));
