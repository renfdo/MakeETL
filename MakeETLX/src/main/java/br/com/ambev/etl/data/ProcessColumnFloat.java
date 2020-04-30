package br.com.ambev.etl.data;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import br.com.ambev.etl.gson.ColumnSchema;
import br.com.ambev.etl.gson.ColumnSchema.DecimalFormat;

public class ProcessColumnFloat extends ProcessColumn {

	private final static Logger logger = Logger.getLogger(ProcessColumnFloat.class);

	public ProcessColumnFloat(ColumnSchema cs) {
		super(cs);
	}

	@Override
	public String valueOf(String value) {
		if (value.trim().equals("-")) {
			value = "0";
		}
		try {
			StringBuffer sb = new StringBuffer();
			char[] array = value.toCharArray();
			for (char c : array) {
				if (Character.toString(c).matches("[0-9?]")) {
					sb.append(c);
				}
				if (c == ',') {
					sb.append(c);
				}
				if (c == '.') {
					sb.append(c);
				}
			}
			value = sb.toString();
			if (!DecimalFormat.US.equals(getColumnSchema().getDecimalFormat())) {
				if (!value.isEmpty()) {
					if (value.length() - value.indexOf(",") == 2) {
						value = value.replace(".", "");
						value = value.replace(",", ".");
					} else if (value.length() - value.indexOf(",") == 3) {
						value = value.replace(".", "");
						value = value.replace(",", "."); 
					} else if (value.indexOf(",") < 4) {
						value = value.replace(",", "");
					}
					String result = new BigDecimal(value.trim()).toString();
					return result;
				} else {
					return new BigDecimal("0.00").toString();
				}
			}
		} catch (Exception e) {
			logger.error("Erro transforming Float. value = " + value + " ColumnSchema : " + getColumnSchema(), e);
		}
		return value;
	}

}
