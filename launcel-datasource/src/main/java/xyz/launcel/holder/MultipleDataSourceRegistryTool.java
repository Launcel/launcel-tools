package xyz.launcel.holder;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import xyz.launcel.bean.context.BeanDefinitionRegistryTool;
import xyz.launcel.constant.SessionFactoryConstant;
import xyz.launcel.exception.SystemError;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.interceptor.ParamInterceptor;
import xyz.launcel.json.Json;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.log.RootLogger;
import xyz.launcel.properties.DataSourceProperties;
import xyz.launcel.properties.MybatisProperties;
import xyz.launcel.properties.RoleDataSourceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by launcel on 2018/9/1.
 */
public class MultipleDataSourceRegistryTool
{
    private final Map<String, MybatisProperties.MybatisPropertie> multipleMybatis;
    private final DataSourceProperties                            dataSourceProperties;

    public MultipleDataSourceRegistryTool(Map<String, MybatisProperties.MybatisPropertie> multipleMybatis,
                                          DataSourceProperties dataSourceProperties)
    {
        this.multipleMybatis = multipleMybatis;
        this.dataSourceProperties = dataSourceProperties;
    }

    public void registrMultipleBean(BeanDefinitionRegistry registry)
    {
        if (Objects.isNull(dataSourceProperties) && CollectionUtils.isEmpty(multipleMybatis))
        {
            throw new SystemError("_DEFINE_ERROR_CODE_010", ">>>  datasource propertie config or mybatis propertie config is null !!");
        }
        RootLogger.info("dataSourceProperties is : {}\nmultipleMybatis is : {}", Json.toString(dataSourceProperties),
                Json.toString(multipleMybatis));
        registMultipeDataSource(dataSourceProperties.getMain(), registry);
        if (CollectionUtils.isNotEmpty(dataSourceProperties.getOthers()))
            dataSourceProperties.getOthers().forEach(propertie -> registMultipeDataSource(propertie, registry));
    }

    private void registMultipeDataSource(DataSourceProperties.DataSourcePropertie dataSourcePropertie, BeanDefinitionRegistry registry)
    {
        MybatisProperties.MybatisPropertie mybatisPropertie      = multipleMybatis.get(dataSourcePropertie.getName());
        String                             sqlSessionFactoryName = dataSourcePropertie.getName() + SessionFactoryConstant.sessionFactoryName;
        HikariDataSource                   dataSource            = new HikariDataSource(dataSourcePropertie.getHikariConfig());

        registSessionFactory(registry, dataSource, mybatisPropertie, sqlSessionFactoryName, dataSourcePropertie.getDebugSql());
        registMapperScannerConfigurer(registry, mybatisPropertie, sqlSessionFactoryName);

        if (dataSourcePropertie.getEnableTransactal())
            registTransactal(dataSourcePropertie.getName(), registry, dataSource);
        if (dataSourcePropertie.getRoleDataSource())
            RoleDataSourceHolder.setDataSource(dataSource);
    }

    /**
     * 注册 sessionFactory
     */
    private void registSessionFactory(BeanDefinitionRegistry registry, HikariDataSource hikariDataSource,
                                      MybatisProperties.MybatisPropertie mybatisPropertie, String sqlSessionFactoryBeanName,
                                      boolean isDebugSql)
    {
        AnnotatedGenericBeanDefinition sqlSessionAbd = BeanDefinitionRegistryTool.decorateAbd(SqlSessionFactoryBean.class);
        MutablePropertyValues          sqlSession    = sqlSessionAbd.getPropertyValues();

        sqlSession.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        sqlSession.addPropertyValue(SessionFactoryConstant.configLocationName, "classpath:mybatis/mybatis-config.xml");
        sqlSession.addPropertyValue(SessionFactoryConstant.typeAliasesPackageName, mybatisPropertie.getAliasesPackage());

        List<Interceptor> interceptors = new ArrayList<>(2);
        interceptors.add(new PageInterceptor());
        if (isDebugSql)
            interceptors.add(new ParamInterceptor());
        sqlSession.addPropertyValue(SessionFactoryConstant.pluginName, interceptors);
        try
        {
            sqlSession.addPropertyValue(SessionFactoryConstant.mapperLocationName,
                    new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        }
        catch (IOException e)
        {
            throw new SystemError("_DEFINE_ERROR_CODE_010", ">>>  connot load resource :" + mybatisPropertie.getMapperResource() + " !!");
        }
        BeanDefinitionRegistryTool.registryBean(sqlSessionFactoryBeanName, registry, sqlSessionAbd);
    }

    /**
     * 注册 MapperScannerConfigurer
     */
    private void registMapperScannerConfigurer(BeanDefinitionRegistry registry, MybatisProperties.MybatisPropertie mybatisPropertie,
                                               String sqlSessionFactoryBeanName)
    {
        AnnotatedGenericBeanDefinition abd                     = BeanDefinitionRegistryTool.decorateAbd(MapperScannerConfigurer.class);
        MutablePropertyValues          mapperScannerConfigurer = abd.getPropertyValues();

        mapperScannerConfigurer.addPropertyValue(SessionFactoryConstant.sqlSessionFactoryName, sqlSessionFactoryBeanName);
        mapperScannerConfigurer.addPropertyValue(SessionFactoryConstant.basePackageName, mybatisPropertie.getMapperPackage());
        String mybatisBeanName = mybatisPropertie.getDataSourceName() + SessionFactoryConstant.mybatisName;

        BeanDefinitionRegistryTool.registryBean(mybatisBeanName, registry, abd);
    }


    /**
     * 为当前 dataSource 注册事物
     */
    public static void registTransactal(String key, BeanDefinitionRegistry registry, HikariDataSource hikariDataSource)
    {
        AnnotatedGenericBeanDefinition transactalAbd = BeanDefinitionRegistryTool.decorateAbd(DataSourceTransactionManager.class);
        MutablePropertyValues transactaDataSourceValue = transactalAbd.getPropertyValues();

        transactaDataSourceValue.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        transactaDataSourceValue.addPropertyValue("enforceReadOnly", false);

        BeanDefinitionRegistryTool.registryBean(key, registry, transactalAbd);
    }
}
