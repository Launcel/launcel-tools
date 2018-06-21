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
import xyz.launcel.lang.TimeFormatUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExcelUtils
{

    public static void exportExcel(HttpServletResponse response, String fileName, String[] titles, List<List<Object>> list)
    {
        if (titles.length > 0 && CollectionUtils.isNotEmpty(list))
        {
            response.setContentType("application/ms-excel;charset=UTF-8");
            String fileNameTmp = TimeFormatUtil.YYYY_MM_DD(new Date()) + "_" + fileName + ".xlsx";
            fileName = new String(fileNameTmp.getBytes(), Charset.forName("UTF-8"));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Cache-Control", "no-cache");
            try
            {
                ServletOutputStream out = response.getOutputStream();
                writeXSSFWorkbook(titles, list).write(out);
                out.flush();
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
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont     font     = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        font.setBold(true);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        style.setFont(font);

        XSSFSheet sheet = workbook.createSheet("sheet1");
        XSSFRow   row   = sheet.createRow(0);
        XSSFCell  cell;
        for (int i = 0; i < titles.length; i++)
        {
            cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(style);
        }
        writeDataRow(list, sheet);
        return workbook;
    }

    private static void writeDataRow(List<List<Object>> list, XSSFSheet sheet)
    {
        for (int i = 0; i < list.size(); i++)
        {
            XSSFRow      row   = sheet.createRow(i + 1);
            List<Object> clist = list.get(i);
            for (int n = 0; n < clist.size(); n++)
            {
                Object value = clist.get(n);
                if (Objects.isNull(value))
                {
                    row.createCell((short) n).setCellValue("");
                }
                else
                {
                    if (value instanceof Date)
                    {
                        row.createCell((short) n).setCellValue(TimeFormatUtil.YYYY_MM_DD((Date) value));
                    }
                    else
                    {
                        row.createCell((short) n).setCellValue(String.valueOf(value));
                    }
                }
            }
        }
    }
}