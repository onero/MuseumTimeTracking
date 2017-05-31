/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import museumtimetracking.dal.db.LoginDAO;

/**
 *
 * @author Skovgaard
 */
public class LoginManager {

    private static LoginManager instance;

    private final LoginDAO loginDAO;

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    private LoginManager() {
        loginDAO = LoginDAO.getInstance();
    }

    /**
     * Validate the user and password.
     *
     * @param password
     * @return
     */
    public boolean validPassword(String password) {
        boolean validPassword = password.equals(loginDAO.getMockPassword());
        return validPassword;
    }

    public boolean validUsername(String username) {
        boolean validUsername = username.equals(loginDAO.getMockUsername());
        return validUsername;
    }
}
