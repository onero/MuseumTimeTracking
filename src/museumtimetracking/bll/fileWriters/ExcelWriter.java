/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll.fileWriters;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import museumtimetracking.exception.DALException;

public class ExcelWriter {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String fileLocation;

    private WritableWorkbook workbook;
    private WritableSheet excelSheet;

    /**
     * Set name of file
     *
     * @param inputFile
     */
    public void setOutputFile(String inputFile) {
        this.fileLocation = inputFile;
    }

    /**
     * Write sheet as file
     *
     * @throws IOException
     * @throws WriteException
     * @throws RowsExceededException
     * @throws DALException
     */
    public void createNewExcel(String nameOfSheet) throws IOException {
        File file = new File(fileLocation);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet(nameOfSheet, 0);
        excelSheet = workbook.getSheet(0);
    }

    /**
     * Writes the excel to a file on the computer
     *
     * @throws IOException
     * @throws WriteException
     */
    public void writeExcelToFile() throws IOException, WriteException {
        workbook.write();
        workbook.close();
    }

    /**
     * Create headline labels (captions)
     *
     * @param sheet
     * @throws WriteException
     */
    public void createCaptions(String... captions) throws WriteException {
        setupFontAndLayout();

        //Write headers
        for (int i = 0; i < captions.length; i++) {
            addCaption(excelSheet, i, 0, captions[i]);
        }

    }

    /**
     * Setup default fonts and layout
     *
     * @throws WriteException
     */
    private void setupFontAndLayout() throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);
    }

    /**
     * Write content to sheet
     *
     * @param sheet
     * @throws WriteException
     * @throws RowsExceededException
     * @throws IOException
     * @throws DALException
     */
    public void createLabelNumberContent(List<String> keys, List<Integer> values) throws WriteException, RowsExceededException, IOException, DALException {

        //Insert all the keys entries
        for (int i = 0; i < keys.size(); i++) {
            addLabel(excelSheet, 0, i + 1, keys.get(i));
        }

        //Insert all the values for the keys
        for (int i = 0; i < values.size(); i++) {
            addNumber(excelSheet, 1, i + 1, values.get(i));
        }
        // Lets calculate the sum of it
//        StringBuffer buf = new StringBuffer();
//        buf.append("SUM(A2:A10)");
//        Formula f = new Formula(0, 10, buf.toString());
//        sheet.addCell(f);
//        buf = new StringBuffer();
//        buf.append("SUM(B2:B10)");
//        f = new Formula(1, 10, buf.toString());
//        sheet.addCell(f);
    }

    /**
     * Write content to sheet
     *
     * @param sheet
     * @throws WriteException
     * @throws RowsExceededException
     * @throws IOException
     * @throws DALException
     */
    public void createLabelContent(List<String> keys, List<String> values) throws WriteException, RowsExceededException, IOException, DALException {

        //Insert all the keys entries
        for (int i = 0; i < keys.size(); i++) {
            addLabel(excelSheet, 0, i + 1, keys.get(i));
        }

        //Insert all the values for the keys
        for (int i = 0; i < values.size(); i++) {
            addLabel(excelSheet, 1, i + 1, values.get(i));
        }
    }

    /**
     * Add caption over a row (Headline)
     *
     * @param sheet
     * @param column
     * @param row
     * @param s
     * @throws RowsExceededException
     * @throws WriteException
     */
    private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    /**
     * Write integer to sheet
     *
     * @param sheet
     * @param column
     * @param row
     * @param integer
     * @throws WriteException
     * @throws RowsExceededException
     */
    private void addNumber(WritableSheet sheet, int column, int row, Integer integer) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    /**
     * Add label to sheet
     *
     * @param sheet
     * @param column
     * @param row
     * @param s
     * @throws WriteException
     * @throws RowsExceededException
     */
    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }
}
