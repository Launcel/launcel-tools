package xyz.launcel.configuration;

import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.launcel.handle.GlobalExceptionHandle;
import xyz.launcel.lang.PrimyGsonBuilder;
import xyz.launcel.aspejct.ControllerParamValidateAspejct;
import xyz.launcel.prop.CorsProperties;
import xyz.launcel.prop.GsonConverterProperties;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(value = {CorsProperties.class, GsonConverterProperties.class})
public class WebKitAutoConfiguration extends WebMvcConfigurerAdapter {

    private final CorsProperties corsProperties;

    private final GsonConverterProperties gsonConverterProperties;

    public WebKitAutoConfiguration(CorsProperties corsProperties, GsonConverterProperties gsonConverterProperties) {
        this.corsProperties = corsProperties;
        this.gsonConverterProperties = gsonConverterProperties;
    }

    /**
     * 用 gson 替换 jackson
     */
    @ConditionalOnProperty(prefix = "web.gson-converter", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        GsonBuilder gsonBuilder = new PrimyGsonBuilder().setDateFormat(gsonConverterProperties.getDateFormat()).getGsonBuilder();
        converter.setGson(gsonBuilder.create());
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @ConditionalOnProperty(prefix = "web.cors", value = "enabled", havingValue = "true")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsProperties.getPathPattern()).allowedOrigins(corsProperties.getAllowedOrigins()).
                allowCredentials(true).allowedMethods(corsProperties.getMethods()).maxAge(corsProperties.getMaxAge());
        super.addCorsMappings(registry);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        super.configureContentNegotiation(configurer);
    }

    @ConditionalOnProperty(prefix = "web.global-exception", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public GlobalExceptionHandle globalExceptionHandle() {
        return new GlobalExceptionHandle();
    }

    @ConditionalOnProperty(prefix = "web.aspejct", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public ControllerParamValidateAspejct controllerParamValidateAspejct() {
        return new ControllerParamValidateAspejct();
    }
}
