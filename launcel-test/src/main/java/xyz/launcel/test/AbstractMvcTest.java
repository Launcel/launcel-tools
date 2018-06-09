package xyz.launcel.test;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@WebAppConfiguration
public abstract class AbstractMvcTest extends AbstractTest
{

    @Inject
    private WebApplicationContext wac;
    private MockMvc               mockMvc;

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
            //noinspection ConstantConditions
            return getReturn(getMockMvc().perform(builder(uri, params, "get")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
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
            return null;
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
            return null;
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
            return null;
        }
    }

    private MockHttpServletRequestBuilder builder(String uri, Map<String, String> params, String requestMethod)
    {
        if ("get".equals(requestMethod.toLowerCase()))
        {
            return addParam(MockMvcRequestBuilders.get(uri), params);
        }
        else if ("post".equals(requestMethod.toLowerCase()))
        {
            return addParam(MockMvcRequestBuilders.post(uri), params);
        }
        else if ("put".equals(requestMethod.toLowerCase()))
        {
            return addParam(MockMvcRequestBuilders.put(uri), params);
        }
        else if ("delete".equals(requestMethod.toLowerCase()))
        {
            return addParam(MockMvcRequestBuilders.delete(uri), params);
        }
        else
        {
            return null;
        }
    }

    private MockHttpServletRequestBuilder addParam(MockHttpServletRequestBuilder builder, Map<String, String> params)
    {
        for (Map.Entry<String, String> entry : params.entrySet())
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
            return null;
        }
    }
}
