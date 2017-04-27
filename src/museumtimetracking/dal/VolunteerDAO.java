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
import museumtimetracking.be.Volunteer;

/**
 *
 * @author Skovgaard
 */
public class VolunteerDAO {

    private DBConnectionManager cm;
    
    private static VolunteerDAO instance;

    public static VolunteerDAO getInstance() {
        if (instance == null) {
            instance = new VolunteerDAO();
        }
        return instance;
    }

    private VolunteerDAO() {
        cm = null;
        try {
            cm = DBConnectionManager.getInstance();
        } catch (IOException ex) {
            System.out.println("Couldn't connect to the database.");
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Add Volunteer to DB.
     *
     * @param newVolunteer
     */
    public void addVolunteer(Volunteer newVolunteer) {
        String sql = "INSERT INTO Volunteer "
                + "(PersonID, IsIdle) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, newVolunteer.getID());
            ps.setBoolean(2, newVolunteer.getIsIdle());

            ps.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println("Couldn't add newVolunteer to DB");
            System.out.println(sqlException);
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }
    
    
    
    
}
