package xyz.launcel.webkit.autoconfigure;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.launcel.webkit.convert.IntegerCodeToEnumConverterFactory;
import xyz.launcel.webkit.handler.GlobalExceptionHandler;
import xyz.launcel.log.Log;
import xyz.launcel.webkit.properties.CorsProperties;
import xyz.launcel.webkit.properties.JsonProperties;
import xyz.launcel.webkit.properties.UploadProperties;
import xyz.launcel.webkit.util.UploadLocalUtil;
import xyz.launcel.common.utils.json.builder.DateFormat;
import xyz.launcel.common.utils.json.builder.DefaultGsonBuilder;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(value = {CorsProperties.class, UploadProperties.class, JsonProperties.class})
@RequiredArgsConstructor
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
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters)
    {
        Log.info("init web.json-converter...");
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        var gsonConverter = new GsonHttpMessageConverter();
        DefaultGsonBuilder.builder()
                .dateFormat(DateFormat.getByName(jsonPropertie.getDateFormat()))
                .floatingPointValues(jsonPropertie.getFloatingPointValue())
                .formatPrint(jsonPropertie.getFormatPrint())
                .serializeNull(jsonPropertie.getSerializeNull())
                .version(jsonPropertie.getVersion())
                .build();
        List<MediaType> list = Lists.newArrayList();
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
    public void addCorsMappings(final CorsRegistry registry)
    {
        Log.info("init web.cors...");
        registry.addMapping(corsProperties.getPathPattern())
                .allowedOrigins(corsProperties.getAllowedOrigins())
                .allowCredentials(true)
                .allowedMethods(corsProperties.getMethods())
                .maxAge(corsProperties.getMaxAge());
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer)
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
        Log.info("init globalExceptionHandler...");
        return new GlobalExceptionHandler();
    }

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addConverterFactory(new IntegerCodeToEnumConverterFactory());
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "web.upload", value = "enabled", havingValue = "true")
    public MultipartConfigElement multipartConfigElement()
    {
        Log.info("inti multipartConfigElement...");
        var factory = new MultipartConfigFactory();
        factory.setMaxFileSize(uploadProperties.getMaxSize());
        factory.setMaxRequestSize(uploadProperties.getMaxSize());
        UploadLocalUtil.init(uploadProperties);
        return factory.createMultipartConfig();
    }
}
