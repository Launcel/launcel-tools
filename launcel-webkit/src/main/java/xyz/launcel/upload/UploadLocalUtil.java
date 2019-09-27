package xyz.launcel.upload;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.BusinessException;
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

    public static void init(@NonNull final UploadProperties properties)
    {
        UploadLocalUtil.properties = properties;
    }

    public static String save(@NonNull final File file)
    {
        if (Objects.isNull(properties))
        {
            ExceptionFactory.create("0411");
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
            throw new BusinessException("0416");
        }
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

    private static void checkSize(Long size)
    {
        Me.builder(size < (properties.getMinSize())).isTrue("0412");
        Me.builder(size > properties.getMaxSize()).isTrue("0413");
    }

    /**
     * 检验上传文件的头信息，用来判断是否是非法文件
     *
     * @param in 文件流
     */
    private static void checkContent(InputStream in) throws IOException
    {
        //        InputStream in = file.getInputStream();
        var b = new byte[4];
        Me.builder(in).isNull("0414");
        if (in.read(b, 0, b.length) < 4)
        {
            ExceptionFactory.create("0412");
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
        ExceptionFactory.create("0415");
    }

    private static String getExt(String originalName)
    {
        int index = originalName.lastIndexOf(".");
        Me.builder(index <= 0).isTrue("0414");
        var ext = originalName.substring(index + 1);
        checkFile(ext);
        return ext;
    }

    private static void checkFile(String ext)
    {
        Me.builder(properties.getFileType().contains(ext.toLowerCase())).isFalse("0415");
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

    /**
     * @param file 文件
     * @return net resource url
     */
    public static String upload(MultipartFile file)
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
            throw new BusinessException("0416");
        }
    }
}
