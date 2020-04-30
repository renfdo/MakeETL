package br.com.ambev.etl.gson;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.com.ambev.etl.ETL;
import br.com.ambev.etl.gson.ColumnSchema.DecimalFormat;

public final class JsonConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Delimiter {

		PIPE("|", '|'), TAB("\t", '\t'), SEMICOLON(";", ';');

		private Delimiter(String s, char c) {
			this.s = s;
			this.c = c;
		}

		private String s;
		private char c;

		public String toString() {
			return s;
		}

		public char toChar() {
			return c;
		}

	}

	private boolean removeFirstEmptyData = false;
	private Delimiter delimiter = null;
	private String enconding = null;
	private String namePattern = null;
	private String extension = null;
	private int sheetNumber = 0;
	private Integer skip_header = 0;
	private boolean addDateColumn = false;
	private List<String> header;
	private char decimalSeparator = '.';
	private List<ColumnSchema> columnsSchema = new ArrayList<ColumnSchema>();
	private List<File> fileList;
	private DateFormat dateFormat;
	private String encodingDetected;
	private String headerFileName;
	private List<byte[]> headerUTFBytes;
	private boolean lineIncompleteAccepted = false;
	private DecimalFormat decimalFormat;
	private int headerSize;

	public String getHeaderFileName() {
		return headerFileName;
	}

	public void setHeaderFileName(String headerFileName) {
		this.headerFileName = headerFileName;
	}

	public List<File> getFileList() {
		if (this.fileList == null) {
			this.fileList = new LinkedList<File>();
			File[] filesFolder = new File(ETL.preLandingSourcePath).listFiles();
			Pattern pt = Pattern
					.compile(namePattern.toUpperCase().replace("*", ".*") + "." + this.getExtension().toUpperCase());
			for (File file : filesFolder) {
				if (file.getName().toUpperCase().matches(pt.pattern()))
					this.fileList.add(file);
			}
		}
		return this.fileList;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	public boolean isAddDateColumn() {
		return addDateColumn;
	}

	public int getSheetNumber() {
		return sheetNumber;
	}

	public Delimiter getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(Delimiter delimiter) {
		this.delimiter = delimiter;
	}

	public float getSkip_header() {
		return skip_header;
	}

	public String getEnconding() {
		return enconding;
	}

	public String getExtension() {
		return extension;
	}

	public int totalCols() {
		return columnsSchema.size();
	}

	public String getEncodingDetected() {
		return encodingDetected;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public ColumnSchema getIndexColumn(int idx) {
		return columnsSchema.get(idx);
	}

	public String getHeader(String sep) {
		return columnsSchema.stream().map(ColumnSchema::getName).collect(Collectors.joining(sep));
	}

	public String getName() {
		return namePattern + "." + extension;
	}

	public String getFullPath(int idx) {
		return ETL.preLandingSourcePath + "/" + fileList.get(idx).getName();
	}

	public String getPreLandingSourcePath() {
		return ETL.preLandingSourcePath;
	}

	public String getPreLandingDestinyPath() {
		return ETL.preLandingDestinyPath;
	}

	public void setEncodingDetected(String encodingDetected) {
		this.encodingDetected = encodingDetected;
	}

	public List<ColumnSchema> getColumnsSchema() {
		return columnsSchema;
	}

	public List<String> getHeader() {
		if(header == null)
			return new ArrayList<String>();
		if (header.size() == 1 && header.get(0).contains("|")) {
			StringTokenizer stringTokenizer = new StringTokenizer(header.get(0), "|");
			header = new LinkedList<String>();
			for (int i = 0; stringTokenizer.hasMoreTokens(); i++) {
				String vaue = String.valueOf(stringTokenizer.nextElement());
				header.add(vaue);
			}
		}
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public String getTmpPath() {
		File tmp = new File(this.getPreLandingSourcePath(), "tmp");
		if (!tmp.exists()) {
			tmp.mkdir();
		}
		return tmp.getAbsolutePath();
	}

	public List<byte[]> getHeaderUTFBytes() {
		if (headerUTFBytes == null) {
			headerUTFBytes = new LinkedList<byte[]>();
			for (String h : getHeader()) {
				try {
					headerUTFBytes.add(h.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return headerUTFBytes;
	}

	public boolean isLineIncompleteAccepted() {
		return lineIncompleteAccepted;
	}

	public boolean isRemoveFirstEmptyData() {
		return removeFirstEmptyData;
	}

	public void setRemoveFirstEmptyData(boolean removeFirstEmptyData) {
		this.removeFirstEmptyData = removeFirstEmptyData;
	}

	public DecimalFormat getDecimalFormat() {
		return this.decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public int getHeaderSize() {
		return this.headerSize;
	}

	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

}