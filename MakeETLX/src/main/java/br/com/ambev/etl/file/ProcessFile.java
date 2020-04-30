package br.com.ambev.etl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import br.com.ambev.etl.gson.JsonConfig;

public abstract class ProcessFile {

	protected final JsonConfig JSON_CONFIG;
	protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	protected static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private File file;
	private File csv;
	private String language =  System.getProperty("user.language");

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public File getFile() {
		return file;
	}

	public ProcessFile(JsonConfig jsonConfig) {
		this.JSON_CONFIG = jsonConfig;
	}

	public void doProcess(File file) {
		this.file = file;
		try {
			setCsv(toCSV());
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract File toCSV() throws FileNotFoundException, IOException, Exception;

	public File getCsv() {
		return csv;
	}

	public void setCsv(File csv) {
		this.csv = csv;
	}

	public JsonConfig getJSON_CONFIG() {
		return JSON_CONFIG;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
