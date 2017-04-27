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
    private List<Integer> guildManagerIDs;

    public GuildManagerDAO() throws IOException {
        connectionManager = DBConnectionManager.getInstance();
        guildManagerIDs = new ArrayList<>();
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
    public List<GuildManager> getAllGuildManagers() throws SQLException {
        guildManagerIDs.clear();
        List<GuildManager> listOfGuildManagers = new ArrayList<>();
        String sql = "SELECT p.ID, p.FirstName, p.LastName, p.Email, p.Phone, gm.GuildName FROM Person p "
                + "JOIN GuildManager gm ON gm.PersonID = p.ID";
        try (Connection con = connectionManager.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                if (checkIfGuildManagerIsAlreadyThere(id)) {
                    listOfGuildManagers.add(getOneGuildManager(rs, id));
                }
            }
        }
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

    /**
     * Creates a guild manager via a resultset + id.
     *
     * @param person
     * @param id
     * @return
     * @throws SQLException
     */
    private GuildManager getOneGuildManager(ResultSet rs, int id) throws SQLException {
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String email = rs.getString("Email");
        int phone = rs.getInt("Phone");
        return new GuildManager(firstName, lastName, email, phone, id);
    }

    /**
     * Checks the list of guildmanager id's to see if it already is added.
     *
     * @param id
     * @return
     */
    private boolean checkIfGuildManagerIsAlreadyThere(int id) {
        for (Integer guildManagerID : guildManagerIDs) {
            if (id == guildManagerID) {
                return false;
            }
        }
        guildManagerIDs.add(id);
        return true;
    }

    /**
     * Gets all guilds a Manager manages.
     *
     * @param con
     * @param managerID
     * @return
     * @throws SQLException
     */
    private List<String> getAllGuildsForOneManager(Connection con, int managerID) throws SQLException {
        String sql = "SELECT gm.GuildName "
                + "FROM GuildManager gm "
                + "WHERE gm.PersonID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, managerID);
        ResultSet rs = ps.executeQuery();
        List<String> listOfGuilds = new ArrayList<>();
        while (rs.next()) {
            listOfGuilds.add(rs.getString("GuildName"));
        }
        return listOfGuilds;
    }

    /**
     * Adds guilds to all the guildManagers in the list and returns it.
     *
     * @param guildManagers
     * @return
     * @throws SQLException
     */
    private List<GuildManager> addGuildsToGuildManagers(List<GuildManager> guildManagers) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            for (GuildManager guildManager : guildManagers) {
                guildManager.addAllGuilds(getAllGuildsForOneManager(con, guildManager.getID()));
            }
        }
        return guildManagers;
    }
}
