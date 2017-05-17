/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal.fileWriting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.VolunteerModel;

public class VolunteerFileDAO implements IFileDAO<VolunteerModel> {

    @Override
    public VolunteerModel loadModel() {
        VolunteerModel model = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("VolunteerModel.ser"))) {
            model = (VolunteerModel) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionDisplayer.display(ex);
        } catch (FileNotFoundException ex) {
            System.out.println("Test");
        } catch (IOException ex) {
            System.out.println("Test");
        }
        return model;
    }

    @Override
    public void saveModelToFile(VolunteerModel model) {
        try (FileOutputStream fileOut = new FileOutputStream("VolunteerModel.ser"); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(model);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VolunteerFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VolunteerFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
