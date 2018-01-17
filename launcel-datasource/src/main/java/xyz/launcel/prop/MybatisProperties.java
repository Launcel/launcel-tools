package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "db.mybatis")
public class MybatisProperties {

    private String config;

    private String aliasesPackage;

    private String mapperResource;

    private String mapperPackage;

    public String getConfig() {
        return config;
    }

    public String getAliasesPackage() {
        return aliasesPackage;
    }

    public String getMapperResource() {
        return mapperResource;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }
}
