/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTabPane;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jxl.write.WriteException;
import museumtimetracking.MuseumTimeTracking;
import static museumtimetracking.be.enums.EAppLanguage.*;
import museumtimetracking.be.enums.EFXMLName;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.be.enums.ETabPaneID;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.ModelFacade;
import museumtimetracking.gui.views.ModalFactory;
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
    private HBox iconBox;
    @FXML
    private ImageView imgExcel;
    @FXML
    private ImageView imgScreenshot;
    @FXML
    private HBox languageBox;

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
    @FXML
    private Hyperlink btnLogin;
    @FXML
    private JFXSpinner spinnerUpdate;
    @FXML
    private Label lblUpdateData;
    @FXML
    private JFXButton btnUpdate;

    private static boolean online;

    private ModalFactory modalFactory;

    private ModelFacade modelFacade;

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

    private List<Tab> adminTabList;

    private String paneTabID;

    private final String LOGOUT_BTN_TEXT = MuseumTimeTracking.bundle.getString("Logout");
    private final String LOGIN_BTN_TEXT = MuseumTimeTracking.bundle.getString("Login");

    public static MTTMainControllerView getInstance() {
        return instance;
    }

    public MTTMainControllerView() {
        online = true;
        modelFacade = ModelFacade.getInstance();
        modalFactory = ModalFactory.getInstance();

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

        paneTabID = "statistics";

        adminTabList = new ArrayList<>();
    }

    @FXML
    private void handleDanish() {
        MuseumTimeTracking.changeLanguage(DANISH);
    }

    @FXML
    private void handleEnglish() {
        MuseumTimeTracking.changeLanguage(ENGLISH);
    }

    @FXML
    private void handleScreenshot() {
        WritableImage image = statistics.snapshot(new SnapshotParameters(), null);

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

        File img = fc.showSaveDialog(snackPane.getScene().getWindow());

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", img);
        } catch (IOException e) {
            System.out.println("Error: " + e);

        }
    }

    private void getLoginView() {
        Stage primStage = (Stage) imgHeader.getScene().getWindow();

        Stage loginModal = modalFactory.createNewModal(primStage, EFXMLName.LOGIN_VIEW);

        loginModal.showAndWait();
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

        setGuildManagerMode();

        btnLogin.setText(LOGIN_BTN_TEXT);
    }

    /**
     * Removes the tabs in the TapPanel.
     *
     * @param hide
     */
    public void setGuildManagerMode() {
        //Adds and saves the tabs to a list.
        adminTabList.add(tabGM);
        adminTabList.add(tabPaneArchivedGuild);
        adminTabList.add(tabPaneActiveGuild);
        //Removes the tabs.
        tabPane.getTabs().remove(tabPaneActiveGuild);
        tabPane.getTabs().remove(tabPaneArchivedGuild);
        tabPane.getTabs().remove(tabGM);

        languageBox.setVisible(true);
        languageBox.setDisable(false);
    }

    /**
     * Adds the tabButtons and sets the text in btnLabel. Makes the admin start
     * in statistics view.
     */
    public void setAdminMode() {
        addTabs();
        btnLogin.setText(LOGOUT_BTN_TEXT);
        tabPane.getSelectionModel().select(0);
        languageBox.setDisable(true);
        languageBox.setVisible(false);

    }

    /**
     * Adds the tabs from the list.
     */
    private void addTabs() {
        for (Tab tab : adminTabList) {
            tabPane.getTabs().add(1, tab);
        }
    }

    @FXML
    private void handleExportExcel() {
        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xls"));
            String location = fc.showSaveDialog(snackPane.getScene().getWindow()).getAbsolutePath();
            if (location != null) {
                ETabPaneID paneID = ETabPaneID.getTabByString(paneTabID);
                switch (paneID) {
                    case STATISTICS:
                        modelFacade.getGuildModel().exportGuildHoursToExcel(location);
                        String[] locationArray = location.split("\\.");
                        ModelFacade.getInstance().getGuildManagerModel().exportROIToExcel(locationArray[0] + "ROI." + locationArray[1]);
                        break;
                    case VOLUNTEER:
                        ModelFacade.getInstance().getVolunteerModel().exportVolunteerInfoToExcel(location);
                        break;
                    default:
                }
                displaySnackWarning(MuseumTimeTracking.bundle.getString("ExcelExported"));
            } else {
                displaySnackWarning(MuseumTimeTracking.bundle.getString("ExcelNotExported"));
            }
        } catch (IOException | DALException | WriteException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleGotoWebsite() throws MalformedURLException, URISyntaxException, IOException {
        URL website = new URL(WEBSITE);
        java.awt.Desktop.getDesktop().browse(website.toURI());
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
            paneTabID = newTab.getId();
            switch (paneTabID) {
                case "statistics":
                    setSearchBarVisible(false);
                    statisticsViewController.updateDataForGuildHoursOverview();
                    setScreenshotVisibility(true);
                    setExportToExcelVisibility(true);
                    StatisticsViewController.getInstance().handleGuild(null);
                    break;
                case "guildOverView":
                    guildOverViewController.refreshTable();
                    setExportToExcelVisibility(false);
                    break;
                case "volunteer":
                    setExportToExcelVisibility(true);
                    break;
                default:
                    setExportToExcelVisibility(false);
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
        ETabPaneID paneID = ETabPaneID.getTabByString(paneTabID);
        switch (paneID) {
            case GUILD_OVERVIEW:
                guildOverViewController.handleSearch(searchText);
                break;
            case ARCHIVED_GUILD:
                archivedGuildViewController.handleSearch(searchText);
                break;
            case GM:
                guildManagerOverviewController.handleSearch(searchText);
                break;
            case VOLUNTEER:
                volunteerOverviewController.handleSearch(searchText);
                break;
            case IDLE:
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

    /**
     * Compare the text in the hyperlink. If it is logout then show a alert. If
     * pressing yes set hyperlink to login and remove tabs.
     *
     * @param event
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        if (btnLogin.getText().equals(LOGOUT_BTN_TEXT)) {
            Alert alert = new AlertFactory().createLogoutAlert();
            alert.showAndWait().ifPresent(type -> {
                //If the first button ("YES") is clicked.
                if (type == alert.getButtonTypes().get(0)) {
                    btnLogin.setText(LOGIN_BTN_TEXT);
                    setGuildManagerMode();
                    tabPane.getSelectionModel().select(0);
                }
            });
        } else {
            if (MuseumTimeTracking.LOCALE.get().equals(ENGLISH.toString())) {
                Alert loginAlert = new AlertFactory().createAlertWithoutCancel(Alert.AlertType.CONFIRMATION, "The program is currently in english\nDanish language will be loaded instead");
                loginAlert.showAndWait().ifPresent((button) -> {
                    handleDanish();
                });
            } else {
                getLoginView();
            }
        }
        // Resets the hyperlink.
        btnLogin.setVisited(false);
    }

    @FXML
    public void handleUpdate() {
        if (online) {
            ModelFacade.getInstance().getGuildModel().updateData();
            ModelFacade.getInstance().getGuildManagerModel().updateData();
            ModelFacade.getInstance().getVolunteerModel().updateData();
        } else {
            displaySnackWarning(MuseumTimeTracking.bundle.getString("OfflineWarning"));
        }
    }

    /**
     * Show the update process
     *
     * @param shown
     */
    public void showUpdate(boolean shown) {
        btnUpdate.setVisible(!shown);
        btnUpdate.setDisable(shown);
        spinnerUpdate.setVisible(shown);
        lblUpdateData.setVisible(shown);
    }

    /**
     *
     * @return online status
     */
    public static boolean isOnline() {
        return online;
    }

    /**
     * Set the online status of the program
     *
     * @param value as boolean (true == online)
     */
    public static void updateOnlineStatus(boolean value) {
        online = value;
    }

    /**
     * Show or hide export to excel
     *
     * @param shown
     */
    public void setExportToExcelVisibility(boolean shown) {
        imgExcel.setVisible(shown);
        imgExcel.setDisable(!shown);
    }

    /**
     * Show or hide screenshot btn
     *
     * @param shown
     */
    public void setScreenshotVisibility(boolean shown) {
        imgScreenshot.setVisible(shown);
        imgScreenshot.setDisable(!shown);
    }

}
