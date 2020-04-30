package br.com.ambev.etl.data;

import br.com.ambev.etl.gson.ColumnSchema;

public class ProcessColumnString extends ProcessColumn {

	public ProcessColumnString(ColumnSchema cs) {
		super(cs);
	}

	@Override
	public String valueOf(String value) {
		if (value != null && value.length() > 199) {
			System.err.println("Valeu length warning : " + value);
		}
		for (String s : getColumnSchema().getRemoveChar()) {
			value = value.replaceAll("\\"+s, "");
		}
		return value!=null ? value.trim() : "";
	}

}
