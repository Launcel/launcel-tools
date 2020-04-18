package xyz.launcel.webkit.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.common.utils.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//@Setter
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

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setDomain(String domain)
    {
        UploadConfig.domain = this.domain = StringUtils.isBlank(domain) ? "" : domain;
    }

    public void setMaxSize(Long maxSize)
    {
        UploadConfig.maxSize = this.maxSize = Objects.isNull(maxSize) ? Long.MAX_VALUE : maxSize;
    }

    public void setMinSize(Long minSize)
    {
        UploadConfig.minSize = this.minSize = Objects.isNull(minSize) ? 1024L : minSize;
    }

    public void setPath(String path)
    {
        UploadConfig.path = this.path = StringUtils.isNotBlank(path) ? path : File.separator.concat("Users")
                .concat(File.separator)
                .concat("tmp")
                .concat(File.separator)
                .concat("upload");
    }

    public void setFileType(List<String> fileType)
    {
        UploadConfig.fileType = this.fileType = CollectionUtils.isEmpty(fileType) ? Arrays.asList("doc", "docx", "pdf", "jpg", "png", "jepg") : fileType;
    }

    public void setContentType(List<String> contentType)
    {
        UploadConfig.contentType = this.contentType = CollectionUtils.isEmpty(contentType) ? Arrays.asList("504b0304", "d0cf11e0", "25504446", "ffd8ffe0",
                "ffd8ffe1", "89504e47", "ffd8ff") : contentType;
    }

    public static UploadConfig getConfig()
    {
        return new UploadConfig();
    }


    public static class UploadConfig
    {
        private static String       domain      = "";
        private static Long         maxSize     = Long.MAX_VALUE;
        private static Long         minSize     = 1024L;
        private static String       path        = File.separator.concat("Users").concat(File.separator).concat("tmp").concat(File.separator).concat("upload");
        private static List<String> fileType    = Arrays.asList("doc", "docx", "pdf", "jpg", "png", "jepg");
        private static List<String> contentType = Arrays.asList("504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff");

        public String getDomain()
        {
            return UploadConfig.domain;
        }

        public Long getMaxSize()
        {
            return UploadConfig.maxSize;
        }

        public Long getMinSize()
        {
            return UploadConfig.minSize;
        }

        public String getPath()
        {
            return UploadConfig.path;
        }

        public List<String> getFileType()
        {
            return UploadConfig.fileType;
        }

        public List<String> getContentType()
        {
            return UploadConfig.contentType;
        }
    }
}
