package br.com.ambev.etl.gson;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ColumnSchema implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum DataType {
		Float, String, DateTime, Date, Integer
	}

	public enum DecimalFormat {
		US, pt_BR
	}

	private String name;
	private DataType dataType;
	private String nameFile;
	private String[] removeChar;
	private DecimalFormat decimalFormat = null;
	private String dateOrigin;
	private String dateFrom;
	private String dateTo;
	private boolean allowNull = true;
	private List<String> replace;
	private boolean formatCell;
	private Integer scale;

	public boolean allowNull() {
		return allowNull;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	

	@Override
	public String toString() {
		return "ColumnSchema [name=" + name + ",\n               dataType=" + dataType + ",\n               nameFile=" + nameFile + ",\n               removeChar="
				+ Arrays.toString(removeChar) + ",\n               decimalFormat=" + decimalFormat + ",\n               dateOrigin=" + dateOrigin
				+ ",\n               dateFrom=" + dateFrom + ",\n               dateTo=" + dateTo + ",\n               allowNull=" + allowNull + ",\n               replace=" + replace
				+ "]";
	}

	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public String getDateOrigin() {
		return dateOrigin;
	}

	public void setDateOrigin(String dateOrigin) {
		this.dateOrigin = dateOrigin;
	}

	public List<String> getReplace() {
		return replace;
	}

	public void setReplace(List<String> replace) {
		this.replace = replace;
	}

	public String[] getRemoveChar() {
		if (removeChar == null) {
			removeChar = new String[0];
		}
		return removeChar;
	}

	public void setRemoveChar(String[] removeChar) {
		this.removeChar = removeChar;
	}

	public Integer getScale() {
		return this.scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

}
