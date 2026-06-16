package net.hackyourfuture.tickettrackingsystem.repository;

import net.hackyourfuture.tickettrackingsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserRepository {


    @Autowired
    private JdbcTemplate jdbc;

    public List<User> findAll() {
        return jdbc.query(
                "SELECT * FROM users",
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                )
        );

    }
public User findById (Long id) {
        return jdbc.queryForObject(
                "SELECT * FROM users WHERE id = ? ",
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")


                ), id

        );
}

public int save(User user) {
        return jdbc.update(
                "INSERT INTO users(name, email) VALUES (?, ?)",
                user.getName(),
                user.getEmail()
        );
}

public int update(Long id, User user) {
        return jdbc.update(
              "UPDATE users SET name = ?, email = ? WHERE id = ?",
              user.getName(),
              user.getEmail(),
                id
        );
}
public int delete(Long id, User user) {
        return jdbc.update(
                "DELETE FROM users WHERE id = ?",
                id
        );
}
public boolean existsByEmail(String email){
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = ?",
                Integer.class,
                email
        );
        return count != null && count > 0;
}

}
