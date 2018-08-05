package xyz.launcel.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionFactoryConstant;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = SessionFactoryConstant.mybatisConfigPrefix)
public class MybatisProperties
{

    private MybatisPropertie main;

    private List<MybatisPropertie> others;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MybatisPropertie
    {

        private String refName;

        private String aliasesPackage;

        private String mapperResource;

        private String mapperPackage;

    }


}