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

public class AlertFactory {

    public static final String DELETE_WARNING = "Tryk 'Ja' for at slette permanent. \n Tryk 'Nej' for at fortryde.";
    public static final String VALIDATION_WARNING = "Skal udfyldes:\nFornavn.\nEfternavn\n\n"
            + "Tjekt eventuelt at:\n"
            + "Telefon nummeret kun indeholder tal.";
    public static final String ALERT_TITLE = "OBS!";

    /**
     * Display alert with just OK button
     *
     * @param type
     * @param alertText
     * @return
     */
    public static Alert createAlertWithoutCancel(AlertType type, String alertText) {
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
    private static void createSingleButton(Alert alert) {
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
    public static Alert createAlert(AlertType type, String message) {
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
    public static void createTwoButtons(Alert alert) {
        ButtonType yesButton = new ButtonType("JA", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("NEJ", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
    }

    /**
     * Create an alert for deleting
     *
     * @return
     */
    public static Alert createDeleteAlert() {
        String message = DELETE_WARNING;
        Alert alert = AlertFactory.createAlert(Alert.AlertType.WARNING, message);
        createTwoButtons(alert);
        return alert;
    }

    /**
     * Create alert for exceptions
     *
     * @param message
     * @return
     */
    public static Alert createExceptionAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(ALERT_TITLE);
        alert.setHeaderText(message);
        createSingleButton(alert);
        return alert;
    }

    /**
     * Create alert for validation errors
     *
     * @return
     */
    public static Alert createValidationAlert() {
        String text = VALIDATION_WARNING;
        Alert alert = AlertFactory.createAlertWithoutCancel(AlertType.INFORMATION, text);
        return alert;
    }

}
