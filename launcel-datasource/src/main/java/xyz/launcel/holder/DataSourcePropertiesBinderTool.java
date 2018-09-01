package xyz.launcel.holder;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import xyz.launcel.constant.SessionFactoryConstant;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.properties.DataSourceProperties;
import xyz.launcel.properties.MybatisProperties;
import xyz.launcel.properties.RoleDataSourceHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by launcel on 2018/9/1.
 */
@Getter
@Setter
public class DataSourcePropertiesBinderTool
{

    private Map<String, MybatisProperties.MybatisPropertie> multipleMybatis;
    private DataSourceProperties                            dataSourceProperties;

    /**********************************************************************/
    private MybatisProperties.MybatisPropertie dynamicMybatisPropertie;
    private List<DataSourceConfigMap>          dynamicDataSourceConfigMapList;

    private boolean isDebugSql = false;

    public void binderDataSource(Binder binder)
    {
        dataSourceProperties = binder.bind(SessionFactoryConstant.dataSourceConfigPrefix, Bindable.of(DataSourceProperties.class)).get();
        if (Objects.nonNull(dataSourceProperties) && dataSourceProperties.getUseDynamicDataSource())
            binderDynamicDataSource(dataSourceProperties);
    }

    private void binderDynamicDataSource(DataSourceProperties dataSourceProperties)
    {
        if (Objects.nonNull(dataSourceProperties.getMain()))
        {
            DataSourceProperties.DataSourcePropertie main       = dataSourceProperties.getMain();
            HikariDataSource                         dataSource = new HikariDataSource(main.getHikariConfig());
            if (Objects.isNull(dynamicDataSourceConfigMapList))
                dynamicDataSourceConfigMapList = new ArrayList<>();
            dynamicDataSourceConfigMapList.add(new DataSourceConfigMap(main.getName(), main.getEnableTransactal(), main.getRoleDataSource(), dataSource));
            if (main.getRoleDataSource())
                RoleDataSourceHolder.setDataSource(dataSource);
            isDebugSql(dataSourceProperties.getMain());
        }
        if (CollectionUtils.isNotEmpty(dataSourceProperties.getOthers()))
        {
            dataSourceProperties.getOthers().forEach(other ->
            {
                isDebugSql(other);
                HikariDataSource dataSource = new HikariDataSource(other.getHikariConfig());
                dynamicDataSourceConfigMapList.add(new DataSourceConfigMap(other.getName(), other.getEnableTransactal(), other.getRoleDataSource(), dataSource));
                if (other.getRoleDataSource())
                    RoleDataSourceHolder.setDataSource(dataSource);
            });
        }
    }

    public void binderMybatisConfig(Binder binder)
    {
        MybatisProperties mybatisProperties = binder.bind(SessionFactoryConstant.mybatisConfigPrefix, Bindable.of(MybatisProperties.class)).get();
        if (Objects.isNull(mybatisProperties) || Objects.isNull(mybatisProperties.getMain()) || CollectionUtils.isEmpty(mybatisProperties.getOthers()))
            System.exit(-1);
        if (dataSourceProperties.getUseDynamicDataSource())
        {
            binderDynamicMybatisPropertie(mybatisProperties);
            return;
        }
        binderMultipleMybatisPropertie(mybatisProperties);
    }

    private void binderDynamicMybatisPropertie(MybatisProperties mybatisProperties)
    {
        dynamicMybatisPropertie = mybatisProperties.getMain();
    }

    private void binderMultipleMybatisPropertie(MybatisProperties mybatisProperties)
    {
        if (Objects.isNull(multipleMybatis))
            multipleMybatis = new HashMap<>();
        multipleMybatis.put(mybatisProperties.getMain().getRefName(), mybatisProperties.getMain());
        if (CollectionUtils.isNotEmpty(mybatisProperties.getOthers()))
            mybatisProperties.getOthers().forEach(mybatisPropertie -> multipleMybatis.put(mybatisPropertie.getRefName(), mybatisPropertie));
    }

    private void isDebugSql(DataSourceProperties.DataSourcePropertie dataSourcePropertie)
    {
        if (!isDebugSql && dataSourcePropertie.getDebugSql())
        {
            isDebugSql = true;
            return;
        }
        isDebugSql = false;
    }

    public Boolean debugSql()
    {
        return isDebugSql;
    }
}
