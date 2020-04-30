package br.com.ambev.etl.file;

import br.com.ambev.etl.gson.JsonConfig;

public class ProcessFileFactory {

	public static ProcessFile getInstance(JsonConfig jsonConfig) {
		if (jsonConfig.getExtension().equalsIgnoreCase("xls")) {
			return new ProcessFileXLS(jsonConfig);
		}
		if (jsonConfig.getExtension().equalsIgnoreCase("xlsx")) {
			return new ProcessFileXLSX(jsonConfig);
		}
		if (jsonConfig.getExtension().equalsIgnoreCase("xlsb")) {
			return new ProcessFileXLSB(jsonConfig);
		}
		if (jsonConfig.getExtension().equalsIgnoreCase("txt")) {
			return new ProcessFileCsv(jsonConfig);
		}
		if (jsonConfig.getExtension().equalsIgnoreCase("csv")) {
			return new ProcessFileCsv(jsonConfig);
		}
		return null;
	}

}
