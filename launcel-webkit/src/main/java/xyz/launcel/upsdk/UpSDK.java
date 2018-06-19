package xyz.launcel.upsdk;

import org.springframework.web.multipart.MultipartFile;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.properties.UploadProperties;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Launcel
 */
public class UpSDK
{


    private UploadProperties properties;

    public UpSDK(UploadProperties properties) { this.properties = properties; }

    public UpSDK()                            {}

    public String upload(File file)
    {
        try
        {
            InputStream in = new FileInputStream(file);
            check(in, file.length());
            String newName = getNewName(file.getName());
            File   dir     = new File(getGenPath(newName));
            if (!dir.getParentFile().exists())
            {
                if (!dir.getParentFile().mkdirs()) { return null; }
            }
            BufferedOutputStream out    = new BufferedOutputStream(new FileOutputStream(dir));
            int                  bytesRead;
            byte[]               buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
            in.close();
            return getDomainPath(newName);
        }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    private void check(InputStream in, Long size)
    {
        try { checkContent(in); }
        catch (IOException e) { e.printStackTrace(); }
        checkSize(size);
    }


    /**
     * @param file
     *
     * @return net resource url
     */
    public String upload(MultipartFile file)
    {
        try { check(file.getInputStream(), file.getSize()); }
        catch (IOException e) { e.printStackTrace(); }
        String newName = getNewName(file.getOriginalFilename());
        File   dir     = new File(getGenPath(newName));
        if (!dir.getParentFile().exists())
        {
            if (!dir.getParentFile().mkdirs()) { return null; }
        }
        try
        {
            //            file.transferTo(dir);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(getGenPath(newName)));
            out.write(file.getBytes());
            out.flush();
            out.close();
            return getDomainPath(newName);
        }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    private void checkSize(Long size)
    {
        if (size < (properties.getMinSize())) { ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的文件太小"); }
        if (size > properties.getMaxSize()) { ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的文件大小超过限制"); }
    }

    /**
     * 检验上传文件的头信息，用来判断是否是非法文件
     *
     * @param in
     */
    private void checkContent(InputStream in) throws IOException
    {
        //        InputStream in = file.getInputStream();
        byte[] b = new byte[4];
        if (in == null) { ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为无法识别的文件"); }
        if ((in != null ? in.read(b, 0, b.length) : 0) < 4) { ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的文件太小"); }
        StringBuilder sb = new StringBuilder();
        String        hv;
        for (byte b1 : b)
        {
            hv = Integer.toHexString(b1 & 0xFF).toLowerCase();
            if (hv.length() < 2) { sb.append(0); }
            sb.append(hv);
        }
        if (properties.getContentType().contains(sb.toString())) { return; }
        ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为不能接收的文件类型");
    }

    private String getExt(String originalName)
    {
        Integer index = originalName.lastIndexOf(".");
        if (index <= 0)
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为无法识别的文件");
        }
        String ext = originalName.substring(index + 1);
        checkFile(ext);
        return ext;
    }

    private void checkFile(String ext)
    {
        if (properties.getFileType().contains(ext.toLowerCase())) { return; }
        ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "上传的为不能接收的文件类型");
    }

    private String getNewName(String oldName)    { return getNewFileName(getExt(oldName)); }

    private String getSavePath(String newName)   { return File.separator + newName; }

    private String getGenPath(String newName)    { return properties.getPath() + getSavePath(newName); }

    private String getDomainPath(String newName) { return properties.getDomain() + getSavePath(newName); }

    private String getNewFileName(String ext)
    {
        return new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd").format(new Date()) + File.separator + StringUtils.getUUID() + "." + ext;
    }

}
