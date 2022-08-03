package com.ay.common.excel.poi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {

	/**
	 * 创建excel文档， [@param] list 数据 <br>
	 * 分页导出excel
	 * 
	 * @param list
	 *            数据
	 * @param pageSize
	 *            每页显示的记录数
	 * @param sheetName
	 *            sheet页名称
	 * @param keys
	 *            list中map的key数组集合
	 * @param columnNames
	 *            excel的列名
	 */
	public static Workbook createWorkBook(List<Map<String, Object>> list, Integer pageSize, String sheetName, String[] keys, String columnNames[]) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		int pageNo = list.size() / pageSize + (list.size() % pageSize == 0 ? 0 : 1);
		String backSheet = null;
		List<Sheet> sheetList = new ArrayList<>();
		if (pageNo == 0) {
			backSheet = sheetName;
			if (backSheet.contains("{start}")) {
				backSheet = backSheet.replace("{start}", "");
			}
			if (backSheet.contains("{end}")) {
				backSheet = backSheet.replace("{end}", "");
			}
			if (backSheet.contains("to")) {
				backSheet = backSheet.replace("to", "");
			}
			sheetList.add(wb.createSheet(backSheet));
		}
		for (int i = 0; i < pageNo; i++) {
			backSheet = sheetName;
			if (backSheet.contains("{start}")) {
				backSheet = backSheet.replace("{start}", (i * pageSize + 1) + "");
			}
			if (backSheet.contains("{end}")) {
				backSheet = backSheet.replace("{end}", (i + 1) * pageSize + "");
			}
			sheetList.add(wb.createSheet(backSheet));
		}

		// Sheet sheet =
		// wb.createSheet(list.get(0).get("sheetName").toString());
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < keys.length; i++) {
			for (int j = 0; j < sheetList.size(); j++) {
				sheetList.get(j).setColumnWidth((short) i, (short) (35.7 * 150));
			}
		}

		// 创建第一行
		List<Row> rowList = new ArrayList<>();
		for (int i = 0; i < sheetList.size(); i++) {
			rowList.add(sheetList.get(i).createRow(0));
		}
		// Row row = sheet.createRow((short) 0);

		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();

		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());

		// Font f3=wb.createFont();
		// f3.setFontHeightInPoints((short) 10);
		// f3.setColor(IndexedColors.RED.getIndex());

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);

		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		// 设置列名
		for (int i = 0; i < columnNames.length; i++) {
			for (int j = 0; j < rowList.size(); j++) {
				Cell cell = rowList.get(j).createCell(i);
				cell.setCellValue(columnNames[i]);
				cell.setCellStyle(cs);
			}
		}
		// 设置每行每列的值
		int index = 1;
		for (int i = 0; i < list.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Sheet sheet = sheetList.get(i / pageSize);
			if (i % pageSize == 0) {
				index = 1;
			} else {
				index++;
			}
			Row row1 = sheet.createRow(index);
			// 在row行上创建一个方格
			for (int j = 0; j < keys.length; j++) {
				Cell cell = row1.createCell(j);
				cell.setCellValue("null".equals((list.get(i).get(keys[j]) + "")) ? " " : list.get(i).get(keys[j]).toString());
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}

	/**
	 * 创建excel文档， 导出表头 <br>
	 * 
	 * @param sheetName
	 *            sheet页名称
	 * @param keys
	 *            list中map的key数组集合
	 * @param columnNames
	 *            excel的列名
	 */
	public static Workbook createWorkBook(String sheetName, String[] keys, String columnNames[]) {
		return createWorkBook(new ArrayList<>(), 10, sheetName, keys, columnNames);
	}
	
}
