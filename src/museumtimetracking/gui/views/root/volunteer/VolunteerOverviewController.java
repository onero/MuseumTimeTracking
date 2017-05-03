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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.VolunteerModel;
import museumtimetracking.gui.views.ModalFactory;
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
    private Label txtLinkMoreInfo;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextArea txtVolunteerInfo;

    public static final String NO_PHOTO = "/museumtimetracking/asset/img/no-photo.jpg";

    private VolunteerModel volunteerModel;

    private final ModalFactory modalFactory;

    private Stage primStage;

    private Volunteer selectedVolunteer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtLinkMoreInfo.setVisible(false);

        lstVolunteer.setItems(volunteerModel.getCachedVolunteers());
        setVolunteerCellFactory();

        setTextVisibility(false);
    }

    public VolunteerOverviewController() {
        modalFactory = ModalFactory.getInstance();
        volunteerModel = null;
        try {
            volunteerModel = VolunteerModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    private void setTextVisibility(boolean value) {
        txtFirstName.setDisable(!value);
        txtLastName.setDisable(!value);
        txtEmail.setDisable(!value);
        txtPhone.setDisable(!value);
        txtVolunteerInfo.setDisable(!value);
    }

    @FXML
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
            public void updateItem(Volunteer volunteer, boolean empty) {
                super.updateItem(volunteer, empty);
                if (empty) {
                    textProperty().unbind();
                } else {
                    textProperty().bind(volunteer.getFullNameProperty());
                }
            }
        });
    }

    @FXML
    private void handleDeleteVolunteer() {
        String message = "Tryk 'Ja' for at slette permanent. \n Tryk 'Nej' for at fortryde.";
        Alert alert = AlertFactory.createAlert(Alert.AlertType.WARNING, message);
        alert.showAndWait().ifPresent(type -> {
            //If user clicks first button
            if (type == alert.getButtonTypes().get(0)) {
                Volunteer deleteVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
                try {
                    volunteerModel.deleteVolunteer(deleteVolunteer);
                } catch (DALException ex) {
                    ExceptionDisplayer.display(ex);
                }

            }
        });
    }

    @FXML
    private void handleEditVolunteer() {
        if (btnEdit.getText().equalsIgnoreCase("rediger")) {
            btnEdit.setText("Gem");
            setTextVisibility(true);
        } else {
            btnEdit.setText("Rediger");
            setTextVisibility(false);

            // Select the volunteer from the list and updates the new info.
            selectedVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
            selectedVolunteer.setFirstName(txtFirstName.getText());
            selectedVolunteer.setLastName(txtLastName.getText());
            selectedVolunteer.setEmail(txtEmail.getText());
            selectedVolunteer.setPhone(Integer.parseInt(txtPhone.getText()));
            selectedVolunteer.setDescription(txtVolunteerInfo.getText());
            selectedVolunteer.updateFullName();
            try {
                VolunteerModel.getInstance().updateVolunteer(selectedVolunteer);
            } catch (IOException | DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    @FXML
    private void handleNewVolunteer() throws IOException {
        primStage = (Stage) btnEdit.getScene().getWindow();
        Stage newVolunteerModal = modalFactory.createNewModal(primStage, EFXMLName.ADD_NEW_VOLUNTEER);
        newVolunteerModal.show();
    }

    @FXML
    private void handleSelectVolunteer() {
        selectedVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
        if (selectedVolunteer != null) {
            txtFirstName.setText(selectedVolunteer.getFirstName());
            txtLastName.setText(selectedVolunteer.getLastName());
            txtEmail.setText(selectedVolunteer.getEmail());
            txtPhone.setText("" + selectedVolunteer.getPhone());
            txtLinkMoreInfo.setVisible(true);
            txtVolunteerInfo.setText(selectedVolunteer.getDescription());

            selectVolunteerLanguage(selectedVolunteer);

            if (selectedVolunteer.getImage() != null) {
                imgProfile.setImage(selectedVolunteer.getImage());
            } else {
                Image img = new Image(this.getClass().getResourceAsStream(NO_PHOTO));
                imgProfile.setImage(img);
            }
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
        if (event.getClickCount() == 2) {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"));
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fc.showOpenDialog(primStage.getScene().getWindow());
            if (file != null) {
                try {
                    volunteerModel.setVolunteerImage(selectedVolunteer.getID(), file);
                } catch (DALException | FileNotFoundException ex) {
                    ExceptionDisplayer.display(ex);
                }
                Image img = new Image(file.toURI().toASCIIString());
                selectedVolunteer.setImage(img);
                imgProfile.setImage(img);
            }
        }
    }

}
