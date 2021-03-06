package xyz.launcel.upload;

import lombok.var;
import org.springframework.web.multipart.MultipartFile;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.properties.UploadProperties;
import xyz.launcel.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Launcel
 */
public class UploadLocalUtil
{


    private static UploadProperties properties;

    public static void init(UploadProperties properties)
    {
        UploadLocalUtil.properties = properties;
    }

    public static String save(File file)
    {
        if (Objects.isNull(properties))
        {
            ExceptionFactory.create("-1", "没有设置相关上传配置...");
        }

        try
        {
            var in = new FileInputStream(file);
            check(in, file.length());
            var newName = getNewName(file.getName());
            var dir     = new File(getGenPath(newName));
            if (!dir.getParentFile().exists())
            {
                if (!dir.getParentFile().mkdirs())
                {
                    return null;
                }
            }
            var out    = new BufferedOutputStream(new FileOutputStream(dir));
            int bytesRead;
            var buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
            in.close();
            return getDomainPath(newName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void check(InputStream in, Long size)
    {
        try
        {
            checkContent(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        checkSize(size);
    }


    /**
     * @param file
     *
     * @return net resource url
     */
    public String upload(MultipartFile file)
    {
        try
        {
            check(file.getInputStream(), file.getSize());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        var newName = getNewName(file.getOriginalFilename());
        var dir     = new File(getGenPath(newName));
        if (!dir.getParentFile().exists())
        {
            if (!dir.getParentFile().mkdirs())
            {
                return null;
            }
        }
        try
        {
            //            file.transferTo(dir);
            var out = new BufferedOutputStream(new FileOutputStream(getGenPath(newName)));
            out.write(file.getBytes());
            out.flush();
            out.close();
            return getDomainPath(newName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void checkSize(Long size)
    {
        if (size < (properties.getMinSize()))
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的文件太小");
        }
        if (size > properties.getMaxSize())
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的文件大小超过限制");
        }
    }

    /**
     * 检验上传文件的头信息，用来判断是否是非法文件
     *
     * @param in
     */
    private static void checkContent(InputStream in) throws IOException
    {
        //        InputStream in = file.getInputStream();
        var b = new byte[4];
        if (in == null)
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为无法识别的文件");
        }
        if (in.read(b, 0, b.length) < 4)
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的文件太小");
        }
        var    sb = new StringBuilder();
        String hv;
        for (byte b1 : b)
        {
            hv = Integer.toHexString(b1 & 0xFF).toLowerCase();
            if (hv.length() < 2)
            {
                sb.append(0);
            }
            sb.append(hv);
        }
        if (properties.getContentType().contains(sb.toString()))
        {
            return;
        }
        ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为不能接收的文件类型");
    }

    private static String getExt(String originalName)
    {
        int index = originalName.lastIndexOf(".");
        if (index <= 0)
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为无法识别的文件");
        }
        var ext = originalName.substring(index + 1);
        checkFile(ext);
        return ext;
    }

    private static void checkFile(String ext)
    {
        if (properties.getFileType().contains(ext.toLowerCase()))
        {
            return;
        }
        ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为不能接收的文件类型");
    }

    private static String getNewName(String oldName)
    {
        return getNewFileName(getExt(oldName));
    }

    private static String getSavePath(String newName)
    {
        return File.separator + newName;
    }

    private static String getGenPath(String newName)
    {
        return properties.getPath() + getSavePath(newName);
    }

    private static String getDomainPath(String newName)
    {
        return properties.getDomain() + getSavePath(newName);
    }

    private static String getNewFileName(String ext)
    {
        return new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd").format(
                new Date()) + File.separator + StringUtils.getUUID() + "." + ext;
    }

}
