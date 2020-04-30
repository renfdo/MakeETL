package br.com.ambev.etl.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import br.com.ambev.etl.gson.ColumnSchema;

public class ProcessColumnDateTime extends ProcessColumn {

	private final static Logger logger = Logger.getLogger(ProcessColumnDateTime.class);

	public ProcessColumnDateTime(ColumnSchema cs) {
		super(cs);
	}

	@Override
	public String valueOf(String value) {
		if (!value.isEmpty()) {
			if (getColumnSchema().getDateFrom().isEmpty()) {
				return value;
			}
			if (getColumnSchema().getDateTo().isEmpty()) {
				return value;
			}
			if (getColumnSchema().getDateFrom().equals(getColumnSchema().getDateTo())) {
				return value;
			}
			SimpleDateFormat SDF_FROM = new SimpleDateFormat(getColumnSchema().getDateFrom());
			try {
				Date date = SDF_FROM.parse(value);
				SimpleDateFormat SDF_TO = new SimpleDateFormat(getColumnSchema().getDateTo());
				return SDF_TO.format(date);
			} catch (ParseException e) {
//				System.err.println("Error converting datetime : value = " + value + " ColumnSchema = " + getColumnSchema().getName());
//				logger.error("Error converting datetime : value = " + value + " ColumnSchema = " + getColumnSchema().getName());
			}
		}
		return value;
	}

}