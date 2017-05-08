/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class StatisticsViewController implements Initializable {

    @FXML
    private BarChart<String, Integer> chartHoursOverview;

    private GuildModel guildModel;

    public StatisticsViewController() {
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
        updateDataForChart();
    }

    /**
     *
     * @param title
     */
    private void updateDataForChart() {
        chartHoursOverview.setLegendVisible(false);
        chartHoursOverview.getData().clear();
        List<Guild> guilds = guildModel.getGuildsFromDB();
        Map<String, Integer> guildHours = guildModel.getMapOfHoursPerGuild();

        XYChart.Series hoursSeries = new XYChart.Series<>();

        for (Guild guild : guilds) {
            hoursSeries.getData().add(new XYChart.Data<>(guild.getName(), guildHours.get(guild.getName())));
        }

        chartHoursOverview.getData().add(hoursSeries);
    }

}
