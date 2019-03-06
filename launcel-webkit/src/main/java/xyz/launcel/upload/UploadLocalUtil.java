package xyz.launcel.upload;

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

    private static void checkSize(Long size)
    {
        if (size < (properties.getMinSize()))
        {
            ExceptionFactory.create("0412");
        }
        if (size > properties.getMaxSize())
        {
            ExceptionFactory.create("0413");
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
            ExceptionFactory.create("0414");
        }
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
        if (index <= 0)
        {
            ExceptionFactory.create("0414");
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
        ExceptionFactory.create("0415");
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
}
