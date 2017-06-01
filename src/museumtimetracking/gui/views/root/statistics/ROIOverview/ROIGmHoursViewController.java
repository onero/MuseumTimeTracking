/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.ROIOverview;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.ModelFacade;

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
    private TableView<Guild> tableROI;
    @FXML
    private TableColumn<Guild, String> clmName;
    @FXML
    private TableColumn<Guild, String> clmInvestment;

    private GuildModel guildModel;
    @FXML
    private Spinner<Integer> spnHours;
    
    /**
     * The number startup for the spinner.
     */
    public static final int INITIAL_VALUE_FOR_GM = 8;

    public ROIGmHoursViewController() {
        guildModel = ModelFacade.getInstance().getGuildModel();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartPie.setLabelsVisible(true);
        chartPie.setLegendVisible(false);
        updateDataForChart();
        initializeTable();
        initializeSpinner();
        addListener();

        //Set a search listener on serach textfield
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            guildModel.searchGuilds(newValue);
        });
    }

    /**
     * Updates the Guild ROI with the new selected hours.
     *
     * @param gmHours
     */
    private void updateGuildROIWithSelectedGmHour(int gmHours) {
        try {
            guildModel.updateGuildROI(gmHours);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
        updateDataForChart();
    }

    /**
     * Listens if the spinner gets a new value.
     */
    private void addListener() {
        spnHours.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue != oldValue) {
                    updateGuildROIWithSelectedGmHour(newValue);
                    tableROI.refresh();
                }
            }
        });
    }

    /**
     * Sets the min, max and init value on the spinner.
     */
    private void initializeSpinner() {
        int minValue = 1;
        int maxValue = 248; //31 dage * 8 arbejdstimer.
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, INITIAL_VALUE_FOR_GM);
        spnHours.setValueFactory(valueFactory);
    }

    /**
     * Updates the pieChart with ROI data.
     */
    public void updateDataForChart() {
        Map<String, Integer> ROIHours = guildModel.getGuildROI();

        ObservableList<Data> chartData = FXCollections.observableArrayList();
        if (!ROIHours.isEmpty()) {
            for (Map.Entry<String, Integer> entry : ROIHours.entrySet()) {
                chartData.add(new Data(entry.getKey(), entry.getValue()));
            }
            chartPie.setData(chartData);
        }
    }

    /**
     * Sets the items in the tableview and specifies what data each column
     * holds.
     */
    private void initializeTable() {
        tableROI.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableROI.setItems(guildModel.getCachedGuilds());
        //Sets the name of each guild in the column.
        clmName.setCellValueFactory(g -> g.getValue().getNameProperty());
        //Checks if the guild has ROI. If yes - displays it. Else display 0.
        clmInvestment.setCellValueFactory(g -> {
            if (guildModel.getGuildROI().get(g.getValue().getName()) != null) {
                return new SimpleStringProperty(guildModel.getGuildROI().get(g.getValue().getName()) + "");
            }
            return new SimpleStringProperty(0 + "");
        });
    }

    public void clearSearch() {
        txtSearchBar.clear();
    }
}
