package xyz.launcel.controller;

import xyz.launcel.dao.Page;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.response.Response;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController extends BaseLogger {
    
    @Inject
    private HttpServletRequest HttpRequest;
    
    @Inject
    private HttpServletResponse HttpResponse;
    
    
    protected <T> Page<T> initPaging() {
//        String pageNoStr = getReqLocal().getParameter("pageNo");
//        String rowStr = getReqLocal().getParameter("maxRow");
        String pageNoStr = getRequest().getParameter("pageNo");
        String rowStr    = getRequest().getParameter("maxRow");
//        String lowerIdStr = getRequest().getParameter("lowerId");
//        String largeIdStr = getRequest().getParameter("largeId");
        
        Integer pageNo = Integer.valueOf(StringUtils.isNotBlank(pageNoStr) ? pageNoStr.trim() : "1");
        Integer maxRow = Integer.valueOf(StringUtils.isNotBlank(rowStr) ? rowStr.trim() : "15");
//        Integer lowerId = Integer.valueOf(StringUtils.isNotBlank(lowerIdStr) ? lowerIdStr.trim() : "1");
//        Integer largeId = Integer.valueOf(StringUtils.isNotBlank(largeIdStr) ? largeIdStr.trim() : "1");
//        return new Paging<>(pageNo, maxRow, lowerId, largeId);
        //noinspection unchecked
        return new Page<>(pageNo, maxRow);
    }
    
    protected HttpServletRequest getRequest() {
        return HttpRequest;
    }
    
    protected HttpServletResponse getResponse() {
        return HttpResponse;
    }
    
    protected Response getSuccess(Object o) {
        return new Response(true, o);
    }
    
    protected Response getFail() {
        Response response = new Response();
        response.setIsOk(false);
        return response;
    }
    
    protected Response getFail(String msg) {
        return new Response(false, msg);
    }
    
    
}
