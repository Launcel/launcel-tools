package xyz.launcel.constant;

public interface SessionConstant {

    String dataSourceConfigPrefix = "db.jdbc";

    String mybatisConfigPrefix = "db.mybatis";

    String dataSourceName = "dataSource";

    String sessionFactoryName = "SqlSessionFactory";

    String sessionFactoryDataSourceName = "dataSource";

    String configLocationName = "configLocation";
    String configLocationValue = "classpath:mybatis/mybatis-config.xml";

    String typeAliasesPackageName = "typeAliasesPackage";

    String pluginName = "plugins";

    String mapperLocationName = "mapperLocations";

    String mybatisName = "MapperScannerConfigurer";

    String sqlSessionFactoryName = "sqlSessionFactoryBeanName";

    String basePackageName = "basePackage";
}
