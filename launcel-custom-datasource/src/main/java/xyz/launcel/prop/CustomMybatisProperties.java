package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "custom.mybatis")
public class CustomMybatisProperties {

    private List<PrimyMybatisPropertie> list;

    public List<PrimyMybatisPropertie> getList() {
        return list;
    }

    public void setList(List<PrimyMybatisPropertie> list) {
        this.list = list;
    }

    public static class PrimyMybatisPropertie {
        private String refName;

        private String aliasesPackage;

        private String mapperResource;

        private List<String> mapperPackage;

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

        public List<String> getMapperPackage() {
            return mapperPackage;
        }

        public void setMapperPackage(List<String> mapperPackage) {
            this.mapperPackage = mapperPackage;
        }
    }
}
