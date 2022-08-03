package com.ay.common.excel.poi.util;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * excel 操作
 * 
 * @author jackson
 *
 */
public class POIUtil {

	/** 设置excel头 */
	public static void setExcelTitle(HSSFWorkbook wb, HSSFSheet sheet, List<String> titleList) {
		if (wb == null) {
			wb = new HSSFWorkbook();
		}
		// 居中格式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		if (sheet == null) {
			sheet = wb.createSheet();
		}
		// 第0行,, 表头
		HSSFRow row = sheet.createRow(0);
		if (titleList == null || titleList.size() <= 0) {
			throw new RuntimeException("表头数据为空!");
		}
		for (int i = 0; i < titleList.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(titleList.get(i));
			cell.setCellStyle(style);
		}
	}

	/** 设置数据 */
	public static void setExcelData(HSSFWorkbook wb, HSSFSheet sheet, List<List<Object>> dataList) {
		// 居中格式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		for (int i = 0; i < dataList.size(); i++) {
			List<Object> list = dataList.get(i);
			HSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < list.size(); j++) {
				Object val = list.get(j);
				if (val instanceof Date) {
					HSSFCell cell = row.createCell(j);
					cell.setCellValue((Date) val);
					cell.setCellStyle(style);
					continue;
				}
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(val + "");
				cell.setCellStyle(style);
			}
		}
	}

	public static void main(String[] args) {
	}
}
