package com.asimov.timeTracerSpringWeb.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
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
    public List<User> findAll() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.query(sql, Users::mapUserRow).stream().toList();
    }

    public Optional<User> findById(String userName) {
        String sql = "SELECT * FROM Users WHERE UPPER(UserName) = UPPER(?)";
        return jdbcTemplate.query(sql, Users::mapUserRow, userName).stream().findFirst();
    }

    public boolean updatePassword(String username, String pwd) {
        String sql = "UPDATE Users SET Password = ? WHERE UPPER(UserName) = UPPER(?)";
        Object[] params = new Object[]{pwd, username};
        return jdbcTemplate.update(sql, params) > 0;
    }

    public boolean update(User user){
        String sql = "UPDATE Users SET UserName = ?, RoleID = ?, EmployeeID = ? " +
                "WHERE UPPER(UserName) = UPPER(?)";
        Object[] params = new Object[]{user.UserName(), user.RoleID(),
                user.EmployeeID(), user.UserName()};
        return jdbcTemplate.update(sql, params) > 0;
    }

    public boolean insert(User user){
        String sql = "INSERT INTO Users (UserName, ResetTime, RoleID, EmployeeID) VALUES (?, ?, ?, ?)";
        Object[] params = new Object[]{user.UserName(), user.ResetTime(), user.RoleID(), user.EmployeeID()};
        return jdbcTemplate.update(sql, params) > 0;
    }

    public boolean deleteById(String userName) {
        String sql = "DELETE FROM Users WHERE UPPER(UserName) = UPPER(?)";
        return jdbcTemplate.update(sql, userName) > 0;
    }

    public boolean resetPwd(String userName) {
        String sql = "UPDATE Users SET ResetTime = ?, Password = null WHERE UPPER(UserName) = UPPER(?)";
        Object[] params = new Object[]{new Timestamp(System.currentTimeMillis()), userName};
        return jdbcTemplate.update(sql, params) > 0;
    }
}
