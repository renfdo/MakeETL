package br.com.ambev.etl.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import br.com.ambev.etl.gson.ColumnSchema;
import br.com.ambev.etl.gson.ColumnSchema.DecimalFormat;

public class ProcessColumnFloat2 extends ProcessColumn {

	private final static Logger logger = Logger.getLogger(ProcessColumnFloat2.class);
	private NumberFormat numberFormat;

	public ProcessColumnFloat2(ColumnSchema cs) {
		super(cs);
	}

	@Override
	public String valueOf(String value) {
		if (value.trim().equals("-")) {
			value = "0";
		}
		if (value.trim().equals("ERROR")) {
			value = "0";
		}
		for (String s : getColumnSchema().getRemoveChar()) {
			value = value.replaceAll("\\" + s, "");
		}
		try {
			if (DecimalFormat.pt_BR.equals(getColumnSchema().getDecimalFormat())) {
				if (!value.isEmpty()) {
					Locale ptBR = new Locale("pt", "BR");
					numberFormat = NumberFormat.getNumberInstance(ptBR);
					Number parse = numberFormat.parse(value.trim());
					return parse.toString();
				} else {
					return new BigDecimal("0.00").toString();
				}
			} else {
				if (value != null && !"".equalsIgnoreCase(value)) {
					BigDecimal bd = new BigDecimal(value.trim());
					if (getColumnSchema().getScale() != null) {
						bd = bd.setScale(getColumnSchema().getScale(), RoundingMode.HALF_UP);
					}
					return bd.toPlainString();
				}
			}
		} catch (Exception e) {
//			System.err.println("Erro transforming Float. value = " + value + " ColumnSchema : " + getColumnSchema());
//			logger.debug("Erro transforming Float. value = " + value + " ColumnSchema : " + getColumnSchema());
		}
		return value;
	}

}
