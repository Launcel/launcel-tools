package xyz.launcel.autoconfigure;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(value = {CorsProperties.class, UploadProperties.class, JsonProperties.class})
public class WebKitAutoConfiguration implements WebMvcConfigurer
{
    private final CorsProperties   corsProperties;
    private final UploadProperties uploadProperties;
    private final JsonProperties   jsonPropertie;

    public WebKitAutoConfiguration(CorsProperties corsProperties, UploadProperties uploadProperties, JsonProperties jsonPropertie)
    {
        System.out.println("init WebKitAutoConfiguration....");
        this.corsProperties = corsProperties;
        this.uploadProperties = uploadProperties;
        this.jsonPropertie = jsonPropertie;
    }

    /**
     * 用 gson 替换 jackson
     */
    @ConditionalOnProperty(prefix = "web.json-converter", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        System.out.println("init web.json-converter...");
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        var gsonConverter = new GsonHttpMessageConverter();
        DefaultGsonBuilder.builder()
                .dateFormat(DateFormat.getByName(jsonPropertie.getDateFormat()))
                .floatingPointValues(jsonPropertie.getFloatingPointValue())
                .formatPrint(jsonPropertie.getFormatPrint())
                .serializeNull(jsonPropertie.getSerializeNull())
                .version(jsonPropertie.getVersion())
                .build();
        var list = new ArrayList<MediaType>();
        list.add(new MediaType("text", "plain", StandardCharsets.UTF_8));
        list.add(new MediaType("text", "html", StandardCharsets.UTF_8));
        list.add(new MediaType("application", "xml", StandardCharsets.UTF_8));
        list.add(new MediaType("application", "json", StandardCharsets.UTF_8));
        gsonConverter.setSupportedMediaTypes(list);
        gsonConverter.setGson(DefaultGsonBuilder.create());
        converters.add(gsonConverter);
    }

    @ConditionalOnProperty(prefix = "web.cors", value = "enabled", havingValue = "true")
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        System.out.println("init web.cors...");
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

    //    @ConditionalOnProperty(prefix = "web.aspejct", value = "enabled", havingValue = "true")
    //    @Bean
    //    public ControllerParamValidateAspejct controllerParamValidateAspejct() { return new ControllerParamValidateAspejct(); }

    @Bean(name = "globalExceptionHandler")
    @ConditionalOnProperty(prefix = "web.global-exception", value = "enabled", havingValue = "true")
    public GlobalExceptionHandler globalExceptionHandler()
    {
        System.out.println("init globalExceptionHandler...");
        return new GlobalExceptionHandler();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "web.upload", value = "enabled", havingValue = "true")
    public MultipartConfigElement multipartConfigElement()
    {
        System.out.println("inti multipartConfigElement...");
        var factory = new MultipartConfigFactory();
        factory.setMaxFileSize(uploadProperties.getMaxSize());
        factory.setMaxRequestSize(uploadProperties.getMaxSize());
        UploadLocalUtil.init(uploadProperties);
        return factory.createMultipartConfig();
    }
}
