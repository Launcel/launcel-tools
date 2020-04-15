package xyz.launcel.jdbc.holder;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.support.BeanDefinitionRegistryTool;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.common.utils.Json;
import xyz.launcel.jdbc.SessionFactoryConstant;
import xyz.launcel.jdbc.properties.DataSourceProperties;
import xyz.launcel.jdbc.properties.MybatisProperties;
import xyz.launcel.jdbc.properties.RoleDataSourceHolder;
import xyz.launcel.log.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//import xyz.launcel.jdbc.interceptor.ParamInterceptor;

//import xyz.launcel.jdbc.interceptor.PageInterceptor;

/**
 * Created by launcel on 2018/9/1.
 */
@RequiredArgsConstructor
public class MultipleDataSourceRegistryTool
{
    private final Map<String, MybatisProperties.MybatisPropertie> multipleMybatis;
    private final DataSourceProperties                            dataSourceProperties;

    /**
     * 为当前 dataSource 注册事物
     */
    public static void registTransactal(String key, BeanDefinitionRegistry registry, HikariDataSource hikariDataSource)
    {
        var transactalAbd            = BeanDefinitionRegistryTool.decorateAbd(DataSourceTransactionManager.class);
        var transactaDataSourceValue = transactalAbd.getPropertyValues();

        transactaDataSourceValue.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        transactaDataSourceValue.addPropertyValue("enforceReadOnly", false);

        BeanDefinitionRegistryTool.registryBean(key, registry, transactalAbd);
    }

    public void registrMultipleBean(BeanDefinitionRegistry registry)
    {
        if (Objects.isNull(dataSourceProperties) && CollectionUtils.isEmpty(multipleMybatis))
        {
            ExceptionFactory.error("_DEFINE_ERROR_CODE_010", ">>>  datasource propertie config or mybatis propertie config is null !!");
        }
        Log.info("dataSourceProperties is : {}\nmultipleMybatis is : {}", Json.toString(dataSourceProperties), Json.toString(multipleMybatis));
        registMultipeDataSource(dataSourceProperties.getMain(), registry);
        if (CollectionUtils.isNotEmpty(dataSourceProperties.getOthers()))
        {
            dataSourceProperties.getOthers().forEach(propertie -> registMultipeDataSource(propertie, registry));
        }
    }

    private void registMultipeDataSource(DataSourceProperties.DataSourcePropertie dataSourcePropertie, BeanDefinitionRegistry registry)
    {
        var mybatisPropertie      = multipleMybatis.get(dataSourcePropertie.getName());
        var sqlSessionFactoryName = dataSourcePropertie.getName() + SessionFactoryConstant.sessionFactoryName;
        var dataSource            = new HikariDataSource(dataSourcePropertie.getHikariConfig());

        registSessionFactory(registry, dataSource, mybatisPropertie, sqlSessionFactoryName, dataSourcePropertie.getDebugSql());
        registMapperScannerConfigurer(registry, mybatisPropertie, sqlSessionFactoryName);

        if (dataSourcePropertie.getEnableTransactal())
        {
            registTransactal(dataSourcePropertie.getName(), registry, dataSource);
        }
        if (dataSourcePropertie.getRoleDataSource())
        {
            RoleDataSourceHolder.setDataSource(dataSource);
        }
    }

    /**
     * 注册 sessionFactory
     */
    private void registSessionFactory(BeanDefinitionRegistry registry, HikariDataSource hikariDataSource, MybatisProperties.MybatisPropertie mybatisPropertie,
                                      String sqlSessionFactoryBeanName, boolean isDebugSql)
    {
        var sqlSessionAbd = BeanDefinitionRegistryTool.decorateAbd(SqlSessionFactoryBean.class);
        var sqlSession    = sqlSessionAbd.getPropertyValues();

        sqlSession.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        sqlSession.addPropertyValue(SessionFactoryConstant.configLocationName, "classpath:mybatis/mybatis-config.xml");
        sqlSession.addPropertyValue(SessionFactoryConstant.typeAliasesPackageName, mybatisPropertie.getAliasesPackage());

        List<Interceptor> interceptors = new ArrayList<>(2);
        //        interceptors.add(new PageInterceptor());
        interceptors.add(new PageInterceptor());
//                if (isDebugSql)
        //        {
        //            interceptors.add(new ParamInterceptor());
        //        }
        sqlSession.addPropertyValue(SessionFactoryConstant.pluginName, interceptors);
        try
        {
            sqlSession.addPropertyValue(SessionFactoryConstant.mapperLocationName,
                    new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        }
        catch (IOException e)
        {
            ExceptionFactory.error("_DEFINE_ERROR_CODE_010", ">>>  connot load resource :" + mybatisPropertie.getMapperResource() + " !!");
        }
        BeanDefinitionRegistryTool.registryBean(sqlSessionFactoryBeanName, registry, sqlSessionAbd);
    }

    /**
     * 注册 MapperScannerConfigurer
     */
    private void registMapperScannerConfigurer(BeanDefinitionRegistry registry, MybatisProperties.MybatisPropertie mybatisPropertie,
                                               String sqlSessionFactoryBeanName)
    {
        var abd                     = BeanDefinitionRegistryTool.decorateAbd(MapperScannerConfigurer.class);
        var mapperScannerConfigurer = abd.getPropertyValues();

        mapperScannerConfigurer.addPropertyValue(SessionFactoryConstant.sqlSessionFactoryName, sqlSessionFactoryBeanName);
        mapperScannerConfigurer.addPropertyValue(SessionFactoryConstant.basePackageName, mybatisPropertie.getMapperPackage());
        var mybatisBeanName = mybatisPropertie.getDataSourceName() + SessionFactoryConstant.mybatisName;

        BeanDefinitionRegistryTool.registryBean(mybatisBeanName, registry, abd);
    }
}
