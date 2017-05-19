/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.ModelFacade;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.statistics.ROIOverview.ROIGmHoursViewController;
import museumtimetracking.gui.views.root.statistics.VolunteerStatistics.VolunteerStatisticsViewController;
import museumtimetracking.gui.views.root.statistics.guildHoursOverview.ChartGuildHoursOverviewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class StatisticsViewController implements Initializable {

    @FXML
    private BorderPane borderpane;

    private GuildModel guildModel;

    private final NodeFactory nodeFactory;

    private Node guildHoursOverview;
    private Node ROIGmHours;
    private Node volunteerStatistics;

    private ChartGuildHoursOverviewController chartGuildHoursOverviewController;
    private ROIGmHoursViewController ROIGmHoursController;
    private VolunteerStatisticsViewController volunteerStatisticsViewController;

    public StatisticsViewController() {
        guildModel = ModelFacade.getInstance().getGuildModel();
        nodeFactory = NodeFactory.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Creates the nodes and controllers for the different statisticsViews.
     */
    public void createStatisticsViews() {
        guildHoursOverview = nodeFactory.createNewView(EFXMLName.CHART_GUILD_HOURS_OVERVIEW);
        chartGuildHoursOverviewController = nodeFactory.getLoader().getController();

        ROIGmHours = nodeFactory.createNewView(EFXMLName.ROI_GM_HOURS);
        ROIGmHoursController = nodeFactory.getLoader().getController();

        volunteerStatistics = nodeFactory.createNewView(EFXMLName.VOLUNTEER_STATISTICS);
        volunteerStatisticsViewController = nodeFactory.getLoader().getController();
        initialSetup();
    }

    /**
     * Sets the initial setup.
     */
    private void initialSetup() {
        borderpane.setCenter(guildHoursOverview);
        updateDataForGuildHoursOverview();
    }

    /**
     * Clears the data in the chart and fills it with freshly fetched data.
     *
     */
    public void updateDataForGuildHoursOverview() {
        chartGuildHoursOverviewController.updateDataForChart();
        ROIGmHoursController.updateDataForChart();
    }

    private void handleChangeStatisticsButton() {
        if (borderpane.getCenter() == guildHoursOverview) {
            borderpane.setCenter(ROIGmHours);
        } else {
            borderpane.setCenter(guildHoursOverview);
        }
    }

    @FXML
    private void handleGM(ActionEvent event) {
        borderpane.setCenter(ROIGmHours);
    }

    @FXML
    private void handleGuild(ActionEvent event) {
        borderpane.setCenter(guildHoursOverview);
    }

    @FXML
    private void handleVolunteer(ActionEvent event) {
        borderpane.setCenter(volunteerStatistics);
        volunteerStatisticsViewController.clearAll();
    }

}
