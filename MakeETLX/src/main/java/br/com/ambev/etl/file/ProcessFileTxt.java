package br.com.ambev.etl.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;
import br.com.ambev.etl.gson.JsonConfig;

public class ProcessFileTxt extends ProcessFile {

	private final static Logger logger = Logger.getLogger(ProcessFileTxt.class);

	public ProcessFileTxt(JsonConfig jsonConfig) {
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
			boolean foundHeader = false;
			while ((line = br.readLine()) != null) {
				//logger.debug("line : " + line);
				List<String> csvdata = new java.util.LinkedList<String>();
				List<String> lineList = Arrays.asList(line.split(JSON_CONFIG.getDelimiter().toString()));
				if (getJSON_CONFIG().isRemoveFirstEmptyData()) {
					if (lineList.get(0).trim().isEmpty() && lineList.size() == JSON_CONFIG.getHeader().size() + 1) {
						lineList = lineList.subList(1, lineList.size());
					} else if (lineList.get(0).trim().equals("")
							&& lineList.size() == JSON_CONFIG.getHeader().size() + 1) {
						lineList = lineList.subList(1, lineList.size());
					}
				}
				if (lineList.size() == JSON_CONFIG.getHeader().size()) {
					lineList = lineList.stream().map(String::trim).collect(Collectors.toList());
					List<String> tmp = JSON_CONFIG.getHeader().stream().filter(s -> !s.isEmpty())
							.collect(Collectors.toList());
					if (lineList.containsAll(tmp)) {
//						csvdata.addAll(lineList);
//						csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
						foundHeader = true;
					} else if (foundHeader) {
						csvdata.addAll(lineList);
						csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
					}
				}
			}
			if (!foundHeader) {
				System.err.println("Header not found. Check JSON file.");
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		csvWriter.close();
		return csv;
	}

}
