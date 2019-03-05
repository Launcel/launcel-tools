package xyz.launcel.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionFactoryConstant;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = SessionFactoryConstant.mybatisConfigPrefix)
public class MybatisProperties
{
    @NonNull
    private MybatisPropertie main;

    private List<MybatisPropertie> others;

    @Getter
    @Setter
    public static class MybatisPropertie
    {
        private String dataSourceName = "main";
        @NonNull
        private String aliasesPackage;
        @NonNull
        private String mapperResource;
        @NonNull
        private String mapperPackage;
    }
}
