package br.com.ambev.etl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import au.com.bytecode.opencsv.CSVWriter;
import br.com.ambev.etl.gson.JsonConfig;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ProcessFileXLS<E> extends ProcessFile {

	private final static Logger logger = Logger.getLogger(ProcessFileXLS.class);

	public ProcessFileXLS(JsonConfig jsonConfig) {
		super(jsonConfig);
	}

	protected File toCSV() throws InvalidFormatException, FileNotFoundException, IOException {
		logger.debug(" toCSV ");
		CSVWriter csvWriter = null;
		File csv = new File(JSON_CONFIG.getTmpPath(),
				getFile().getName() + "_Sheet_" + JSON_CONFIG.getSheetNumber() + ".csv");
		if (csv.exists()) {
			csv.delete();
			csv.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(csv);
		csvWriter = new CSVWriter(fileWriter);
		try {
			Workbook workbook = Workbook.getWorkbook(getFile());
			Sheet sheet = workbook.getSheet(0);
			System.out.println(sheet.getCell(0, 0).getContents() + " " + sheet.getCell(1, 0).getContents() + " "
					+ sheet.getCell(2, 0).getContents() + " " + sheet.getCell(3, 0).getContents() + " "
					+ sheet.getCell(4, 0).getContents());

			List<String> csvdata = new java.util.LinkedList<String>();
			int i = 0;
			int lineNumber = 1;



			for (; i < sheet.getRows(); i++) {
				if(lineNumber > JSON_CONFIG.getSkip_header()) {
					Cell[] cells = sheet.getRow(i);
					csvdata = new java.util.LinkedList<String>();
					for (int j = 0; j < cells.length; j++) {
						Cell cell = cells[j];
						//					logger.debug(" Cell Content " + cell.getContents());
						csvdata.add(cell.getContents());
					}
					csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
				}
				lineNumber++;
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvWriter.close();
			} catch (Exception e2) {
			}
		}
		return csv;
	}

}
