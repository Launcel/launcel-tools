package xyz.launcel.jdbc.holder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.var;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.jdbc.SessionFactoryConstant;
import xyz.launcel.jdbc.properties.DataSourceProperties;
import xyz.launcel.jdbc.properties.MybatisProperties;
import xyz.launcel.jdbc.properties.RoleDataSourceHolder;

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

    private Map<String, MybatisProperties.MybatisPropertie> multipleMybatis = Maps.newHashMap();

    private DataSourceProperties               dataSourceProperties;
    /**********************************************************************/
    private MybatisProperties.MybatisPropertie dynamicMybatisPropertie;

    private List<DataSourceConfigMap> dynamicDataSourceConfigMapList = Lists.newArrayList();

    private boolean isDebugSql = false;

    public void binderDataSource(Binder binder)
    {
        dataSourceProperties = binder.bind(SessionFactoryConstant.dataSourceConfigPrefix, Bindable.of(DataSourceProperties.class)).get();
        if (Objects.nonNull(dataSourceProperties) && dataSourceProperties.getUseDynamicDataSource())
        { binderDynamicDataSource(dataSourceProperties); }
    }

    private void binderDynamicDataSource(DataSourceProperties dataSourceProperties)
    {
        if (Objects.nonNull(dataSourceProperties.getMain()))
        {
            DataSourceProperties.DataSourcePropertie main       = dataSourceProperties.getMain();
            var                                      dataSource = new HikariDataSource(main.getHikariConfig());
            dynamicDataSourceConfigMapList.add(new DataSourceConfigMap(main.getName(), main.getEnableTransactal(), main.getRoleDataSource(), dataSource));
            if (main.getRoleDataSource())
            { RoleDataSourceHolder.setDataSource(dataSource); }
        }
        if (CollectionUtils.isNotEmpty(dataSourceProperties.getOthers()))
        {
            dataSourceProperties.getOthers().forEach(other -> {
                var dataSource = new HikariDataSource(other.getHikariConfig());
                dynamicDataSourceConfigMapList.add(
                        new DataSourceConfigMap(other.getName(), other.getEnableTransactal(), other.getRoleDataSource(), dataSource));
                if (other.getRoleDataSource())
                { RoleDataSourceHolder.setDataSource(dataSource); }
            });
        }
    }

    public void binderMybatisConfig(Binder binder)
    {
        var mybatisProperties = binder.bind(SessionFactoryConstant.mybatisConfigPrefix, Bindable.of(MybatisProperties.class)).get();
        if (Objects.isNull(mybatisProperties) || Objects.isNull(mybatisProperties.getMain()) || CollectionUtils.isEmpty(mybatisProperties.getOthers()))
        { System.exit(-1); }
        if (dataSourceProperties.getUseDynamicDataSource())
        {
            binderDynamicMybatisPropertie(mybatisProperties);
            return;
        }
        binderMultipleMybatisPropertie(mybatisProperties);
    }

    private void binderDynamicMybatisPropertie(MybatisProperties mybatisProperties)
    {
        this.dynamicMybatisPropertie = mybatisProperties.getMain();
    }

    private void binderMultipleMybatisPropertie(MybatisProperties mybatisProperties)
    {
        multipleMybatis.put(mybatisProperties.getMain().getDataSourceName(), mybatisProperties.getMain());
        if (CollectionUtils.isNotEmpty(mybatisProperties.getOthers()))
        { mybatisProperties.getOthers().forEach(mybatisPropertie -> multipleMybatis.put(mybatisPropertie.getDataSourceName(), mybatisPropertie)); }
    }

}
