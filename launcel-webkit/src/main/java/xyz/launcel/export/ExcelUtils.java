//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package xyz.launcel.export;

import lombok.var;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.launcel.export.api.XSSFWorkEntity;
import xyz.launcel.utils.TimeFormatUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExcelUtils
{
    public ExcelUtils() { }

    public static ServletOutputStream getOutputStream(HttpServletResponse response, String fileName)
    {
        response.setContentType("application/ms-excel;charset=UTF-8");
        String fileNameTmp = TimeFormatUtil.format(new Date(), "yyyy-MM-dd") + "_" + fileName + ".xlsx";
        try
        {
            fileName = new String(fileNameTmp.getBytes("ISO8859_1"), StandardCharsets.UTF_8);

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Cache-Control", "no-cache");
            return response.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //    private static void exportExcel(HttpServletResponse response, String fileName, String[] titles, List<List<Object>> list)
    //    {
    //        if (titles.length > 0 && CollectionUtils.isNotEmpty(list))
    //        {
    //            try
    //            {
    //                var out = getOutputStream(response, fileName);
    //                if (Objects.isNull(out))
    //                {
    //                    ExceptionFactory.create("_DEFINE_ERROR_CODE_011", "创建excel失败");
    //                }
    //                writeXSSFWorkbook(titles, list).write(out);
    //                out.flush();
    //                out.close();
    //            }
    //            catch (IOException var6)
    //            {
    //                var6.printStackTrace();
    //            }
    //        }
    //        else
    //        {
    //            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "没有数据，无法导出");
    //        }
    //    }
    //
    //    private static XSSFWorkbook writeXSSFWorkbook(String[] titles, List<List<Object>> list)
    //    {
    //        var workbookEntity = createXSS(titles);
    //        writeData(list, workbookEntity.getSheets1());
    //        return workbookEntity.getXlsx();
    //    }

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
