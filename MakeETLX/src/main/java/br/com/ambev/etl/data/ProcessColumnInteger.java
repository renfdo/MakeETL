package br.com.ambev.etl.data;

import br.com.ambev.etl.gson.ColumnSchema;

public class ProcessColumnInteger extends ProcessColumn {

	public ProcessColumnInteger(ColumnSchema cs) {
		super(cs);
	}

	@Override
	public String valueOf(String value) {
		try {
			for (String s : getColumnSchema().getRemoveChar()) {
				value = value.replaceAll("\\"+s, "");
			}
			return value != null && !value.trim().equals("") ? String.valueOf(Double.valueOf(value.trim()).intValue()) : "";
		} catch (Exception e) {
			System.err.println("warning : Value is not numeric, Check dataType. Column ? " + getColumnSchema().getName() + ", value : " + value);
			throw e;
		}
//		return value != null ? value.trim() : "";
	}
}
