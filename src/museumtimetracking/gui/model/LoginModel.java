/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import museumtimetracking.bll.LoginManager;

/**
 *
 * @author Skovgaard
 */
public class LoginModel {

    private final LoginManager loginManager;

    public LoginModel() {
        loginManager = LoginManager.getInstance();
    }

    public boolean validPassword(String password) {
        boolean validPassword = loginManager.validPassword(password);
        return validPassword;
    }

    public boolean validUsername(String username) {
        boolean validUsername = loginManager.validUsername(username);
        return validUsername;
    }

}
