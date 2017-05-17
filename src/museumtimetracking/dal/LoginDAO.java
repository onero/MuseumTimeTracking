/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

/**
 *
 * @author Skovgaard
 */
public class LoginDAO {
    
    private static LoginDAO instance;

    private DBConnectionManager cm;

    public static LoginDAO getInstance() {
        if (instance == null) {
            instance = new LoginDAO();
        }
        return instance;
    }
}
