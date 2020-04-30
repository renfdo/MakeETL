package br.com.ambev.etl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSVWriter;
import br.com.ambev.etl.gson.ColumnSchema;
import br.com.ambev.etl.gson.JsonConfig;

public class ProcessFileXLSX extends ProcessFile {

	private final static Logger logger = Logger.getLogger(ProcessFileXLSX.class);

	public ProcessFileXLSX(JsonConfig jsonConfig) {
		super(jsonConfig);
	}

	@SuppressWarnings("deprecation")
	protected File toCSV() throws InvalidFormatException, FileNotFoundException, IOException {
		logger.info("to csv file started!");
		FileInputStream input_document = new FileInputStream(getFile().getAbsoluteFile());

		// Read workbook into HSSFWorkbook
		XSSFWorkbook wb = new XSSFWorkbook(input_document);
		// Read worksheet into HSSFSheet
		XSSFSheet sheet = wb.getSheetAt(JSON_CONFIG.getSheetNumber());
		// To iterate over the rows
		Iterator<Row> rowIterator = sheet.iterator();
		// OpenCSV writer object to create CSV file
		File csv = new File(JSON_CONFIG.getTmpPath(),
				getFile().getName() + "_Sheet_" + JSON_CONFIG.getSheetNumber() + ".csv");
		setCsv(csv);
		FileWriter fileWriter = new FileWriter(csv);
		CSVWriter csvWriter = new CSVWriter(fileWriter);

		int lineNumber = 1;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if(lineNumber > JSON_CONFIG.getSkip_header()) {
				// change this depending on the length of your sheet
				List<String> csvdata = new java.util.LinkedList<String>();
				int colIntex = 0;
				HSSFDataFormatter formatter = new HSSFDataFormatter();

				Iterator<Cell> cellIterator = row.cellIterator();
				//			for (Integer index : indexHeader) {
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next(); // Fetch CELL
					if (cell != null) {
						if (cell.toString().contains("1686")) {
							System.out.print("");
						}

						ColumnSchema cs;
						if (colIntex > JSON_CONFIG.getColumnsSchema().size() - 1)
							break;
						cs = JSON_CONFIG.getColumnsSchema().get(colIntex);
						colIntex++;

						switch (cell.getCellType()) {
							case BOOLEAN:
								csvdata.add(String.valueOf(cell.getBooleanCellValue()));
								break;
							case STRING:
								if (cs.getDataType().equals(ColumnSchema.DataType.DateTime)) {
									cell.setCellType(CellType.NUMERIC);
									if (DateUtil.isCellDateFormatted(cell)) {
										csvdata.add(DATE_FORMAT.format(cell.getDateCellValue()));
									}
								} else {
									if ("pt".equals(getLanguage())) {
										String formatCellValue = formatter.formatCellValue(cell);
										csvdata.add(formatCellValue);
									} else {
										csvdata.add(cell.getStringCellValue());
									}
								}
								break;
							case NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									if (cs.getDateFrom() != null) {
										csvdata.add(new SimpleDateFormat(cs.getDateFrom()).format(cell.getDateCellValue()));
									} else {
										csvdata.add(DATETIME_FORMAT.format(cell.getDateCellValue()));
									}
								} else {
									if ("pt".equals(getLanguage())) {
										String formatCellValue = formatter.formatCellValue(cell);
										csvdata.add(formatCellValue);
									} else {
										csvdata.add(String.valueOf(cell.getNumericCellValue()));
									}
								}
								break;
							case FORMULA:
								String formulaType = cell.getCachedFormulaResultType().name();
								if (formulaType.equals("NUMERIC")) {
									final DataFormatter dataFormatter = new DataFormatter();
									final CellStyle cellStyle = cell.getCellStyle();
									final String formtatedValue = dataFormatter.formatRawCellContents(
											cell.getNumericCellValue(), cellStyle.getDataFormat(),
											cellStyle.getDataFormatString());
									csvdata.add(formtatedValue);
								} else if (formulaType.equals("STRING")) {
									csvdata.add(cell.getStringCellValue());
								} else if (formulaType.equals("ERROR")) {
									csvdata.add("#ERROR_FORMULA");
								}
								break;
							case BLANK:
								cell.setCellType(CellType.STRING);
								csvdata.add("");
								break;
							default:
								break;
						}
					} else {
						csvdata.add("");
					}
				}
				//			logger.debug(" Line with  " + csvdata.size() + " records.");
				if (!"".equals(String.join("", csvdata))) {
					csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
				} else {
					//				logger.debug(" Line with empyt.");
				}
			}
			lineNumber++;
		}
		wb.close();
		csvWriter.close();
		input_document.close();
		return csv;
	}

}
