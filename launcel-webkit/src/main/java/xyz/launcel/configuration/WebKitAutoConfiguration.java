package xyz.launcel.configuration;

import com.google.gson.GsonBuilder;
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.launcel.aspejct.ControllerParamValidateAspejct;
import xyz.launcel.handle.GlobalExceptionHandle;
import xyz.launcel.json.builder.PrimyGsonBuilder;
import xyz.launcel.properties.CorsProperties;
import xyz.launcel.properties.JsonConverterProperties;
import xyz.launcel.properties.UploadProperties;
import xyz.launcel.upsdk.UpSDK;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(value = {CorsProperties.class, JsonConverterProperties.class, UploadProperties.class})
public class WebKitAutoConfiguration extends WebMvcConfigurerAdapter
{

    private final CorsProperties corsProperties;

    private final JsonConverterProperties jsonConverterProperties;

    private final UploadProperties uploadProperties;

    public WebKitAutoConfiguration(CorsProperties corsProperties, JsonConverterProperties jsonConverterProperties, UploadProperties uploadProperties)
    {
        this.corsProperties = corsProperties;
        this.jsonConverterProperties = jsonConverterProperties;
        this.uploadProperties = uploadProperties;
    }

    /**
     * 用 gson 替换 jackson
     */
    @ConditionalOnProperty(prefix = "web.json-converter", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        GsonHttpMessageConverter converter   = new GsonHttpMessageConverter();
        GsonBuilder              gsonBuilder = new PrimyGsonBuilder().setDateFormat(jsonConverterProperties.getDateFormat()).getGsonBuilder();
        converter.setGson(gsonBuilder.create());
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @ConditionalOnProperty(prefix = "web.cors", value = "enabled", havingValue = "true")
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping(corsProperties.getPathPattern()).allowedOrigins(corsProperties.getAllowedOrigins()).
                allowCredentials(true).allowedMethods(corsProperties.getMethods()).maxAge(corsProperties.getMaxAge());
        super.addCorsMappings(registry);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        super.configureContentNegotiation(configurer);
    }

    @ConditionalOnProperty(prefix = "web.global-exception", value = "enabled", havingValue = "true")
    @Bean
    public GlobalExceptionHandle globalExceptionHandle() { return new GlobalExceptionHandle(); }

    @ConditionalOnProperty(prefix = "web.aspejct", value = "enabled", havingValue = "true")
    @Bean
    public ControllerParamValidateAspejct controllerParamValidateAspejct() { return new ControllerParamValidateAspejct(); }

    @ConditionalOnProperty(prefix = "web.upload", value = "enabled", havingValue = "true")
    @Bean(name = "upSDK")
    public UpSDK upSDK() { return new UpSDK(uploadProperties); }

    @ConditionalOnProperty(prefix = "web.upload", value = "enabled", havingValue = "true")
    @Bean
    @Primary
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(uploadProperties.getMaxSize());
        factory.setMaxRequestSize(100);
        return factory.createMultipartConfig();
    }

}
