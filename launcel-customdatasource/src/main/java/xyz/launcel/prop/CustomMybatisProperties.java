package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybatis.custom")
public class CustomMybatisProperties {

    private String config;

    private String aliasesPackage;

    private String mapperResource;

    public String getConfig() {
        return config;
    }

    public String getAliasesPackage() {
        return aliasesPackage;
    }

    public String getMapperResource() {
        return mapperResource;
    }
}
