package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "db.custom.mybatis")
public class CustomMybatisProperties {

    private Map<String, CustomMybatisPropertie> list;

    public Map<String, CustomMybatisPropertie> getList() {
        return list;
    }

    public void setList(Map<String, CustomMybatisPropertie> list) {
        this.list = list;
    }

    public static class CustomMybatisPropertie {
        private String refName;

        private String aliasesPackage;

        private String mapperResource;

        private String mapperPackage;

        public String getRefName() {
            return refName;
        }

        public void setRefName(String refName) {
            this.refName = refName;
        }

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
}
