package xyz.launcel.test;

import lombok.RequiredArgsConstructor;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.launcel.exception.ProfessionException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@WebAppConfiguration
@RequiredArgsConstructor
public abstract class AbstractMvcTest extends AbstractTest
{

    private final WebApplicationContext wac;
    private       MockMvc               mockMvc;

    private void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysExpect(MockMvcResultMatchers.status().isOk())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
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
            //noinspection ConstantConditions
            return getReturn(getMockMvc().perform(builder(uri, params, "get")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ProfessionException("请求失败...");
        }
    }

    protected String deleting(String uri, Map<String, String> params)
    {
        try
        {
            //noinspection ConstantConditions
            return getReturn(getMockMvc().perform(builder(uri, params, "delete")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ProfessionException("请求失败...");
        }
    }

    protected String posting(String uri, Map<String, String> params)
    {
        try
        {
            //noinspection ConstantConditions
            return getReturn(getMockMvc().perform(builder(uri, params, "post")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ProfessionException("请求失败...");
        }
    }

    protected String puting(String uri, Map<String, String> params)
    {
        try
        {
            //noinspection ConstantConditions
            return getReturn(getMockMvc().perform(builder(uri, params, "put")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ProfessionException("请求失败...");
        }
    }

    private MockHttpServletRequestBuilder builder(String uri, Map<String, String> params, String requestMethod)
    {
        var                           method  = requestMethod.toLowerCase();
        MockHttpServletRequestBuilder builder = null;
        switch (method)
        {
            case "get":
                builder = MockMvcRequestBuilders.get(uri);
                break;
            case "post":
                builder = MockMvcRequestBuilders.post(uri);
                break;
            case "put":
                builder = MockMvcRequestBuilders.put(uri);
                break;
            case "delete":
                builder = MockMvcRequestBuilders.delete(uri);
                break;
            default:
                break;
        }
        return addParam(builder, params);
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
            throw new ProfessionException("请求失败...");
        }
    }
}
