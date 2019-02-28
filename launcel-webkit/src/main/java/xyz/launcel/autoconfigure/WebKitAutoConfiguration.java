package xyz.launcel.autoconfigure;

import lombok.var;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.launcel.handler.GlobalExceptionHandler;
import xyz.launcel.properties.CorsProperties;
import xyz.launcel.properties.JsonProperties;
import xyz.launcel.properties.UploadProperties;
import xyz.launcel.upload.UploadLocalUtil;
import xyz.launcel.utils.json.builder.DateFormat;
import xyz.launcel.utils.json.builder.DefaultGsonBuilder;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(value = {CorsProperties.class, UploadProperties.class, JsonProperties.class})
public class WebKitAutoConfiguration implements WebMvcConfigurer
{
    private final CorsProperties   corsProperties;
    private final UploadProperties uploadProperties;
    private final JsonProperties   jsonPropertie;

    /**
     * 用 gson 替换 jackson
     */
    @ConditionalOnProperty(prefix = "web.json-converter", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        var gsonConverter = new GsonHttpMessageConverter();
        DefaultGsonBuilder.builder()
                .dateFormat(DateFormat.getByName(jsonPropertie.getDateFormat()))
                .floatingPointValues(jsonPropertie.getFloatingPointValue())
                .formatPrint(jsonPropertie.getFormatPrint())
                .serializeNull(jsonPropertie.getSerializeNull())
                .version(jsonPropertie.getVersion())
                .build();
        gsonConverter.setGson(DefaultGsonBuilder.create());
        converters.add(gsonConverter);
    }

    @ConditionalOnProperty(prefix = "web.cors", value = "enabled", havingValue = "true")
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping(corsProperties.getPathPattern())
                .allowedOrigins(corsProperties.getAllowedOrigins())
                .allowCredentials(true)
                .allowedMethods(corsProperties.getMethods())
                .maxAge(corsProperties.getMaxAge());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Bean(name = "globalExceptionHandle")
    @ConditionalOnProperty(prefix = "web.global-exception", value = "enabled", havingValue = "true")
    public GlobalExceptionHandler globalExceptionHandle()
    {
        return new GlobalExceptionHandler();
    }

    //    @ConditionalOnProperty(prefix = "web.aspejct", value = "enabled", havingValue = "true")
    //    @Bean
    //    public ControllerParamValidateAspejct controllerParamValidateAspejct() { return new ControllerParamValidateAspejct(); }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "web.upload", value = "enabled", havingValue = "true")
    public MultipartConfigElement multipartConfigElement()
    {
        var factory = new MultipartConfigFactory();
        factory.setMaxFileSize(uploadProperties.getMaxSize());
        factory.setMaxRequestSize(uploadProperties.getMaxSize());
        UploadLocalUtil.init(uploadProperties);
        return factory.createMultipartConfig();
    }

    public WebKitAutoConfiguration(CorsProperties corsProperties, UploadProperties uploadProperties, JsonProperties jsonPropertie)
    {
        this.corsProperties = corsProperties;
        this.uploadProperties = uploadProperties;
        this.jsonPropertie = jsonPropertie;
        System.out.println("init WebKitAutoConfiguration....");
    }
}
