package xyz.x.common.utils.excelapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class XSSFWorkEntity
{
    private XSSFWorkbook xlsx;

    private List<XSSFSheet> sheets;

    public XSSFSheet getSheet(int i)
    {
        return sheets.get(i);
    }
}
