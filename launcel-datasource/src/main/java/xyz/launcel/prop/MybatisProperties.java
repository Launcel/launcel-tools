package xyz.launcel.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "launcel.mybatis")
public class MybatisProperties {
    @Value("${db.mybatis.aliases-package}")
    private String aliasesPackage;
    @Value("${db.mybatis.mapper-resource}")
    private String mapperResource;
    @Value("${db.mybatis.mapper-package}")
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
