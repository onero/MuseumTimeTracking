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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;

/**
 *
 * @author Skovgaard
 */
public class GuildDAO {

    private final DBConnectionManager cm;

    public GuildDAO() throws IOException {
        this.cm = DBConnectionManager.getInstance();
    }

    /**
     * Archive the guild
     *
     * @param guildToArchive
     * @throws java.sql.SQLException
     */
    public void archiveGuild(Guild guildToArchive) throws SQLException {
        String sql = "UPDATE Guild "
                + "SET IsArchived = 1 "
                + "WHERE Name = ?";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, guildToArchive.getName());

            ps.executeUpdate();
        }
    }

    /**
     *
     * @return all guilds from DB
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public List<Guild> getAllGuildsNotArchived() throws SQLServerException, SQLException {
        List<Guild> guilds = new ArrayList<>();
        String sql = "SELECT * FROM Guild "
                + "WHERE IsArchived = 0";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guilds.add(getOneGuild(rs, con));
            }
        }
        return guilds;
    }

    /**
     *
     * @return all guilds from DB
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public List<Guild> getAllGuildsArchived() throws SQLServerException, SQLException {
        List<Guild> guilds = new ArrayList<>();
        String sql = "SELECT * FROM Guild "
                + "WHERE IsArchived = 1";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guilds.add(getOneGuild(rs, con));
            }
        }
        return guilds;
    }

    /**
     * Add Guilds to DB.
     *
     * @param newGuild
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void addGuild(Guild newGuild) throws SQLServerException, SQLException {
        String sql = "INSERT INTO Guild "
                + "(Name, Description) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newGuild.getName());
            ps.setString(2, newGuild.getDescription());

            ps.executeUpdate();

        }
    }

    /**
     * Get the manager for the guild
     *
     * @param guildName
     * @return
     * @throws SQLServerException
     * @throws SQLException
     */
    private GM getMangerForGuild(Connection con, String guildName) throws SQLServerException, SQLException {
        String sql = "SELECT p.ID, "
                + "p.FirstName, "
                + "p.LastName, "
                + "p.Email, "
                + "p.Phone, "
                + "gm.Description "
                + "FROM Person p "
                + "JOIN GuildManager gm ON gm.PersonID = p.ID "
                + "WHERE gm.GuildName = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, guildName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
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
        return null;
    }

    // Get one guild from DB.
    private Guild getOneGuild(ResultSet rs, Connection con) throws SQLException {
        String name = rs.getString("Name");
        String description = rs.getString("Description");
        boolean isArchived = rs.getBoolean("IsArchived");

        Guild guild = new Guild(name, description, isArchived);
        GM gm = getMangerForGuild(con, guild.getName());
        if (gm != null) {
            guild.setGuildManager(gm);
        }

        return guild;
    }

    /**
     * Deletes guild from the tableView and DB. Comes from GuildManager and goes
     * to DB.
     *
     * @param deleteGuild
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void deleteGuild(Guild deleteGuild) throws SQLServerException, SQLException {
        String sql = "DELETE FROM Guild "
                + " WHERE Name = ?";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, deleteGuild.getName());

            ps.executeUpdate();
        }
    }

    /**
     * Restore guild from archive
     *
     * @param guildToRestore
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void restoreGuild(Guild guildToRestore) throws SQLServerException, SQLException {
        String sql = "UPDATE Guild "
                + "SET IsArchived = 0 "
                + "WHERE Name = ?";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, guildToRestore.getName());

            ps.executeUpdate();
        }
    }

    /**
     * Update guild in DB
     *
     * @param guildToUpdate
     * @param updatedGuild
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) throws SQLServerException, SQLException {
        String sql = "UPDATE Guild "
                + "SET Name = ?, Description = ? "
                + "WHERE Name = ?";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, updatedGuild.getName());
            ps.setString(2, updatedGuild.getDescription());
            ps.setString(3, guildToUpdate);

            ps.executeUpdate();

        }
    }

    /**
     * Returns a Map containing all the hours worked for each guild. Key = Name
     * of the guild. Value = hours worked.
     *
     * @param guildNames
     * @return
     * @throws SQLException
     */
    public Map<String, Integer> getVolunteerWorkHours(List<String> guildNames) throws SQLException {
        Map<String, Integer> workHours = new HashMap<>();
        try (Connection con = cm.getConnection()) {
            for (String guildName : guildNames) {
                int hours = getAllVoluteerHoursForOneGuild(con, guildName);
                workHours.put(guildName, hours);
            }
        }
        return workHours;
    }

    /**
     * Returns all the volunteer hours for at single guild.
     *
     * @param con
     * @param guildName
     * @return
     * @throws SQLException
     */
    private int getAllVoluteerHoursForOneGuild(Connection con, String guildName) throws SQLException {
        String sql = "SELECT Hours "
                + "FROM VolunteerWork "
                + "WHERE guildName = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, guildName);

        ResultSet rs = ps.executeQuery();
        int hours = 0;
        while (rs.next()) {
            hours += rs.getInt("Hours");
        }
        return hours;
    }

    /**
     * Gets the Guilds from the DB that has no GuildManager connected.
     *
     * @return
     * @throws SQLException
     */
    public List<Guild> getGuildsWithoutManagers() throws SQLException {
        List<Guild> guilds = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Guild g "
                + "WHERE g.Name "
                + "NOT IN "
                + "(SELECT gm.GuildName FROM GuildManager gm)";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guilds.add(getOneGuild(rs, con));
            }
        }
        return guilds;
    }

    List<String> getGuildsAVolunteerHasWorkedOn(Volunteer volunteer) throws SQLException {
        List<String> guilds = new ArrayList<>();

        String sql = "SELECT g.Name "
                + "FROM Guild g "
                + "JOIN VolunteerWork vw ON vw.GuildName = g.Name "
                + "WHERE vw.VolunteerID = ?";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, volunteer.getID());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guilds.add(rs.getString("Name"));
            }
        }
        return guilds;
    }
}
