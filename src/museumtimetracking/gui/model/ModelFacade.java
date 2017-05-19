/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;

public class ModelFacade {

    private static ModelFacade instance;

    private GuildModel guildModel;
    private GuildManagerModel guildManagerModel;
    private VolunteerModel volunteerModel;
    private LoginModel loginModel;

    public static ModelFacade getInstance() {
        if (instance == null) {
            instance = new ModelFacade();
        }
        return instance;
    }

    private ModelFacade() {
        try {
            guildModel = new GuildModel(true);
            guildManagerModel = new GuildManagerModel(true);
            volunteerModel = new VolunteerModel(true);
            loginModel = new LoginModel();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);

        }
    }

    public GuildModel getGuildModel() {
        return guildModel;
    }

    public GuildManagerModel getGuildManagerModel() {
        return guildManagerModel;
    }

    public VolunteerModel getVolunteerModel() {
        return volunteerModel;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

}
