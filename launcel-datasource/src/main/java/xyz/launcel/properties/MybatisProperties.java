package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionFactoryConstant;

import java.util.List;

@ConfigurationProperties(prefix = SessionFactoryConstant.mybatisConfigPrefix)
public class MybatisProperties {
    
    private MybatisPropertie main;
    
    private List<MybatisPropertie> others;
    
    public MybatisPropertie getMain() {
        return main;
    }
    
    public void setMain(MybatisPropertie main) {
        this.main = main;
    }
    
    public List<MybatisPropertie> getOthers() {
        return others;
    }
    
    public void setOthers(List<MybatisPropertie> others) {
        this.others = others;
    }
    
    public static class MybatisPropertie {
        
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
