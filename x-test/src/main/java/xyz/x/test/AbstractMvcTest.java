package xyz.x.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.x.common.exception.BusinessException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

@WebAppConfiguration
@RequiredArgsConstructor
public abstract class AbstractMvcTest extends AbstractTest
{

    private final WebApplicationContext wac;
    private       MockMvc               mockMvc;

    private void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysExpect(MockMvcResultMatchers.status().isOk()).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    private MockMvc getMockMvc()
    {
        setup();
        return mockMvc;
    }

    protected String getting(String uri, Map<String, String> params)
    {
        try
        {
            return getReturn(getMockMvc().perform(builder(uri, params, "get")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("请求失败...");
        }
    }

    protected String deleting(String uri, Map<String, String> params)
    {
        try
        {
            return getReturn(getMockMvc().perform(builder(uri, params, "delete")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("请求失败...");
        }
    }

    protected String posting(String uri, Map<String, String> params)
    {
        try
        {
            return getReturn(getMockMvc().perform(builder(uri, params, "post")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("请求失败...");
        }
    }

    protected String puting(String uri, Map<String, String> params)
    {
        try
        {
            return getReturn(getMockMvc().perform(builder(uri, params, "put")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("请求失败...");
        }
    }

    private MockHttpServletRequestBuilder builder(String uri, Map<String, String> params, String requestMethod)
    {
        var method = HttpMethod.getInstance(requestMethod.toLowerCase());
        return addParam(method.send(uri), params);
    }

    private MockHttpServletRequestBuilder addParam(MockHttpServletRequestBuilder builder, Map<String, String> params)
    {
        for (var entry : params.entrySet())
        {
            builder.param(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    private String getReturn(ResultActions resultActions)
    {
        try
        {
            return resultActions.andReturn().getResponse().getContentAsString();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            throw new BusinessException("请求失败...");
        }
    }

    @RequiredArgsConstructor
    @Getter
    public enum HttpMethod implements HttpMethodBuilder
    {
        GET("get")
                {
                    @Override
                    public MockHttpServletRequestBuilder send(String url)
                    {
                        return MockMvcRequestBuilders.get(url);
                    }
                },
        POST("post")
                {
                    @Override
                    public MockHttpServletRequestBuilder send(String url)
                    {
                        return MockMvcRequestBuilders.post(url);
                    }
                },
        PUT("put")
                {
                    @Override
                    public MockHttpServletRequestBuilder send(String url)
                    {
                        return MockMvcRequestBuilders.put(url);
                    }
                },
        DELETE("delete")
                {
                    @Override
                    public MockHttpServletRequestBuilder send(String url)
                    {
                        return MockMvcRequestBuilders.delete(url);
                    }
                },
        ;

        private final String code;

        public static HttpMethod getInstance(String requestMethod)
        {
            return Arrays.stream(values()).filter(v -> v.code.equalsIgnoreCase(requestMethod)).findFirst().orElseThrow(RuntimeException::new);
        }

    }

    interface HttpMethodBuilder
    {
        MockHttpServletRequestBuilder send(String url);
    }
}
