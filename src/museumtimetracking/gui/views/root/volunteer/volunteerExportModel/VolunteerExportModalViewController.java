/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.volunteerExportModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.write.WriteException;
import museumtimetracking.MuseumTimeTracking;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.ModelFacade;
import museumtimetracking.gui.views.root.MTTMainControllerView;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class VolunteerExportModalViewController implements Initializable {

    @FXML
    private Button btnCancel;

    private Stage thisStage;

    private Pane snackPane;

    private MTTMainControllerView controller;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleAllVolunteersButton() {
        exportToExcel(true);
        handleCancelButton();
    }

    @FXML
    private void handleActiveVolunteersButton() {
        exportToExcel(false);
        handleCancelButton();
    }

    /**
     * Finds the location to save the export. Then exports the data.
     *
     * @param inactiveIncluded If true - Includes both active and inactive
     * volunteers in the export. If false - Only inlcudes active volunteers.
     */
    private void exportToExcel(boolean inactiveIncluded) {
        String location = selectPatch();
        if (location != null) {
            try {
                ModelFacade.getInstance().getVolunteerModel().exportVolunteerInfoToExcel(location, inactiveIncluded);
                controller.displaySnackWarning(MuseumTimeTracking.bundle.getString("ExcelExported"));
            } catch (IOException | WriteException | DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        } else {
            controller.displaySnackWarning(MuseumTimeTracking.bundle.getString("ExcelNotExported"));
        }
    }

    @FXML
    private void handleCancelButton() {
        thisStage.close();
    }

    public void setControllerAndPane(MTTMainControllerView controller, Pane snackPane) {
        this.controller = controller;
        this.snackPane = snackPane;
        thisStage = (Stage) btnCancel.getScene().getWindow();
    }

    private String selectPatch() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xls"));
        return fc.showSaveDialog(snackPane.getScene().getWindow()).getAbsolutePath();
    }

}
