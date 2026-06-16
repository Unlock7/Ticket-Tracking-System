package net.hackyourfuture.tickettrackingsystem.repository;

import net.hackyourfuture.tickettrackingsystem.model.TicketAssignee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketAssigneeRepository {

    private final JdbcTemplate jdbc;

    public TicketAssigneeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void assign(Long ticketId, Long userId) {
        String sql = "INSERT INTO ticket_assignees (ticket_id, user_id) VALUES (?, ?)";
        jdbc.update(sql, ticketId, userId);
    }

    public void remove(Long ticketId, Long userId) {
        String sql = "DELETE FROM ticket_assignees WHERE ticket_id = ? AND user_id = ?";
        jdbc.update(sql, ticketId, userId);
    }

    public boolean exists(Long ticketId, Long userId) {
        String sql = "SELECT COUNT(*) FROM ticket_assignees WHERE ticket_id = ? AND user_id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, ticketId, userId);
        return count != null && count > 0;
    }

    public List<Long> findUserIds(Long ticketId) {
        String sql = "SELECT user_id FROM ticket_assignees WHERE ticket_id = ?";
        return jdbc.query(sql, (rs, rowNum) -> rs.getLong("user_id"), ticketId);
    }

    public List<TicketAssignee> findAllByTicket(Long ticketId) {
        String sql = "SELECT ticket_id, user_id FROM ticket_assignees WHERE ticket_id = ?";
        return jdbc.query(sql,
                (rs, rowNum) -> new TicketAssignee(
                        rs.getLong("ticket_id"),
                        rs.getLong("user_id")
                ),
                ticketId
        );
    }
}
