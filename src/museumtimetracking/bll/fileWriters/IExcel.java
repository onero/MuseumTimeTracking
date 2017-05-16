/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll.fileWriters;

import java.io.IOException;
import java.util.List;
import jxl.write.WriteException;
import museumtimetracking.exception.DALException;

/**
 *
 * @author gta1
 */
public interface IExcel {

    /**
     * Export all lists varargs to excel sheet
     *
     * @throws IOException
     * @throws WriteException
     * @throws DALException
     */
    public <T> void exportToExcel(String location, List<T>... values) throws IOException, WriteException, DALException;

}
