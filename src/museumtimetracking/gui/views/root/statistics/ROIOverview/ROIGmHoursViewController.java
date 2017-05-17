/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.ROIOverview;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class ROIGmHoursViewController implements Initializable {

    @FXML
    private PieChart chartPie;
    @FXML
    private TextField txtSearchBar;
    @FXML
    private ComboBox<Guild> cmbGuilds;
    @FXML
    private Label lblWeek;
    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;

    private GuildModel guildModel;

    public ROIGmHoursViewController() {
        try {
            guildModel = GuildModel.getInstance();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartPie.setLabelsVisible(false);
        chartPie.setLegendSide(Side.LEFT);
        updateDataForChart();
        initializeComboBox();
    }

    /**
     * Updates the pieChart with ROI data.
     */
    public void updateDataForChart() {
        List<Guild> guilds = guildModel.getGuildsFromDB();
        Map<String, Integer> ROIHours = null;
        try {
            ROIHours = guildModel.getGMROIOnVolunteerForAMonth(guilds, 2);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }

        ObservableList<Data> chartData = FXCollections.observableArrayList();
        if (ROIHours != null) {
            for (Map.Entry<String, Integer> entry : ROIHours.entrySet()) {
                chartData.add(new Data(entry.getKey(), entry.getValue()));
            }
            chartPie.setData(chartData);
        }
    }

    private void initializeComboBox() {
        cmbGuilds.setItems(guildModel.getCachedGuilds());

        if (!cmbGuilds.getItems().isEmpty()) {
            cmbGuilds.getSelectionModel().selectFirst();
            selectGuild();
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

    @FXML
    private void selectGuild() {
        Guild guild = cmbGuilds.getSelectionModel().getSelectedItem();
        if (guild != null) {
            int[] guildROI = guildModel.getROIForAGuild(guild.getName());
            lblWeek.setText(guildROI[0] + "");
            lblMonth.setText(guildROI[1] + "");
            lblYear.setText(guildROI[2] + "");
        }
    }
}
