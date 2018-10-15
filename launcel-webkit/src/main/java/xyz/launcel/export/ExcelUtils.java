//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package xyz.launcel.export;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.TimeFormatUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExcelUtils
{
    public ExcelUtils() { }

    public static void exportExcel(HttpServletResponse response, String fileName, String[] titles, List<List<Object>> list)
    {
        if (titles.length > 0 && CollectionUtils.isNotEmpty(list))
        {
            response.setContentType("application/ms-excel;charset=UTF-8");
            var fileNameTmp = TimeFormatUtil.format(new Date(), "yyyy-MM-dd") + "_" + fileName + ".xlsx";
            try
            {
                fileName = new String(fileNameTmp.getBytes("ISO8859_1"), Charset.forName("UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Cache-Control", "no-cache");

            try
            {
                var out = response.getOutputStream();
                writeXSSFWorkbook(titles, list).write(out);
                out.flush();
                out.close();
            }
            catch (IOException var6)
            {
                var6.printStackTrace();
            }
        }
        else
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "没有数据，无法导出");
        }
    }

    private static XSSFWorkbook writeXSSFWorkbook(String[] titles, List<List<Object>> list)
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
        var sheet = workbook.createSheet("sheet1");
        var row   = sheet.createRow(0);

        for (var i = 0; i < titles.length; ++i)
        {
            var cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(style);
        }

        writeDataRow(list, sheet);
        return workbook;
    }

    private static void writeDataRow(List<List<Object>> list, XSSFSheet sheet)
    {
        for (var i = 0; i < list.size(); ++i)
        {
            var row   = sheet.createRow(i + 1);
            var clist = list.get(i);

            for (var n = 0; n < clist.size(); ++n)
            {
                row.createCell((short) n).setCellValue("");
                var value = clist.get(n);
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
