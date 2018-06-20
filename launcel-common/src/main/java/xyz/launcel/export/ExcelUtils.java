package xyz.launcel.export;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

public class ExcelUtils
{

    public static void exportExcel(HttpServletResponse response, String fileName, String[] titles, List<List<Object>> list)
    {
        if (titles.length > 0 && CollectionUtils.isNotEmpty(list))
        {
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((TimeFormatUtil.YYYY_MM_DD(new Date()) + "_" + fileName).getBytes(), Charset.forName("UTF-8")) + ".xlsx" + "\" ");
            try
            {
                ServletOutputStream out = response.getOutputStream();
                writeXSSFWorkbook(titles, list).write(out);
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "没有数据，无法导出");
        }

    }

    private static XSSFWorkbook writeXSSFWorkbook(String[] titles, List<List<Object>> list)
    {
        XSSFWorkbook  workbook = new XSSFWorkbook();
        XSSFSheet     sheet    = workbook.createSheet("sheet1");
        XSSFRow       row      = sheet.createRow(0);
        XSSFCellStyle style    = workbook.createCellStyle();
        XSSFFont      font     = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        font.setBold(true);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        style.setFont(font);
        XSSFCell cell;
        for (int i = 0; i < titles.length; i++)
        {
            cell = row.createCell((short) i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(style);
        }
        writeDataRow(list, sheet);
        return workbook;
    }

    private static void writeDataRow(List<List<Object>> list, XSSFSheet sheet)
    {
        int length = list.get(0).size();
        for (int i = 0; i < list.size(); i++)
        {
            if (i > 0 && list.get(i).size() != length)
            {
                ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "导出的数据中，存在数据列不一致");
            }
            XSSFRow row = sheet.createRow(i + 1);
            List<Object> clist = list.get(i);
            for (int n = 0; n < clist.size(); n++)
            {
                Object value = clist.get(n);
                if (value instanceof Date)
                {
                    row.createCell((short) n).setCellValue(TimeFormatUtil.YYYY_MM_DD((Date) value));
                }
                else
                {
                    row.createCell((short) n).setCellValue(clist.get(n).toString());
                }
            }
        }
    }
}
