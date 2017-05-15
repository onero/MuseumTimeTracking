/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.guildHoursOverview;

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
 * @author Rasmus
 */
public class ChartGuildHoursOverviewController implements Initializable {

    @FXML
    private BarChart<String, Integer> chartHoursOverview;

    private GuildModel guildModel;

    public ChartGuildHoursOverviewController() {
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
        chartHoursOverview.setLegendVisible(false);
        updateDataForChart();
    }

    public void updateDataForChart() {
        chartHoursOverview.getData().clear();
        List<Guild> guilds = guildModel.getGuildsFromDB();
        Map<String, Integer> guildHours = null;
        try {
            guildHours = guildModel.getMapOfHoursPerGuild();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
        XYChart.Series hoursSeries = new XYChart.Series<>();

        for (Guild guild : guilds) {
            if (guildHours != null) {
                XYChart.Data data = new XYChart.Data<>(guild.getName(), guildHours.get(guild.getName()));
                hoursSeries.getData().add(data);
            }
        }
        //Adds the serie to the barChart.
        chartHoursOverview.getData().add(hoursSeries);
    }

}
