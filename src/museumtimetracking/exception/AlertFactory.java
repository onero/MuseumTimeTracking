/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import museumtimetracking.MuseumTimeTracking;

public class AlertFactory {

    private final String DELETE_WARNING = MuseumTimeTracking.bundle.getString("DeleteWarning");
    private final String VALIDATION_WARNING = MuseumTimeTracking.bundle.getString("ValidationWarning");
    private final String ALERT_TITLE = "OBS!";
    private final String LOGOUT_WARNING = MuseumTimeTracking.bundle.getString("LogoutWarning");

    /**
     * Display alert with just OK button
     *
     * @param type
     * @param alertText
     * @return
     */
    public Alert createAlertWithoutCancel(AlertType type, String alertText) {
        Alert alert = new Alert(type);
        alert.setTitle(ALERT_TITLE);
        alert.setHeaderText(alertText);
        createSingleButton(alert);
        return alert;
    }

    /**
     * Create a single button
     *
     * @param alert
     */
    private void createSingleButton(Alert alert) {
        ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(yesButton);
    }

    /**
     * Create ordinary alert
     *
     * @param type
     * @param message
     * @return
     */
    public Alert createAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(ALERT_TITLE);
        alert.setHeaderText(message);
        createTwoButtons(alert);
        return alert;
    }

    /**
     * Create two buttons
     *
     * @param alert
     */
    public void createTwoButtons(Alert alert) {
        ButtonType yesButton = new ButtonType(MuseumTimeTracking.bundle.getString("Yes"), ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType(MuseumTimeTracking.bundle.getString("No"), ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
    }

    /**
     * Create an alert for deleting
     *
     * @return
     */
    public Alert createDeleteAlert() {
        String message = DELETE_WARNING;
        Alert alert = createAlert(Alert.AlertType.WARNING, message);
        createTwoButtons(alert);
        return alert;
    }

    /**
     * Create an logout alert for admin.
     *
     * @return
     */
    public Alert createLogoutAlert() {
        String message = LOGOUT_WARNING;
        Alert alert = createAlert(Alert.AlertType.WARNING, message);
        createTwoButtons(alert);
        return alert;
    }

    /**
     * Create alert for exceptions
     *
     * @param message
     * @return
     */
    public Alert createExceptionAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(ALERT_TITLE);
        alert.setHeaderText(message);
        alert.getButtonTypes().clear();
        return alert;
    }

    /**
     * Create alert for validation errors
     *
     * @return
     */
    public Alert createValidationAlert() {
        String text = VALIDATION_WARNING;
        Alert alert = createAlertWithoutCancel(AlertType.INFORMATION, text);
        return alert;
    }

}
