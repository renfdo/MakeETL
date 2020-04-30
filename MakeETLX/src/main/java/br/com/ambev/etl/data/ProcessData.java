package br.com.ambev.etl.data;

import java.util.LinkedList;
import java.util.List;

import br.com.ambev.etl.gson.ColumnSchema;

public class ProcessData {

	private List<ProcessColumn> processColumnList;
	
	public ProcessData(List<ColumnSchema> columnsSchema) {
		for (ColumnSchema cs : columnsSchema) {
			this.getProcessColumnList().add(ProcessColumnFactory.getInstance(cs));
		}
	}

	public List<ProcessColumn> getProcessColumnList() {
		if (processColumnList == null) {
			processColumnList = new LinkedList<ProcessColumn>();
		}
		return processColumnList;
	}

	public void setProcessColumnList(List<ProcessColumn> processColumnList) {
		this.processColumnList = processColumnList;
	}

	public ProcessColumn getProcessColumn(int i) {
		return getProcessColumnList().get(i);
	}

}
