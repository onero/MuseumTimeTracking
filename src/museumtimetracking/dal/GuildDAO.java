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
import museumtimetracking.be.Guild;

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
                guilds.add(getOneGuild(rs));
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
                guilds.add(getOneGuild(rs));
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

    // Get one guild from DB.
    private Guild getOneGuild(ResultSet rs) throws SQLException {
        String name = rs.getString("Name");
        String description = rs.getString("Description");
        boolean isArchived = rs.getBoolean("IsArchived");

        Guild guild = new Guild(name, description, isArchived);

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
}
