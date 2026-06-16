package net.hackyourfuture.tickettrackingsystem.repository;

import net.hackyourfuture.tickettrackingsystem.model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketRepository {

    private final JdbcTemplate jdbc;

    public TicketRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // -----------------------------
    // RowMapper
    // -----------------------------
    private RowMapper<Ticket> mapper() {
        return new RowMapper<>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Ticket(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getLong("project_id"),
                        rs.getTimestamp("created_at").toInstant()
                );
            }
        };
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    public Ticket create(Ticket t) {
        String sql = """
                INSERT INTO tickets (title, description, status, project_id, created_at)
                VALUES (?, ?, ?, ?, NOW())
                RETURNING id, title, description, status, project_id, created_at
                """;

        return jdbc.queryForObject(
                sql,
                mapper(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getProjectId()
        );
    }

    // -----------------------------
    // FIND BY ID
    // -----------------------------
    public Optional<Ticket> findById(Long id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        List<Ticket> list = jdbc.query(sql, mapper(), id);
        return list.stream().findFirst();
    }

    // -----------------------------
    // FIND ALL
    // -----------------------------
    public List<Ticket> findAll() {
        String sql = """
                SELECT id, title, description, status, project_id, created_at
                FROM tickets
                ORDER BY id
                """;

        return jdbc.query(sql, mapper());
    }

    // -----------------------------
    // SEARCH (text + status)
    // -----------------------------
    public List<Ticket> search(String text, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tickets WHERE 1=1 ");
        List<Object> params = new java.util.ArrayList<>();

        if (text != null && !text.isBlank()) {
            sql.append(" AND (LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))");
            params.add("%" + text + "%");
            params.add("%" + text + "%");
        }

        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        return jdbc.query(sql.toString(), mapper(), params.toArray());
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    public void update(Ticket t) {
        String sql = """
                UPDATE tickets
                SET title = ?, description = ?, status = ?, project_id = ?
                WHERE id = ?
                """;

        jdbc.update(
                sql,
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getProjectId(),
                t.getId()
        );
    }
}
