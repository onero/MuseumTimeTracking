/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    /**
     * Add Guilds to DB.
     * 
     * @param newGuild 
     */
    public void addGuild(Guild newGuild){
        String sql = "INSERT INTO Guild "
                + "(Name, Description) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()){
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

}
