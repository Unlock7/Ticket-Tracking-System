package net.hackyourfuture.tickettrackingsystem.repository;

import net.hackyourfuture.tickettrackingsystem.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<User> mapper() {
        return new RowMapper<>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        };
    }

    public List<User> findAll() {
        return jdbc.query("SELECT * FROM users ORDER BY id", mapper());
    }

    public Optional<User> findById(Long id) {
        List<User> list = jdbc.query("SELECT * FROM users WHERE id = ?", mapper(), id);
        return list.stream().findFirst();
    }

    public List<User> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        String inSql = String.join(",", ids.stream().map(id -> "?").toList());

        String sql = "SELECT * FROM users WHERE id IN (" + inSql + ")";

        return jdbc.query(sql, mapper(), ids.toArray());
    }

    public Optional<User> findByEmail(String email) {
        List<User> list = jdbc.query("SELECT * FROM users WHERE email = ?", mapper(), email);
        return list.stream().findFirst();
    }

    public User create(User user) {
        String sql = """
                INSERT INTO users (name, email)
                VALUES (?, ?)
                RETURNING id, name, email
                """;

        return jdbc.queryForObject(
                sql,
                mapper(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(Long id, User user) {
        String sql = """
                UPDATE users
                SET name = ?, email = ?
                WHERE id = ?
                """;

        jdbc.update(sql, user.getName(), user.getEmail(), id);
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM users WHERE id = ?", id);
    }
}
