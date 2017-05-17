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
import museumtimetracking.gui.model.GuildModel;

public class GuildFileDAO implements IFileDAO<GuildModel> {

    @Override
    public GuildModel loadModel() {
        GuildModel model = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("guildModel.ser"))) {
            model = (GuildModel) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionDisplayer.display(ex);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.getStackTrace();
        }
        return model;
    }

    @Override
    public void saveModelToFile(GuildModel model) {
        try (FileOutputStream fileOut = new FileOutputStream("guildModel.ser"); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(model);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GuildFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GuildFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
