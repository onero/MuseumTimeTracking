/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.exception;

import javafx.scene.control.Alert;

public class ExceptionDisplayer {

    /**
     * Display an error, by showing an Alert Pop-up
     *
     * @param ex as Throwable exception
     */
    public static void display(Throwable ex) {
        ex.printStackTrace();
        Alert alert = new AlertFactory().createExceptionAlert(ex.getMessage());
        alert.show();
    }

}
