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

    private static LoginModel instance;

    private final LoginManager loginManager;

    public static LoginModel getInstance() {
        if (instance == null) {
            instance = new LoginModel();
        }
        return instance;
    }

    private LoginModel() {
        loginManager = LoginManager.getInstance();
    }
    
    public boolean validateAdminLogin(String username, String password){
        return loginManager.validateAdminLogin(username, password);
    }

    public boolean userExsist(String username) {
        return loginManager.userExsist(username);
    }

}
