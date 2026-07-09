package kr.co.ibk.common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelConstant {
    public static final String FILE_NAME = "fileName";
    public static final String HEAD = "head";
    public static final String BODY = "body";

    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String XLSX_STREAM = "xlsx-stream";

    public static void MakeCellStyle(SXSSFWorkbook workbook, XSSFCellStyle style, String type){
        Font font = workbook.createFont();

        if( "header".equals(type) ){
            font.setFontHeight((short)(9*20));
            font.setFontName("Arial");
            font.setBold(true);

            style.setFont(font);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setFillForegroundColor(new XSSFColor(new byte[] {(byte) 255,(byte) 255,(byte) 153}, null));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        }else{
            font.setFontHeight((short)(9*20));
            font.setFontName("Arial");

            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        }
    }
}
