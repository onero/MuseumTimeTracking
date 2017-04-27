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
import java.util.logging.Level;
import java.util.logging.Logger;
import museumtimetracking.be.Guild;

/**
 *
 * @author Skovgaard
 */
public class GuildDAO {

    private static GuildDAO instance;

    private DBConnectionManager cm;

    public static GuildDAO getInstance() {
        if (instance == null) {
            instance = new GuildDAO();
        }
        return instance;
    }

    private GuildDAO() {
        cm = null;
        try {
            cm = DBConnectionManager.getInstance();
        } catch (IOException ex) {
            System.out.println("Couldn't connect to the database.");
            Logger.getLogger(GuildDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Guild> getAllGuilds() {
        List<Guild> guilds = new ArrayList<>();
        String sql = "SELECT * FROM Guild";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guilds.add(getOneGuild(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuildDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return guilds;
    }

    /**
     * Add Guilds to DB.
     *
     * @param newGuild
     */
    public void addGuild(Guild newGuild) {
        String sql = "INSERT INTO Guild "
                + "(Name, Description) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newGuild.getName());
            ps.setString(2, newGuild.getDescription());

            ps.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println("Couldn't add newGuild to DB");
            System.out.println(sqlException);
            Logger.getLogger(GuildDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    // Adds one guild to DB.
    private Guild getOneGuild(ResultSet rs) throws SQLException {
        String name = rs.getString("Name");
        String description = rs.getString("Description");
        boolean isArchived = rs.getBoolean("IsArchived");

        Guild guild = new Guild(name, description, isArchived);

        return guild;
    }

    /**
     * Update guild in DB
     *
     * @param guildToUpate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpate, Guild updatedGuild) {
        String sql = "UPDATE Guild "
                + "SET Name = ?, Description = ? "
                + "WHERE Name = ?";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, updatedGuild.getName());
            ps.setString(2, updatedGuild.getDescription());
            ps.setString(3, guildToUpate);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Couldn't update " + updatedGuild.getName());
            System.out.println(e);
        }
    }

}