package xyz.launcel.jdbc.holder;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by launcel on 2018/8/20.
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{
    @Override
    protected Object determineCurrentLookupKey()
    {
        return DbContextHolder.getDbType();
    }
}
