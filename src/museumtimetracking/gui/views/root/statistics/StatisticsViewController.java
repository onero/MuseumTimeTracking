/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import museumtimetracking.be.Guild;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.statistics.ROIOverview.ROIGmHoursViewController;
import museumtimetracking.gui.views.root.statistics.guildHoursOverview.ChartGuildHoursOverviewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class StatisticsViewController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private ComboBox<Guild> cmbGuilds;
    @FXML
    private TextField txtSearchBar;

    private GuildModel guildModel;

    private final NodeFactory nodeFactory;

    private Node guildHoursOverview;
    private Node ROIGmHours;

    private ChartGuildHoursOverviewController chartGuildHoursOverviewController;
    private ROIGmHoursViewController ROIGmHoursController;

    public StatisticsViewController() {
        try {
            guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
        nodeFactory = NodeFactory.getInstance();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeComboBox();

    }

    public void initializeComboBox() {
        cmbGuilds.setItems(guildModel.getCachedGuilds());

        if (!cmbGuilds.getItems().isEmpty()) {
            cmbGuilds.getSelectionModel().selectFirst();
        }

        //Fill combobox with guilds
        cmbGuilds.setCellFactory(gm -> new ListCell<Guild>() {
            @Override
            protected void updateItem(Guild guild, boolean empty) {
                super.updateItem(guild, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(guild.getName());
                }
            }
        });

        //Make sure that the guilds name is shown
        cmbGuilds.setButtonCell(
                new ListCell<Guild>() {
            @Override
            protected void updateItem(Guild guild, boolean bln) {
                super.updateItem(guild, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(guild.getName());
                }
            }
        });

        //Set a search listener on serach textfield
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            guildModel.searchGuilds(newValue);
        });
    }

    public void createStatisticsView() {
        guildHoursOverview = nodeFactory.createNewView(EFXMLName.CHART_GUILD_HOURS_OVERVIEW);
        chartGuildHoursOverviewController = nodeFactory.getLoader().getController();

        ROIGmHours = nodeFactory.createNewView(EFXMLName.ROI_GM_HOURS);
        ROIGmHoursController = nodeFactory.getLoader().getController();

        initialSetup();
    }

    private void initialSetup() {
        borderpane.setCenter(guildHoursOverview);
        updateDataForGuildHoursOverview();
    }

    @FXML
    private void selectGuild() {
        Guild selected = cmbGuilds.getSelectionModel().getSelectedItem();

        try {

            int total = guildModel.getGMROIOnVolunteerForAMonth(selected, 10);

            //TODO ALH&RKL: Connect with chart
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Clears the data in the chart and fills it with freshly fetched data.
     *
     */
    public void updateDataForGuildHoursOverview() {
        chartGuildHoursOverviewController.updateDataForChart();
    }

    @FXML
    private void handleChangeStatisticsButton() {
        if (borderpane.getCenter() == guildHoursOverview) {
            borderpane.setCenter(ROIGmHours);
        } else {
            borderpane.setCenter(guildHoursOverview);
        }
    }

}
