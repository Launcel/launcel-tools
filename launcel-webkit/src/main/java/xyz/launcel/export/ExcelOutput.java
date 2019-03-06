//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package xyz.launcel.export;

import xyz.launcel.exception.ProfessionException;
import xyz.launcel.utils.TimeFormatUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ExcelOutput
{
    public ExcelOutput() { }

    public static ServletOutputStream getOutput(HttpServletResponse response, String fileName)
    {
        response.setContentType("application/ms-excel;charset=UTF-8");
        var fileNameTmp = TimeFormatUtil.format(new Date(), "yyyy-MM-dd") + "_" + fileName + ".xlsx";
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
            throw new ProfessionException("00520");
        }
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
}
