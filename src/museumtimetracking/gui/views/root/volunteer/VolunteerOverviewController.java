/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import museumtimetracking.MuseumTimeTracking;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.EFXMLName;
import static museumtimetracking.be.enums.EFXMLName.ADD_HOURS_VOLUNTEER;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.ModelFacade;
import museumtimetracking.gui.model.VolunteerModel;
import museumtimetracking.gui.views.ModalFactory;
import museumtimetracking.gui.views.root.volunteer.addHours.AddVolunteersHoursViewController;
import museumtimetracking.gui.views.root.volunteer.volunteerInfo.VolunteerInfoViewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class VolunteerOverviewController implements Initializable {

    @FXML
    private Button btnEdit;
    @FXML
    private ImageView imgProfile;
    @FXML
    private ToggleGroup language;
    @FXML
    private Label lblVolunteerAmount;
    @FXML
    private ListView<Volunteer> lstVolunteer;
    @FXML
    private RadioButton radioDA;
    @FXML
    private RadioButton radioDE;
    @FXML
    private RadioButton radioENG;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextArea txtVolunteerInfo;
    @FXML
    private Button btnAddVolunteer;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnMakeInactive;
    @FXML
    private Button btnDocument;

    public static final String NO_PHOTO = "/museumtimetracking/asset/img/no-photo.jpg";
    private static final String NO_VOLUNTEER = MuseumTimeTracking.bundle.getString("NoVolunteerSelected");

    private VolunteerModel volunteerModel;

    private final ModalFactory modalFactory;

    private Stage primStage;

    private Volunteer selectedVolunteer;
    @FXML
    private ButtonBar volunteerOptions;

    public VolunteerOverviewController() {
        modalFactory = ModalFactory.getInstance();
        volunteerModel = ModelFacade.getInstance().getVolunteerModel();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lstVolunteer.setItems(volunteerModel.getCachedVolunteers());
        setVolunteerCellFactory();
        setColorToBlack();
        setTextVisibility(false);

        setVolunteerOptionsVisibility(false);

        lblVolunteerAmount.textProperty().bind(Bindings.size((volunteerModel.getCachedVolunteers())).asString());
    }

    /**
     * Set visibility of the volunteer options bar
     *
     * @param shown
     */
    private void setVolunteerOptionsVisibility(boolean shown) {
        volunteerOptions.setDisable(!shown);
        volunteerOptions.setVisible(shown);
    }

    private void setTextVisibility(boolean value) {
        txtFirstName.setDisable(!value);
        txtLastName.setDisable(!value);
        txtEmail.setDisable(!value);
        txtPhone.setDisable(!value);
        txtVolunteerInfo.setDisable(!value);
    }

    /**
     * sets the color of the textFields.
     *
     * @param color
     */
    private void setColor(String color) {
        txtFirstName.setStyle("-fx-text-fill: " + color + ";");
        txtLastName.setStyle("-fx-text-fill: " + color + ";");
        txtEmail.setStyle("-fx-text-fill: " + color + ";");
        txtPhone.setStyle("-fx-text-fill: " + color + ";");
        txtVolunteerInfo.setStyle("-fx-text-fill: " + color + ";");
    }

    private void setColorToBlack() {
        setColor("black");
    }

    private void setColorToOrange() {
        setColor("#c18100");
    }

    private void handleVolunteerInfo() throws IOException {
        primStage = (Stage) btnEdit.getScene().getWindow();

        Stage volunteerInfoModal = modalFactory.createNewModal(primStage, EFXMLName.VOLUNTEER_INFO);
        VolunteerInfoViewController controller = modalFactory.getLoader().getController();
        controller.setCurrentVolunteer(selectedVolunteer);

        volunteerInfoModal.show();
    }

    /**
     * For each Volunteer in the list, show only their full name
     */
    private void setVolunteerCellFactory() {
        lstVolunteer.setCellFactory(v -> new ListCell<Volunteer>() {
            @Override
            protected void updateItem(Volunteer volunteer, boolean empty) {
                super.updateItem(volunteer, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(volunteer.getFullName());
                }
            }

        });
    }

    /**
     * Sends the selected Volunteer through the layers to delete it and returns
     * the buttons to view mode.
     */
    @FXML
    private void handleDeleteVolunteer() {
        Volunteer volunteerToDelete = lstVolunteer.getSelectionModel().getSelectedItem();
        if (volunteerToDelete != null) {
            if (btnDelete.getText().equals(MuseumTimeTracking.bundle.getString("Delete"))) {
                Alert deleteAlert = new AlertFactory().createDeleteAlert();
                deleteAlert.showAndWait().ifPresent(type -> {
                    //If user clicks first button
                    if (type == deleteAlert.getButtonTypes().get(0)) {
                        try {
                            volunteerModel.deleteVolunteer(volunteerToDelete);
                        } catch (DALException ex) {
                            ExceptionDisplayer.display(ex);
                        }
                    }
                });
            } else if (btnDelete.getText().equals(MuseumTimeTracking.bundle.getString("Cancel"))) {
                pressingSave();
                showButtons();
            }
        }
        lstVolunteer.refresh();
    }

    @FXML
    private void handleEditVolunteer() {
        selectedVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
        //Validate that a volunteer is selected
        if (selectedVolunteer != null) {
            //If we're not in edit mode
            if (btnEdit.getText().equalsIgnoreCase(MuseumTimeTracking.bundle.getString("Edit"))) {
                pressingCancel();
                //If we are in edit mode
            } else {
                pressingSave();
                updateVolunteer();
                //Update volunteer in DB
                try {
                    ModelFacade.getInstance().getVolunteerModel().updateVolunteer(selectedVolunteer);
                } catch (DALException ex) {
                    ExceptionDisplayer.display(ex);
                }
                showButtons();
            }
        }
        lstVolunteer.refresh();
    }

    private void pressingCancel() {
        btnEdit.setText(MuseumTimeTracking.bundle.getString("Save"));
        btnDelete.setText(MuseumTimeTracking.bundle.getString("Cancel"));
        setTextVisibility(true);
        lstVolunteer.setDisable(true);
        setColorToOrange();
        hideButtons();
    }

    private void pressingSave() {
        btnEdit.setText(MuseumTimeTracking.bundle.getString("Edit"));
        btnDelete.setText(MuseumTimeTracking.bundle.getString("Delete"));
        setTextVisibility(false);
        lstVolunteer.setDisable(false);
        setColorToBlack();
    }

    /**
     * Update the information for the volunteer
     *
     * @throws NumberFormatException
     */
    public void updateVolunteer() {
        // Select the volunteer from the list and updates the new info.
        selectedVolunteer.setFirstName(txtFirstName.getText());
        selectedVolunteer.setLastName(txtLastName.getText());
        selectedVolunteer.setEmail(txtEmail.getText());
        selectedVolunteer.setPhone(Integer.parseInt(txtPhone.getText()));
        selectedVolunteer.setDescription(txtVolunteerInfo.getText());
        selectedVolunteer.updateFullName();
    }

    @FXML
    private void handleNewVolunteer() {
        primStage = (Stage) btnEdit.getScene().getWindow();
        Stage newVolunteerModal = modalFactory.createNewModal(primStage, EFXMLName.ADD_NEW_VOLUNTEER);
        newVolunteerModal.show();
    }

    @FXML
    private void handleSelectVolunteer(MouseEvent event) {
        selectedVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
        // Doubleclick to edit the volunteer.
        if (event.getClickCount() == 2) {
            handleEditVolunteer();
        }

        setVolunteerOptionsVisibility(true);

        txtFirstName.setText(selectedVolunteer.getFirstName());
        txtLastName.setText(selectedVolunteer.getLastName());
        txtEmail.setText(selectedVolunteer.getEmail());
        txtPhone.setText("" + selectedVolunteer.getPhone());
        txtVolunteerInfo.setText(selectedVolunteer.getDescription());
        selectVolunteerLanguage(selectedVolunteer);

        if (selectedVolunteer.getImage() != null) {
            imgProfile.setImage(selectedVolunteer.getImage());
        } else {
            Image img = new Image(this.getClass().getResourceAsStream(NO_PHOTO));
            imgProfile.setImage(img);
        }
    }

    /**
     * Select the volunteer language
     *
     * @param selectedVolunteer
     */
    private void selectVolunteerLanguage(Volunteer selectedVolunteer) {
        switch (selectedVolunteer.getLanguage()) {
            case DANISH:
                radioDA.setSelected(true);
                break;
            case ENGLISH:
                radioENG.setSelected(true);
                break;
            case GERMAN:
                radioDE.setSelected(true);
                break;
            default:
        }
    }

    @FXML
    private void handleInactiveVolunteer() {
        selectedVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
        if (selectedVolunteer != null) {
            Stage inactiveInformationModal = modalFactory.createNewModal(primStage, EFXMLName.VOLUNTEER_INFO);
            VolunteerInfoViewController controller = modalFactory.getLoader().getController();
            controller.setCurrentVolunteer(selectedVolunteer);
            inactiveInformationModal.show();
        }
    }

    @FXML
    private void handleSelectVolunteerImage(MouseEvent event) throws IOException {
        primStage = (Stage) btnEdit.getScene().getWindow();
        if (event.getClickCount() == 1) {
            FileChooser fc = createFileChooser();
            File file = fc.showOpenDialog(primStage.getScene().getWindow());
            if (file != null) {
                try {
                    volunteerModel.setVolunteerImage(selectedVolunteer.getID(), file);
                    Image img = new Image(file.toURI().toASCIIString());
                    selectedVolunteer.setImage(img);
                    imgProfile.setImage(img);
                } catch (DALException | FileNotFoundException ex) {
                    ExceptionDisplayer.display(ex);
                }
            }
        }
    }

    @FXML
    private void handleDocumentHoursButton() {
        Volunteer volunteer = lstVolunteer.getSelectionModel().getSelectedItem();
        if (volunteer != null) {
            Stage primeryStage = (Stage) txtEmail.getScene().getWindow();
            Stage stage = modalFactory.createNewModal(primeryStage, ADD_HOURS_VOLUNTEER);
            AddVolunteersHoursViewController controller = modalFactory.getLoader().getController();
            controller.setVolunteer(volunteer);
            stage.show();
        } else {
            ExceptionDisplayer.display(new NullPointerException(NO_VOLUNTEER));
        }
    }

    /**
     * Create the file chooser
     *
     * @return
     */
    private FileChooser createFileChooser() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        return fc;
    }

    /**
     * Hides the buttons for: Adding a new volunteer. Deleting a volunteer.
     * Document hours for a volunteer. Make a volunteer inactive.
     */
    private void hideButtons() {
        hideButton(btnAddVolunteer);
        hideButton(btnDocument);
        hideButton(btnMakeInactive);
        radioDA.setDisable(false);
        radioDE.setDisable(false);
        radioENG.setDisable(false);
        imgProfile.setDisable(false);
    }

    /**
     * Shows the buttons for: Adding a new volunteer. Deleting a volunteer.
     * Document hours for a volunteer. Make a volunteer inactive.
     */
    private void showButtons() {
        showButton(btnAddVolunteer);
        showButton(btnDocument);
        showButton(btnMakeInactive);
        radioDA.setDisable(true);
        radioDE.setDisable(true);
        radioENG.setDisable(true);
        imgProfile.setDisable(true);
    }

    /**
     * Hides the parsed button.
     *
     * @param button
     */
    private void hideButton(Button button) {
        button.setDisable(true);
        button.setVisible(false);
    }

    /**
     * Shows the parsed button.
     *
     * @param button
     */
    private void showButton(Button button) {
        button.setDisable(false);
        button.setVisible(true);
    }

    /**
     * Makes the model search in the cachedLists
     *
     * @param searchText
     */
    public void handleSearch(String searchText) {
        volunteerModel.searchActiveVolunteers(searchText);
    }

}
