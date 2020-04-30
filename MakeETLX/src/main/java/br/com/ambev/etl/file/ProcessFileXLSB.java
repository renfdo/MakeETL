package br.com.ambev.etl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.binary.XSSFBSharedStringsTable;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;

import au.com.bytecode.opencsv.CSVWriter;
import br.com.ambev.etl.gson.JsonConfig;

public class ProcessFileXLSB extends ProcessFile {

	private final static Logger logger = Logger.getLogger(ProcessFileXLSB.class);

	public ProcessFileXLSB(JsonConfig jsonConfig) {
		super(jsonConfig);
	}

	@SuppressWarnings("deprecation")
	protected File toCSV() throws FileNotFoundException, IOException, Exception {
		logger.info("to csv file started!");

		// OpenCSV writer object to create CSV file
		File csv = new File(JSON_CONFIG.getTmpPath(),
				getFile().getName() + "_Sheet_" + JSON_CONFIG.getSheetNumber() + ".csv");
		if (csv.exists()) {
			csv.delete();
			csv.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(csv);
		CSVWriter csvWriter = new CSVWriter(fileWriter);
		// Loop through rows.

		try (OPCPackage pkg = OPCPackage.open(getFile().getAbsoluteFile(), PackageAccess.READ)) {
			XSSFBReader wb = new XSSFBReader(pkg);
			XSSFBSharedStringsTable sst = new XSSFBSharedStringsTable(pkg);
			XSSFBReader.SheetIterator it = (XSSFBReader.SheetIterator) wb.getSheetsData();
			AmbevSheetHandler testSheetHandler = new AmbevSheetHandler();
			int sheetIndex = 0;
			while (it.hasNext()) {
				InputStream is = it.next();
				if (sheetIndex == JSON_CONFIG.getSheetNumber()) {

					XSSFBSheetHandler sheetHandler = new XSSFBSheetHandler(is, wb.getXSSFBStylesTable(),
							it.getXSSFBSheetComments(), sst, testSheetHandler, new DataFormatter(), false);
					sheetHandler.parse();
					break;
				}
				sheetIndex++;
			}

			int countInfo = 1;
			boolean findHeader = false;
			int lineNumber = 1;
			for (List<String> line : testSheetHandler.getSb()) {
				if(lineNumber > JSON_CONFIG.getSkip_header()) {
					List<String> csvdata = new java.util.LinkedList<String>();
					if (countInfo++ % 300 == 0) {
						//					logger.debug("Prossed " + countInfo + " from " + testSheetHandler.getSb().size() + " records");
					}
					int headerSize = JSON_CONFIG.getColumnsSchema().size();
					//					logger.debug("size : " + line.size() + " line : " + line);
					int lineSize = line.size();
					if (lineSize != headerSize) {
						if (line.size() > headerSize) {
							for (int i = lineSize - 1; i > headerSize - 1; i--) {
								line.remove(i);
							}
						} else {
							for (int i = lineSize; i < headerSize; i++) {
								line.add("");
							}
						}
					}
					csvdata.addAll(line);
					if (!csvdata.isEmpty()) {
						csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
					}
				}
				lineNumber++;
			}
			csvWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csv;
	}

}
