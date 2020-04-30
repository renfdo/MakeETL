package br.com.ambev.etl.file;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class AmbevSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
	private final List<List<String>> sb = new ArrayList<List<String>>();
	private List<String> col = new ArrayList<String>();

	private boolean firstCellOfRow;
	private int currentRow = -1;
	private int currentCol = -1;

	public List<List<String>> getSb() {
		return sb;
	}

	public void startSheet(String sheetName) {
		return;
	}

	public void endSheet() {
		return;
	}

	@Override
	public void startRow(int rowNum) {
		this.col = new ArrayList<String>();

		firstCellOfRow = true;
		currentRow = rowNum;
		currentCol = -1;
	}

	@Override
	public void endRow(int rowNum) {
		this.sb.add(this.col);
	}

	@Override
	public void cell(String cellReference, String formattedValue, XSSFComment comment) {
		// Did we miss any cells?
		int thisCol = (new CellReference(cellReference)).getCol();
		int missedCols = thisCol - currentCol - 1;
		// não entendi mas funciona
		for (int i = 0; i < missedCols; i++) {
			col.add("");
		}
		currentCol = thisCol;
		formattedValue = (formattedValue == null) ? "" : formattedValue;

		col.add(formattedValue);
	}

	@Override
	public void headerFooter(String text, boolean isHeader, String tagName) {
		return;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}