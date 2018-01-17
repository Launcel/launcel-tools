package xyz.launcel.orm;

import java.util.Map;

/**
 * Created by xuyang in 2017/10/25
 */
@Deprecated
public class Table
{
    private String tableName;

    private Map<String, String> columnField;

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public Map<String, String> getColumnField()
    {
        return columnField;
    }

    public void setColumnField(Map<String, String> fieldColumn)
    {
        this.columnField = fieldColumn;
    }

    public void setColumnField(String fileName, String columnName)
    {
        columnField.put(fileName, columnName);
    }

}
