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
import java.util.ArrayList;
import java.util.List;
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
     * @return
     * @throws SQLException
     */
    public GuildManager createNewGuildManager(APerson person) throws SQLException {
        String sql = "INSERT INTO Person (FirstName, LastName, Email, Phone) VALUES (?,?,?,?)";
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());
            ps.setString(3, person.getEmail());
            ps.setInt(4, person.getPhone());

            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();
            key.next();
            int id = key.getInt(1);
            return getOneGuildManager(person, id);
        }
    }

    /**
     * Adds a Guild to a Manager in the database.
     *
     * @param personID
     * @param guildName
     * @throws SQLException
     */
    public void addGuildToManager(int personID, String guildName) throws SQLException {
        String sql = "INSERT INTO GuildManager (PersonID, GuildName) VALUES (?,?)";
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, personID);
            ps.setString(2, guildName);
            ps.executeUpdate();
        }
    }

    /**
     * Gets a list of all GuildManagers from the database.
     *
     * @return
     */
    public List<GuildManager> getAllGuildManagers() {
        List<GuildManager> listOfGuildManagers = new ArrayList<>();
        String sql = "SELECT * FROM ";
        //TODO RKL: Make method finish.
        return listOfGuildManagers;
    }

    /**
     * Creates a guild manager via a resultset.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private GuildManager getOneGuildManager(APerson person, int id) throws SQLException {
        return new GuildManager(person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone(), id);
    }
}
