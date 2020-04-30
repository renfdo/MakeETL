package br.com.ambev.etl.data;

import br.com.ambev.etl.gson.ColumnSchema;

public class ProcessColumnFactory {

	public static ProcessColumn getInstance(ColumnSchema cs) {
		if (ColumnSchema.DataType.Float.equals(cs.getDataType())) {
			return new ProcessColumnFloat2(cs);
		}
		if (ColumnSchema.DataType.Date.equals(cs.getDataType())) {
			return new ProcessColumnDateTime(cs);
		}
		if (ColumnSchema.DataType.DateTime.equals(cs.getDataType())) {
			return new ProcessColumnDateTime(cs);
		}
		if (ColumnSchema.DataType.Integer.equals(cs.getDataType())) {
			return new ProcessColumnInteger(cs);
		}
		return new ProcessColumnString(cs);
	}

}
