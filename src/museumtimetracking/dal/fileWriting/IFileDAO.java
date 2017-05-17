/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal.fileWriting;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author gta1
 */
public interface IFileDAO<T> {

    /**
     * Save the model to a file
     *
     * @param model
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveModelToFile(T model) throws FileNotFoundException, IOException;

    /**
     * Load the model from a file
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public T loadModel() throws FileNotFoundException, IOException;

}
