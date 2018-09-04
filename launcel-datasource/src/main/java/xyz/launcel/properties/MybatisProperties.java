package xyz.launcel.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @NoArgsConstructor
    public static class MybatisPropertie
    {
        @NonNull
        private String refName = "main";
        @NonNull
        private String aliasesPackage;
        @NonNull
        private String mapperResource;
        @NonNull
        private String mapperPackage;

    }


}
