/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.exception;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import museumtimetracking.gui.model.GuildModel;

public class ExceptionDisplayer {

    public static void display(Throwable ex) {
        ex.printStackTrace();
        Alert alert = AlertFactory.createExceptionAlert(ex.getMessage());
        alert.show();
        try {
            Thread.sleep(5000);
            alert.resultProperty().setValue(ButtonType.OK);
        } catch (InterruptedException ex1) {
            Logger.getLogger(GuildModel.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

}
