package xyz.launcel.orm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyang in 2017/10/25
 */
@Deprecated
public class TableMapping
{

    private final Map<Class<?>, Table> modelTableMap = new HashMap<Class<?>, Table>();

    private TableMapping()
    {
    }

    private static TableMapping me = new TableMapping();

    public static TableMapping one()
    {
        return me;
    }

    public void putTable(Class<?> clazz, Table table)
    {
        modelTableMap.put(clazz, table);
    }

    public Table getTable(Class<?> clazz)
    {
        return modelTableMap.get(clazz);
    }
}
