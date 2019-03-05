package xyz.launcel.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.launcel.utils.excelapi.XSSFWorkEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtils
{
    public static XSSFWorkEntity createXSS(String[] titles)
    {
        return createXSS(titles, 1);
    }

    private static XSSFWorkEntity createXSS(String[] titles, int sheetSize)
    {
        var workbook = new XSSFWorkbook();
        var font     = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        font.setBold(true);
        var style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        style.setFont(font);

        var sheets = new ArrayList<XSSFSheet>(sheetSize);
        for (int j = 1; j <= sheetSize; j++)
        {
            var sheet = workbook.createSheet("sheet" + j);
            sheets.add(sheet);
            var row = sheet.createRow(0);

            for (int i = 0; i < titles.length; ++i)
            {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(style);
            }
        }
        return new XSSFWorkEntity(workbook, sheets);
    }

    public static void writeData(List<List<Object>> list, XSSFSheet sheet)
    {
        writeData(list, sheet, 0);
    }

    private static void writeData(List<List<Object>> list, XSSFSheet sheet, int indexRow)
    {
        for (int i = 0; i < list.size(); ++i)
        {
            var row   = sheet.createRow(indexRow + i);
            var clist = list.get(i);

            for (int n = 0; n < clist.size(); ++n)
            {
                row.createCell((short) n).setCellValue("");
                Object value = clist.get(n);
                if (Objects.nonNull(value))
                {
                    if (value instanceof Date)
                    {
                        row.createCell((short) n).setCellValue(TimeFormatUtil.format((Date) value, "yyyy-MM-dd"));
                        continue;
                    }
                    row.createCell((short) n).setCellValue(String.valueOf(value));
                }
            }
        }
    }
}
