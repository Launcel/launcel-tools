package xyz.launcel.redis.properties;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@ConfigurationProperties(prefix = "web.upload")
public class UploadProperties
{

    private Boolean      enabled  = false;
    @NonNull
    private String       domain   = "";
    private Long         maxSize  = Long.MAX_VALUE;
    private Long         minSize  = 1024L;
    private String       path     = File.separator.concat("Users").concat(File.separator).concat("tmp").concat(File.separator).concat("upload");
    /**
     * 多种文件类型
     */
    private List<String> fileType = Arrays.asList("doc", "docx", "pdf", "jpg", "png", "jepg");
    //                                       office07    office03       pdf        jpg         jpg          png       jpeg
    //   private static String[] strings = {"504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff"};

    private List<String> contentType = Arrays.asList("504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff");

    public void setEnabled(Boolean enabled)
    {
        this.enabled = Objects.nonNull(enabled) ? enabled : false;
    }
}
