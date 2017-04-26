/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GuildManager;

/**
 *
 * @author Mathias
 */
public class GuildManagerDAO {

    private final DBConnectionManager connectionManager;

    public GuildManagerDAO() throws IOException {
        connectionManager = DBConnectionManager.getInstance();
    }

    /**
     * Creates a new guild manager in the DB.
     *
     * @param person
     * @throws SQLException
     */
    public void createNewGuildManager(APerson person) throws SQLException {
        String sql = "INSERT INTO Person (FirstName, LastName, Email, Phone) VALUES (?,?,?,?)";
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());
            ps.setString(3, person.getEmail());
            ps.setInt(4, person.getPhone());

            ps.executeQuery();
        }
    }

    /**
     * Creates a guild manager via a resultset.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private GuildManager getOneGuildManager(ResultSet rs) throws SQLException {
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String email = rs.getString("Email");
        int phone = rs.getInt("Phone");
        int id = rs.getInt("ID");

        return new GuildManager(firstName, lastName, email, phone, id);
    }
}
