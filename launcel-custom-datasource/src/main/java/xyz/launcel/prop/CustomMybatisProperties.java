package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "custom.mybatis")
public class CustomMybatisProperties {

    private Map<String, PrimyMybatisPropertie> list;

    public Map<String, PrimyMybatisPropertie> getList() {
        return list;
    }

    public void setList(Map<String, PrimyMybatisPropertie> list) {
        this.list = list;
    }

    public static class PrimyMybatisPropertie {
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
