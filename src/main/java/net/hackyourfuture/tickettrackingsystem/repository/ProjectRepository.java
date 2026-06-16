package net.hackyourfuture.tickettrackingsystem.repository;

import net.hackyourfuture.tickettrackingsystem.dto.project.ProjectResponse;
import net.hackyourfuture.tickettrackingsystem.dto.project.ProjectResponse.TicketsSummary;
import net.hackyourfuture.tickettrackingsystem.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Project> projectRowMapper() {
        return new RowMapper<>() {
            @Override
            public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Project(
                        rs.getLong("id"),
                        rs.getString("name")
                );
            }
        };
    }

    public List<Project> findAll() {
        String sql = "SELECT id, name FROM projects ORDER BY id";
        return jdbcTemplate.query(sql, projectRowMapper());
    }

    public List<ProjectResponse> findAllWithTicketCounts() {
        String sql = """
                SELECT p.id,
                       p.name,
                       COALESCE(SUM(CASE WHEN t.status = 'open' THEN 1 ELSE 0 END), 0)        AS open_count,
                       COALESCE(SUM(CASE WHEN t.status = 'in_progress' THEN 1 ELSE 0 END), 0) AS in_progress_count,
                       COALESCE(SUM(CASE WHEN t.status = 'closed' THEN 1 ELSE 0 END), 0)      AS closed_count
                FROM projects p
                LEFT JOIN tickets t ON t.project_id = p.id
                GROUP BY p.id, p.name
                ORDER BY p.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            long open = rs.getLong("open_count");
            long inProgress = rs.getLong("in_progress_count");
            long closed = rs.getLong("closed_count");

            TicketsSummary summary = new TicketsSummary(open, inProgress, closed);
            return new ProjectResponse(id, name, summary);
        });
    }
}

