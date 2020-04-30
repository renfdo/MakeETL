package br.com.ambev.etl.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;
import br.com.ambev.etl.gson.JsonConfig;

public class ProcessFileCsv extends ProcessFile {

	private final static Logger logger = Logger.getLogger(ProcessFileCsv.class);

	public ProcessFileCsv(JsonConfig jsonConfig) {
		super(jsonConfig);
	}

	@Override
	protected File toCSV() throws FileNotFoundException, IOException, Exception {
		File csv = new File(JSON_CONFIG.getTmpPath(),
				getFile().getName() + "_Sheet_" + JSON_CONFIG.getSheetNumber() + ".csv");
		setCsv(csv);
		FileWriter fileWriter = new FileWriter(csv);
		CSVWriter csvWriter = new CSVWriter(fileWriter);

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(getFile().getAbsoluteFile()), JSON_CONFIG.getEncodingDetected()));
			String line;
//			boolean foundHeader = false;
//			logger.debug("Header : " + getJSON_CONFIG().getHeader());
//			List<String> header = new LinkedList<String>(JSON_CONFIG.getHeader());
//			header.removeIf(n -> n.equals(""));
//			while (!foundHeader && (line = br.readLine()) != null) {
//				List<String> lineList = new LinkedList<String>(Arrays.asList(line.split(JSON_CONFIG.getDelimiter().toString())));
//				lineList.removeIf(n -> n.equals(""));
//				if (lineList.containsAll(header)) {
//					foundHeader = true;
//				}
//			}
//			if (!foundHeader) {
//				System.err.println("Header not founded! Verify the headers accentuation titles JSON. Json size: "
//						+ JSON_CONFIG.getHeader().size() + ", Columns size :" + JSON_CONFIG.getColumnsSchema().size());
//			}/
            int lineNumber = 1;
			while ((line = br.readLine()) != null) {
//                logger.debug("line  : " + line);
				List<String> csvdata = new java.util.LinkedList<String>();
				List<String> lineList = Arrays.asList(line.split(JSON_CONFIG.getDelimiter().toString()));

				if (getJSON_CONFIG().isRemoveFirstEmptyData()) {
					lineList = lineList.subList(1, lineList.size());
				}

				int totalCols = JSON_CONFIG.totalCols();

				if (lineList.size() == totalCols) {
					lineList = lineList.stream().map(String::trim).collect(Collectors.toList());
//					List<String> tmp = JSON_CONFIG.getHeader().stream().filter(s -> !s.isEmpty())
//							.collect(Collectors.toList());
//					if (lineList.containsAll(tmp)) {
////						csvdata.addAll(lineList);
////						csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
//						foundHeader = true;
//					} else if (foundHeader) {
//						if (lineList.size() != JSON_CONFIG.getHeader().size()) {
//							System.err.println("Linha incompleta !!");
//						}
                        if(lineNumber > JSON_CONFIG.getSkip_header()) {
                            csvdata.addAll(lineList);
                            csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
                        }
//					}
				}
				lineNumber++;
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		csvWriter.close();
		return csv;
	}
}
