/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import museumtimetracking.be.APerson;

/**
 *
 * @author Rasmus
 */
public abstract class APersonDAO {

    /**
     * Creates a new person in the database and returns that persons ID.
     *
     * @param con
     * @param person
     * @return
     * @throws SQLException
     */
    public int createNewPersonInDatabase(Connection con, APerson person) throws SQLException {
        String sql = "INSERT INTO Person "
                + "(FirstName, LastName, Email, Phone) "
                + "VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setString(3, person.getEmail());
        ps.setInt(4, person.getPhone());

        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    /**
     * Updates the parsed person in the database.
     *
     * @param con
     * @param person
     * @throws SQLException
     */
    public void updatePersonInformation(Connection con, APerson person) throws SQLException {
        String sql = "UPDATE Person "
                + "SET FirstName = ?, "
                + "LastName = ?, "
                + "Email = ?, "
                + "Phone = ? "
                + "WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setString(3, person.getEmail());
        ps.setInt(4, person.getPhone());
        ps.setInt(5, person.getID());

        ps.executeUpdate();
    }

}
