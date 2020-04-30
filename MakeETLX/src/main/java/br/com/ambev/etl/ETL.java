package br.com.ambev.etl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import br.com.ambev.etl.data.ProcessData;
import br.com.ambev.etl.file.ProcessFile;
import br.com.ambev.etl.file.ProcessFileFactory;
import br.com.ambev.etl.gson.ColumnSchema;
import br.com.ambev.etl.gson.JsonConfig;

/**
 * Hello world!
 *
 */
public class ETL {

	private final JsonConfig JSON_CONFIG;
	private final static Logger logger = Logger.getLogger(ETL.class);

	public static String preLandingSourcePath;
	public static String preLandingDestinyPath;

	public ETL(String jsonFile) throws FileNotFoundException {
		logger.setLevel(Level.OFF);
		logger.debug("Reading Json File!");
		System.out.println(" Processing JSON  + " + jsonFile);
		FileInputStream in = new FileInputStream(jsonFile);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);
		this.JSON_CONFIG = new Gson().fromJson(reader, JsonConfig.class);
		logger.debug("Json File " + jsonFile + " readed!");
		// decimal format
		for (int i = 0; i < JSON_CONFIG.getColumnsSchema().size(); i++) {
			if (JSON_CONFIG.getColumnsSchema().get(i).getDataType() != null) {
				if (JSON_CONFIG.getColumnsSchema().get(i).getDataType().equals(ColumnSchema.DataType.Float)) {
					if (JSON_CONFIG.getColumnsSchema().get(i).getDecimalFormat() == null
							|| JSON_CONFIG.getColumnsSchema().get(i).getDecimalFormat().equals("")) {
						JSON_CONFIG.getColumnsSchema().get(i).setDecimalFormat(JSON_CONFIG.getDecimalFormat());
					}
				}
			}
		}
		System.out.println("Json File " + jsonFile + " readed!");
	}

	public List<List<String>> start() {
		List<List<String>> outPutTalend = new ArrayList<>();
		ProcessFile processFile = ProcessFileFactory.getInstance(JSON_CONFIG);
		List<File> files = JSON_CONFIG.getFileList();
		logger.info("Transorming file : " + files);
		for (File file : files) {
			try {
				logger.info("Transorming file : " + file.getName());
				processFile.doProcess(file);
				boolean sucess = etlCsv(processFile.getCsv(), file);
				ArrayList<String> value = new ArrayList<String>();
				value.add(sucess ? "OK" : "NOK");
				value.add(file.getAbsolutePath());
				outPutTalend.add(value);
				System.out.println("File saved on: " + file.getAbsolutePath());
			} catch (IOException e) {
				ArrayList<String> value = new ArrayList<String>();
				value.add("NOK");
				value.add(file.getAbsolutePath());
				outPutTalend.add(value);
				System.out.println("File saved on: " + file.getAbsolutePath());
				logger.error("Error on ETL final file.");
			}
		}
		return outPutTalend;
	}

	private boolean etlCsv(File fileOut, File originalFile) throws IOException {
		boolean OK = true;
		ProcessData ps = new ProcessData(JSON_CONFIG.getColumnsSchema());
		CSVReader reader = new CSVReader(new FileReader(fileOut), ',', '"', 0);
		File csv = new File(JSON_CONFIG.getPreLandingDestinyPath(),
				originalFile.getName().substring(0, originalFile.getName().lastIndexOf(".")) + ".csv");
		FileWriter fileWriter = new FileWriter(csv);
		CSVWriter csvWriter = new CSVWriter(fileWriter, '|', CSVWriter.NO_QUOTE_CHARACTER);
		// Start load
		String[] nextLine;
		List<String> header = JSON_CONFIG.getHeader();
		List<ColumnSchema> css = JSON_CONFIG.getColumnsSchema();
		List<String> csvdata = new java.util.LinkedList<String>();
		for (int i = 0; i < css.size(); i++) {
			csvdata.add(css.get(i).getName());
		}
		if (JSON_CONFIG.isAddDateColumn()) {
			csvdata.add("ETL_DATE");
		}
		csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
		while ((nextLine = reader.readNext()) != null) {
			csvdata = new java.util.LinkedList<String>();
			if (nextLine != null) {
				List<String> lineList = Arrays.asList(nextLine);
				if (lineList.size() == css.size()) {
					boolean isHeader = header.size() > 0 & lineList.containsAll(header);
					if (!isHeader) {
						try {
							for (int i = 0; i < lineList.size(); i++) {
								csvdata.add(ps.getProcessColumn(i).valueOf(lineList.get(i)));
							}
						}catch (Exception e){
							System.out.println("Erro in line : " + lineList);
							OK = false;
							break;
						}
						if (JSON_CONFIG.isAddDateColumn()) {
							csvdata.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						}
						csvWriter.writeNext(csvdata.toArray(new String[csvdata.size()]));
					}
				} else {
					System.out.println("Erro in line : " + lineList);
					OK = false;
					break;
				}
			}
		}
		csvWriter.close();
		reader.close();
		return OK;
	}

}
