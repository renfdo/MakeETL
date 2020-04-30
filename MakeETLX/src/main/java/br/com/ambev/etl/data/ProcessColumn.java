package br.com.ambev.etl.data;

import br.com.ambev.etl.gson.ColumnSchema;

public abstract class ProcessColumn {

	private ColumnSchema columnSchema;

	public ProcessColumn(ColumnSchema cs) {
		this.columnSchema = cs;
	}

	public ColumnSchema getColumnSchema() {
		return columnSchema;
	}

	public void setColumnSchema(ColumnSchema columnSchema) {
		this.columnSchema = columnSchema;
	}

	public abstract String valueOf(String value);

}
