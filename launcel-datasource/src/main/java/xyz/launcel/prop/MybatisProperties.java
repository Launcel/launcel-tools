package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "db.mybatis")
public class MybatisProperties {
    private String aliasesPackage;
    private String mapperResource;
    private String mapperPackage;

    public String getAliasesPackage() {
        return aliasesPackage;
    }

    public void setAliasesPackage(String aliasesPackage) {
        this.aliasesPackage = aliasesPackage;
    }

    public String getMapperResource() {
        return mapperResource;
    }

    public void setMapperResource(String mapperResource) {
        this.mapperResource = mapperResource;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }
}
