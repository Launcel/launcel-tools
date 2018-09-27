package xyz.launcel.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.launcel.aspejct.ControllerParamValidateAspejct;
import xyz.launcel.handle.GlobalExceptionHandle;
import xyz.launcel.properties.CorsProperties;
import xyz.launcel.properties.JsonConverterProperties;
import xyz.launcel.properties.UploadProperties;
import xyz.launcel.upload.UploadLocalUtil;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(value = {CorsProperties.class, UploadProperties.class, JsonConverterProperties.class})
@RequiredArgsConstructor
public class WebKitAutoConfiguration implements WebMvcConfigurer
{
    private final CorsProperties          corsProperties;
    private final UploadProperties        uploadProperties;
    private final JsonConverterProperties jsonConverterProperties;


    /**
     * 用 gson 替换 jackson
     */
    @ConditionalOnProperty(prefix = "web.json-converter", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        //        GsonHttpMessageConverter converter   = new GsonHttpMessageConverter();
        //        GsonBuilder              gsonBuilder = new PrimyGsonBuilder().setDateFormat(jsonConverterProperties.getDateFormat()).getGsonBuilder();
        //        converter.setGson(gsonBuilder.create());
        //        converters.add(converter);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig               fastJsonConfig               = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat);
        fastJsonConfig.setDateFormat(jsonConverterProperties.getDateFormat());
        converters.add(fastJsonHttpMessageConverter);
    }

    @ConditionalOnProperty(prefix = "web.cors", value = "enabled", havingValue = "true")
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping(corsProperties.getPathPattern()).allowedOrigins(corsProperties.getAllowedOrigins()).
                allowCredentials(true).allowedMethods(corsProperties.getMethods()).maxAge(corsProperties.getMaxAge());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @ConditionalOnProperty(prefix = "web.global-exception", value = "enabled", havingValue = "true")
    @Bean
    public GlobalExceptionHandle globalExceptionHandle() { return new GlobalExceptionHandle(); }

    @ConditionalOnProperty(prefix = "web.aspejct", value = "enabled", havingValue = "true")
    @Bean
    public ControllerParamValidateAspejct controllerParamValidateAspejct() { return new ControllerParamValidateAspejct(); }

    @ConditionalOnProperty(prefix = "web.upload", value = "enabled", havingValue = "true")
    @Bean
    @Primary
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(uploadProperties.getMaxSize());
        factory.setMaxRequestSize(uploadProperties.getMaxSize());
        UploadLocalUtil.init(uploadProperties);
        return factory.createMultipartConfig();
    }

}
