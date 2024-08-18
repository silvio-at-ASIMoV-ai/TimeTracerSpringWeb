package com.asimov.timeTracerSpringWeb.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class Users {
    private final JdbcTemplate jdbcTemplate;

    public Users(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static User mapUserRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getString("UserName"),
                         rs.getString("Password"),
                         rs.getTimestamp("ResetTime"),
                         rs.getInt("RoleId"),
                         rs.getInt("EmployeeId"));
    }

    public Optional<User> findById(String userName) {
        String sql = "SELECT * FROM Users WHERE UPPER(UserName) = UPPER(?)";
        Optional<User> cred = jdbcTemplate.query(sql, Users::mapUserRow, userName).stream().findFirst();
        return cred;
    }

    public boolean updatePassword(String username, String pwd) {
        String sql = "UPDATE Users SET Password = ? WHERE UPPER(UserName) = UPPER(?)";
        return jdbcTemplate.update(sql,new String[]{pwd, username}) > 0;
    }
}
