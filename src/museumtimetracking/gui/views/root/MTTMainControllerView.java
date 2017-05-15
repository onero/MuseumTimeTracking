/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.activeGuilds.GuildOverviewController;
import museumtimetracking.gui.views.root.archivedGuilds.ArchivedGuildViewController;
import museumtimetracking.gui.views.root.guildManager.guildManagerOverview.GuildManagerOverviewController;
import museumtimetracking.gui.views.root.idle.IdleViewController;
import museumtimetracking.gui.views.root.statistics.StatisticsViewController;
import museumtimetracking.gui.views.root.volunteer.VolunteerOverviewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class MTTMainControllerView implements Initializable {

    @FXML
    private Pane snackPane;

    @FXML
    private JFXSnackbar snackWarning;

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
    @FXML
    private ImageView imgHeader;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnClearSearch;

    private static MTTMainControllerView instance;

    private final Node statistics;
    private final Node guildOverView;
    private final Node archivedGuild;
    private final Node manager;
    private final Node volunteer;
    private final Node idle;

    public static final String WEBSITE = "http://www.levendehistorie.dk/";

    private final StatisticsViewController statisticsViewController;
    private final GuildOverviewController guildOverViewController;
    private final ArchivedGuildViewController archivedGuildViewController;
    private final GuildManagerOverviewController guildManagerOverviewController;
    private final VolunteerOverviewController volunteerOverviewController;
    private final IdleViewController idleViewController;

    private final NodeFactory nodeFactory;

    private String searchID;

    public static MTTMainControllerView getInstance() {
        return instance;
    }

    public MTTMainControllerView() {
        nodeFactory = NodeFactory.getInstance();

        statistics = nodeFactory.createNewView(STATISTICS_OVERVIEW);
        statisticsViewController = nodeFactory.getLoader().getController();

        statisticsViewController.createStatisticsViews();

        guildOverView = nodeFactory.createNewView(ACTIVE_GUILD);
        guildOverViewController = nodeFactory.getLoader().getController();

        archivedGuild = nodeFactory.createNewView(ARCHIVED_GUILD);
        archivedGuildViewController = nodeFactory.getLoader().getController();

        manager = nodeFactory.createNewView(MANAGER_OVERVIEW);
        guildManagerOverviewController = nodeFactory.getLoader().getController();

        volunteer = nodeFactory.createNewView(VOLUNTEER_OVERVIEW);
        volunteerOverviewController = nodeFactory.getLoader().getController();

        idle = nodeFactory.createNewView(IDLE_OVERVIEW);
        idleViewController = nodeFactory.getLoader().getController();

        searchID = "";
    }

    @FXML
    private void handleGotoWebsite() throws MalformedURLException, URISyntaxException, IOException {
        URL website = new URL(WEBSITE);
        java.awt.Desktop.getDesktop().browse(website.toURI());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;

        snackWarning = new JFXSnackbar(snackPane);

        setContentOfTabs();

        imgHeader.fitWidthProperty().bind(borderPane.widthProperty());
        initializeTabPane();
        initializeTextFieldListener();
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

    /**
     * Clears the searchBar. Checks if null to avoid nullpointer exception on
     * tab initilization.
     */
    @FXML
    private void handleClearSearchBar() {
        if (txtSearchBar != null) {
            txtSearchBar.clear();
        }
    }

    /**
     * Adds a listener for the tabs, so that the searchID gets updated on
     * changed. Also makes the searchbar invisible on statistics view.
     */
    private void initializeTabPane() {
        setSearchBarVisible(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            searchID = newTab.getId();
            if (searchID.equals("statistics")) {
                setSearchBarVisible(false);
                statisticsViewController.updateDataForGuildHoursOverview();
            } else {
                setSearchBarVisible(true);
            }
        });
    }

    /**
     * Checks the searchID and calls the right controller accordingly.
     *
     * @param searchText
     */
    private void handleSearch(String searchText) {
        switch (searchID) {
            case "guildOverView":
                guildOverViewController.handleSearch(searchText);
                break;
            case "archivedGuild":
                archivedGuildViewController.handleSearch(searchText);
                break;
            case "manager":
                guildManagerOverviewController.handleSearch(searchText);
                break;
            case "volunteer":
                volunteerOverviewController.handleSearch(searchText);
                break;
            case "idle":
                idleViewController.handleSearch(searchText);
                break;
            default:
                break;
        }
    }

    /**
     * Adds a listener on the textField so that it can update the lists with the
     * text written.
     */
    private void initializeTextFieldListener() {
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO GRØN: Make an abstract controller and an interface for the models.
            handleSearch(newValue);
        });
    }

    /**
     * Sets visibility and disable on the seach textField and button.
     *
     * @param shown
     */
    private void setSearchBarVisible(boolean shown) {
        txtSearchBar.setVisible(shown);
        txtSearchBar.setDisable(!shown);
        btnClearSearch.setVisible(shown);
        btnClearSearch.setDisable(!shown);
    }

    /**
     * Display warning in snackbar
     *
     * @param text
     */
    public void displaySnackWarning(String text) {
        snackWarning.show(text, 3000);
    }

}
