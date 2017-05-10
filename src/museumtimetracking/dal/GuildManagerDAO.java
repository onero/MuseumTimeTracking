/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;

/**
 *
 * @author Mathias
 */
public class GuildManagerDAO extends APersonDAO {

    private final DBConnectionManager cm;

    public GuildManagerDAO() throws IOException {
        cm = DBConnectionManager.getInstance();
    }

    /**
     * Archive a manager
     *
     * @param selectedManager
     */
    public void archiveManager(int id, boolean value) throws SQLServerException, SQLException {
        String sql;
        if (value) {
            sql = "UPDATE GuildManager "
                    + "SET IsIdle = 1 "
                    + "WHERE PersonID = ?";
        } else {
            sql = "UPDATE GuildManager "
                    + "SET IsIdle = 0 "
                    + "WHERE PersonID = ?";
        }

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();

        }

    }

    /**
     * Creates a new guild manager in the DB.
     *
     * @param person
     * @param guildName
     * @return
     * @throws SQLException
     */
    public GM createNewGuildManager(APerson person, String guildName) throws SQLException {
        try (Connection con = cm.getConnection()) {
            con.setAutoCommit(false);
            int personId = createNewPersonInDatabase(con, person);
            addGuildToManagerInDatabase(con, personId, guildName);
            GM manager = getOneGuildManager(person, personId);
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
    public void updateGuildManagerInDatabase(GM manager, Set<String> guildsToAdd, Set<String> guildsTodelete) throws SQLException {
        try (Connection con = cm.getConnection()) {
            con.setAutoCommit(false);
            updatePersonInformation(con, manager);
            //TODO rkl: remove isEmpty.
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
        String sql = "INSERT INTO "
                + "GuildManager (PersonID, GuildName) "
                + "VALUES (?,?)";
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
        String sql = "DELETE FROM GuildManager "
                + "WHERE PersonID = ? "
                + "AND GuildName = ?";
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
    public Set<GM> getAllGuildManagersNotIdle() throws SQLException {
        Set<GM> setOfGuildManagers = new TreeSet<>();
        String sql = "SELECT p.ID, "
                + "p.FirstName, "
                + "p.LastName, "
                + "p.Email, "
                + "p.Phone, "
                + "gm.Description "
                + "FROM Person p "
                + "JOIN GuildManager gm ON gm.PersonID = p.ID "
                + "WHERE gm.IsIdle = 0";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GM newGuildManager = getOneGuildManager(rs);
                boolean idExists = setOfGuildManagers
                        .stream()
                        .anyMatch(gm -> gm.getID() == newGuildManager.getID());
                if (!idExists) {
                    setOfGuildManagers.add(newGuildManager);
                }
            }
        }
        addGuildsToGuildManagers(setOfGuildManagers);
        return setOfGuildManagers;
    }

    /**
     * Creates a guild manager via a resultset.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private GM getOneGuildManager(APerson person, int id) throws SQLException {
        return new GM(person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone(), id);
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
    public Set<GM> addGuildsToGuildManagers(Set<GM> guildManagers) throws SQLException {
        try (Connection con = cm.getConnection()) {
            for (GM guildManager : guildManagers) {
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
    private void addGuildsToASingleGuildManager(Connection con, GM manager) throws SQLException {
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
        try (Connection con = cm.getConnection()) {
            con.setAutoCommit(false);
            deletePersonFromDatabaseByID(con, GuildManagerID);
            con.commit();
        }
    }

    /**
     * Get all Idle GuildManagers
     *
     * @return
     */
    public Set<GM> getAllIdleGuildManagers() throws SQLServerException, SQLException {
        Set<GM> guildManagers = new HashSet<>();
        String sql = "SELECT p.ID, "
                + "p.FirstName, "
                + "p.LastName, "
                + "p.Email, "
                + "p.Phone, "
                + "gm.Description "
                + "FROM Person p "
                + "JOIN GuildManager gm ON gm.PersonID = p.ID "
                + "WHERE gm.IsIdle = 1";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guildManagers.add(getOneGuildManager(rs));
            }
            return guildManagers;
        }
    }

    /**
     * Get one guildmanager on resultset
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private GM getOneGuildManager(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String email = rs.getString("Email");
        int phone = rs.getInt("Phone");
        String description = rs.getString("Description");

        GM gm = new GM(id, firstName, lastName, email, phone, email);
        gm.setDescription(description);
        return gm;
    }

    /**
     * Get all GuildManager candidates
     *
     * @return
     * @throws SQLServerException
     * @throws SQLException
     */
    public Set<GM> getGMCandidates() throws SQLServerException, SQLException {
        Set<GM> gmCandidates = new TreeSet<>();
        String sql = "SELECT * FROM Person p "
                + "WHERE p.ID "
                + "NOT IN "
                + "(SELECT PersonID FROM Volunteer)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gmCandidates.add(getOneGMCandidate(rs));
            }
        }
        return gmCandidates;
    }

    /**
     * Assign guild to manager
     *
     * @param id
     * @param guildName
     * @throws SQLServerException
     * @throws SQLException
     */
    public void assignGuildToManager(int id, String guildName) throws SQLServerException, SQLException {
        String sql = "INSERT INTO GuildManager (PersonID, GuildName) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, guildName);

            ps.executeUpdate();
        }
    }

    /**
     * Return one GM Candidate
     *
     * @param rs
     * @return
     */
    private GM getOneGMCandidate(ResultSet rs) throws SQLException {
        int ID = rs.getInt("ID");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String email = rs.getString("Email");
        int phone = rs.getInt("Phone");
        GM gm = new GM(ID, firstName, lastName, email, phone, "");
        return gm;
    }

    /**
     * Update the manager for the parsed guild
     *
     * @param id
     * @param guildName
     * @throws SQLServerException
     * @throws SQLException
     */
    public void updateGMForGuild(int id, String guildName) throws SQLServerException, SQLException {
        String sql = "UPDATE GuildManager "
                + "SET PersonID = ? "
                + "WHERE GuildName = ?";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, guildName);

            ps.executeUpdate();
        }
    }
}
