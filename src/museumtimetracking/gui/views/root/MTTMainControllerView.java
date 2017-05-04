/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.guild.guildSearch.GuildSearchViewController;
import museumtimetracking.gui.views.root.guildManager.guildManagerSearch.GuildManagerSearchController;
import museumtimetracking.gui.views.root.volunteer.volunteerSearch.VolunteerSearchController;

/**
 *
 * @author gta1
 */
public class MTTMainControllerView implements Initializable {

    @FXML
    private BorderPane borderPaneMain;
    @FXML
    private BorderPane borderPaneMainAtBottomBorderPane;

    private final NodeFactory nodeFactory;

    private final Node guildOverview;
    private final Node managerOverview;
    private final Node volunteerOverview;
    private final Node archivedGuilds;
    private final Node idleVolunteer;
    private final Node guildSearch;
    private final Node guildManagerSearch;
    private final Node volunteerSearch;

    private final GuildSearchViewController guildSearchViewController;
    private final GuildManagerSearchController guildManagerSearchController;
    private final VolunteerSearchController volunteerSearchController;

    public MTTMainControllerView() {
        nodeFactory = NodeFactory.getInstance();

        guildOverview = nodeFactory.createNewView(GUILD_OVERVIEW);
        managerOverview = nodeFactory.createNewView(MANAGER_OVERVIEW);
        volunteerOverview = nodeFactory.createNewView(VOLUNTEER_OVERVIEW);
        archivedGuilds = nodeFactory.createNewView(ARCHIVED_TABLE);
        idleVolunteer = nodeFactory.createNewView(IDLE_VOLUNTEER);

        //Initializes the SearchViews.
        guildSearch = nodeFactory.createNewView(GUILD_SEARCH);
        guildSearchViewController = nodeFactory.getLoader().getController();
        guildManagerSearch = nodeFactory.createNewView(GUILD_MANAGER_SEARCH);
        guildManagerSearchController = nodeFactory.getLoader().getController();
        volunteerSearch = nodeFactory.createNewView(VOLUNTEER_SEARCH);
        volunteerSearchController = nodeFactory.getLoader().getController();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borderPaneMain.setCenter(guildOverview);
        borderPaneMain.setTop(guildSearch);
        borderPaneMainAtBottomBorderPane.setCenter(archivedGuilds);
    }

    @FXML
    private void handleGuildsButton() {
        borderPaneMain.setCenter(guildOverview);
        borderPaneMain.setTop(guildSearch);
        guildSearchViewController.clearSearchBar();
        borderPaneMainAtBottomBorderPane.setCenter(archivedGuilds);
    }

    @FXML
    private void handleGuildManagersButton() {
        borderPaneMain.setCenter(managerOverview);
        borderPaneMain.setTop(guildManagerSearch);
        guildManagerSearchController.clearSearchBar();
    }

    @FXML
    private void handleVolunteersButton() {
        borderPaneMain.setCenter(volunteerOverview);
        borderPaneMain.setTop(volunteerSearch);
        volunteerSearchController.clearSearchBar();
        borderPaneMainAtBottomBorderPane.setCenter(idleVolunteer);
    }
}
