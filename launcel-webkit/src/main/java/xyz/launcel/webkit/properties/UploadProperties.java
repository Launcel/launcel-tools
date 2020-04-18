package xyz.launcel.webkit.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.common.utils.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Validated
@Getter
@ConfigurationProperties(prefix = "web.upload")
public class UploadProperties
{

    private Boolean      enabled     = false;
    private String       domain      = "";
    private Long         maxSize     = Long.MAX_VALUE;
    private Long         minSize     = 1024L;
    private String       path        = File.separator.concat("Users").concat(File.separator).concat("tmp").concat(File.separator).concat("upload");
    private List<String> fileType    = Arrays.asList("doc", "docx", "pdf", "jpg", "png", "jepg");
    //                                       office07    office03       pdf        jpg         jpg          png       jpeg
    //   private static String[] strings = {"504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff"};
    private List<String> contentType = Arrays.asList("504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff");

    public UploadProperties()
    {
        InnerUploadProperties.domain = domain;
        InnerUploadProperties.maxSize = maxSize;
        InnerUploadProperties.minSize = minSize;
        InnerUploadProperties.path = path;
        InnerUploadProperties.fileType = fileType;
        InnerUploadProperties.contentType = contentType;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


    public void setDomain(String domain)
    {
        InnerUploadProperties.domain = this.domain = StringUtils.isBlank(domain) ? "" : domain;
    }

    public void setMaxSize(Long maxSize)
    {
        InnerUploadProperties.maxSize = this.maxSize = Objects.isNull(maxSize) ? Long.MAX_VALUE : maxSize;
    }

    public void setMinSize(Long minSize)
    {
        InnerUploadProperties.minSize = this.minSize = Objects.isNull(minSize) ? 1024L : minSize;
    }

    public void setPath(String path)
    {
        InnerUploadProperties.path = this.path = StringUtils.isBlank(path) ? File.separator.concat("Users")
                .concat(File.separator)
                .concat("tmp")
                .concat(File.separator)
                .concat("upload") : path;
    }

    public void setFileType(List<String> fileType)
    {
        InnerUploadProperties.fileType = this.fileType = CollectionUtils.isEmpty(fileType) ? Arrays.asList("doc", "docx", "pdf", "jpg", "png",
                "jepg") : fileType;
    }

    public void setContentType(List<String> contentType)
    {
        InnerUploadProperties.contentType = this.contentType = CollectionUtils.isEmpty(contentType) ? Arrays.asList("504b0304", "d0cf11e0", "25504446",
                "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff") : contentType;
    }

    public static String getDomain()
    {
        return InnerUploadProperties.domain;
    }

    public static Long getMaxSize()
    {
        return InnerUploadProperties.maxSize;
    }

    public static Long getMinSize()
    {
        return InnerUploadProperties.minSize;
    }

    public static String getPath()
    {
        return InnerUploadProperties.path;
    }

    public static List<String> getFileType()
    {
        return InnerUploadProperties.fileType;
    }

    public static List<String> getContentType()
    {
        return InnerUploadProperties.contentType;
    }

    static class InnerUploadProperties
    {
        private static String       domain;
        private static Long         maxSize;
        private static Long         minSize;
        private static String       path;
        private static List<String> fileType;
        private static List<String> contentType;

    }
}
