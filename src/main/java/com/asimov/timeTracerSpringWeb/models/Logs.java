package com.asimov.timeTracerSpringWeb.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class Logs {
    private final JdbcTemplate jdbcTemplate;

    public Logs(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Log mapLogRow(ResultSet rs, int rowNum) throws SQLException {
        return new Log(rs.getInt("ID"),
                rs.getString("Operation"),
                rs.getString("Table"),
                rs.getString("NewValues"),
                rs.getString("OldValues"),
                rs.getInt("undoId"),
                rs.getTimestamp("timestamp"));
    }

    public void save(Log newLog) {
        String sql = "INSERT INTO Logs (Operation, Table, NewValues, OldValues, undoId, timestamp) " +
                     "VALUES (?, ?, ?, ?, ? ,?)";
        Object[] params = new Object[]{newLog.Operation(), newLog.Table(), newLog.NewValues(),
                                       newLog.OldValues(), newLog.undoId(), newLog.timestamp()};
        jdbcTemplate.update(sql, params);
    }

    public Optional<Log> findById(int id) {
        String sql = "SELECT * FROM Logs WHERE ID = ?";
        return jdbcTemplate.query(sql, Logs::mapLogRow, id).stream().findFirst();
    }

    public List<Log> findAll() {
        String sql = "SELECT * FROM Logs";
        return jdbcTemplate.query(sql, Logs::mapLogRow).stream().toList();
    }

    public List<Log> findAllLimited(int limit) {
        String sql = "SELECT * FROM Logs";
        return jdbcTemplate.query(sql, Logs::mapLogRow).stream().limit(limit).toList();
    }

}
