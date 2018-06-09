package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "web.upload")
public class UploadProperties
{

    private Boolean enabled = false;
    private String  domain  = "";
    private Long    maxSize = (long) Integer.MAX_VALUE;
    private Long    minSize = 1024L;
    private String path;
    /**
     * 多种文件类型
     */
    private List<String> fileType = Arrays.asList("doc", "docx", "pdf", "jpg", "png", "jepg");
    //                                       office07    office03       pdf        jpg         jpg          png       jpeg
    //   private static String[] strings = {"504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff"};

    private List<String> contentType = Arrays.asList("504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff");

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public Long getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(Long maxSize)
    {
        this.maxSize = maxSize;
    }

    public Long getMinSize()
    {
        return minSize;
    }

    public void setMinSize(Long minSize)
    {
        this.minSize = minSize;
    }

    public List<String> getFileType()
    {
        return fileType;
    }

    public void setFileType(List<String> fileType)
    {
        this.fileType = fileType;
    }

    public List<String> getContentType()
    {
        return contentType;
    }

    public void setContentType(List<String> contentType)
    {
        this.contentType = contentType;
    }
}
