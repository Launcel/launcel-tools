package xyz.launcel.export.api;

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

    public XSSFSheet getSheets1()
    {
        return sheets.get(0);
    }

    public XSSFSheet getSheet(int i)
    {
        return sheets.get(i);
    }
}
