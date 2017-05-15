/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.ROIOverview;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
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

    private GuildModel guildModel;

    public ROIGmHoursViewController() {
        try {
            guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartPie.setLegendVisible(false);
        updateDataForChart();
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

}
