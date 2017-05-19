/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

/**
 *
 * @author gta1
 */
public interface ISaveModel<T> {

    /**
     * Save the model
     *
     * @param model
     */
    public void saveModel(T model);

}
