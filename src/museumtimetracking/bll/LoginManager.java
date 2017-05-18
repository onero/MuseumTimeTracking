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
     * @param username
     * @param password
     * @return
     */
    public boolean validateAdminLogin(String username, String password) {
        boolean validUsername = username.equalsIgnoreCase(loginDAO.getMockUsername());
        boolean validPassword = password.equals(loginDAO.getMockPassword());
        return validUsername && validPassword;
    }

    public boolean userExsist(String username) {
        return username.equalsIgnoreCase(loginDAO.getMockUsername());
    }
}
