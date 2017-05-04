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
import java.util.Set;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GuildManager;

/**
 *
 * @author Mathias
 */
public class GuildManagerDAO extends APersonDAO {

    private final DBConnectionManager connectionManager;
    private final List<Integer> guildManagerIDs;

    public GuildManagerDAO() throws IOException {
        connectionManager = DBConnectionManager.getInstance();
        guildManagerIDs = new ArrayList<>();
    }

    /**
     * Creates a new guild manager in the DB.
     *
     * @param person
     * @param guildName
     * @return
     * @throws SQLException
     */
    public GuildManager createNewGuildManager(APerson person, String guildName) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            int personId = createNewPersonInDatabase(con, person);
            addGuildToManagerInDatabase(con, personId, guildName);
            GuildManager manager = getOneGuildManager(person, personId);
            addGuildsToASingleGuildManager(con, manager);
            con.commit();
            return manager;
        }
    }

    /**
     * Updates the information of a GuildManager in the database.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsTodelete
     * @throws SQLException
     */
    public void updateGuildManagerInDatabase(GuildManager manager, Set<String> guildsToAdd, Set<String> guildsTodelete) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            updatePersonInformation(con, manager);

            if (guildsToAdd != null && !guildsToAdd.isEmpty()) {
                for (String guild : guildsToAdd) {
                    addGuildToManagerInDatabase(con, manager.getID(), guild);
                }
            }

            if (guildsTodelete != null && !guildsTodelete.isEmpty()) {
                for (String guild : guildsTodelete) {
                    removeGuildFromManagerFromDatabase(con, manager.getID(), guild);
                }
            }
            con.commit();
        }
    }

    /**
     * Adds a Guild to a Manager in the database.
     *
     * @param con
     * @param personID
     * @param guildName
     * @throws SQLException
     */
    private void addGuildToManagerInDatabase(Connection con, int personID, String guildName) throws SQLException {
        String sql = "INSERT INTO GuildManager (PersonID, GuildName) VALUES (?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, personID);
        ps.setString(2, guildName);
        ps.executeUpdate();
    }

    /**
     * Deletes a Guild from a Manager in the database.
     *
     * @param con
     * @param personID
     * @param guildName
     * @throws SQLException
     */
    private void removeGuildFromManagerFromDatabase(Connection con, int personID, String guildName) throws SQLException {
        String sql = "DELETE FROM GuildManager WHERE PersonID = ? AND GuildName = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, personID);
        ps.setString(2, guildName);
        ps.executeUpdate();
    }

    /**
     * Gets a list of all GuildManagers from the database.
     *
     * @return
     * @throws java.sql.SQLException
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
     * Creates a guild manager via a resultSet + id.
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
    public List<GuildManager> addGuildsToGuildManagers(List<GuildManager> guildManagers) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            for (GuildManager guildManager : guildManagers) {
                guildManager.addAllGuilds(getAllGuildsForOneManager(con, guildManager.getID()));
            }
        }
        return guildManagers;
    }

    /**
     * Adds guilds to a single GuildManager.
     *
     * @param con
     * @param manager
     * @throws SQLException
     */
    private void addGuildsToASingleGuildManager(Connection con, GuildManager manager) throws SQLException {
        manager.addAllGuilds(getAllGuildsForOneManager(con, manager.getID()));
    }

    /**
     * Deletes the GuildManager from the GuildManagerTable and from the
     * PersonTable.
     *
     * @param GuildManagerID
     * @throws SQLException
     */
    public void deleteGuildManagerFromDB(int GuildManagerID) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            deletePersonFromDatabaseByID(con, GuildManagerID);
            con.commit();
        }
    }
}
