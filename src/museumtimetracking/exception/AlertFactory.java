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
        ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(yesButton);
        return alert;
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
        ButtonType yesButton = new ButtonType("JA", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("NEJ", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
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
        ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(yesButton);
        return alert;
    }

}
