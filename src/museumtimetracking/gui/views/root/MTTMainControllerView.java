/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import com.jfoenix.controls.JFXTabPane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.activeGuilds.GuildOverviewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class MTTMainControllerView implements Initializable {

    @FXML
    private Tab tabGM;
    @FXML
    private Tab tabIdle;
    @FXML
    private Tab tabPaneActiveGuild;
    @FXML
    private Tab tabPaneArchivedGuild;
    @FXML
    private Tab tabStatistics;
    @FXML
    private Tab tabVolunteer;
    @FXML
    private TextField txtSearchBar;
    @FXML
    private JFXTabPane tabPane;

    private final Node statistics;
    private final Node guildOverView;
    private final Node archivedGuild;
    private final Node manager;
    private final Node volunteer;
    private final Node idle;

    private GuildOverviewController guildOverViewController;

    private final NodeFactory nodeFactory;

    private String searchID;

    public MTTMainControllerView() {
        nodeFactory = NodeFactory.getInstance();

        statistics = nodeFactory.createNewView(STATISTICS_OVERVIEW);
        guildOverView = nodeFactory.createNewView(ACTIVE_GUILD);
        guildOverViewController = nodeFactory.getLoader().getController();
        archivedGuild = nodeFactory.createNewView(ARCHIVED_GUILD);

        manager = nodeFactory.createNewView(MANAGER_OVERVIEW);

        volunteer = nodeFactory.createNewView(VOLUNTEER_OVERVIEW);

        idle = nodeFactory.createNewView(IDLE_OVERVIEW);

        searchID = "";
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setContentOfTabs();
        initializeTabPane();
    }

    /**
     * Interpolate external views into the TabPanes
     */
    private void setContentOfTabs() {
        tabStatistics.setContent(statistics);
        tabPaneActiveGuild.setContent(guildOverView);
        tabPaneArchivedGuild.setContent(archivedGuild);
        tabGM.setContent(manager);
        tabVolunteer.setContent(volunteer);
        tabIdle.setContent(idle);
    }

    @FXML
    private void handleClearSearchBar() {
        txtSearchBar.clear();
    }

    private void initializeTabPane() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            searchID = newTab.getId();
        });
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        switch (searchID) {
            case "statistics":
                break;
            case "guildOverView":
                guildOverViewController.handleSearch(txtSearchBar.getText());
                break;
            case "archivedGuild":

                break;
            case "manager":

                break;
            case "volunteer":

                break;
            case "idle":

                break;
            default:
                break;
        }
    }
}
