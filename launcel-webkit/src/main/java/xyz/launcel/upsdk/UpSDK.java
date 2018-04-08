package xyz.launcel.upsdk;

import org.springframework.web.multipart.MultipartFile;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.prop.UploadProperties;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpSDK {


    private UploadProperties properties;

    public UpSDK(UploadProperties properties) {
        this.properties = properties;
    }

    public UpSDK() {
    }

    /**
     * @param file
     * @return net resource url
     */
    public String upload(MultipartFile file) {
        try {
            checkContent(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkSize(file);
        String oldName = file.getOriginalFilename();
        String newName = getNewFileName(getExt(oldName));
        String savePath = File.separator + newName;
        String genPath = properties.getPath() + savePath;
        File dir = new File(genPath);
        if (!dir.getParentFile().exists())
            if (dir.getParentFile().mkdirs()) {
                try {
//                    file.transferTo(dir);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dir));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                    return properties.getDomain() + savePath;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    private void checkSize(MultipartFile file) {
        if (file.getSize() < (properties.getMinSize() * 2 << 19))
            ExceptionFactory.create("文件太小");
        if (file.getSize() > properties.getMaxSize() * 2 << 19)
            ExceptionFactory.create("文件大小超过限制");
    }

    /**
     * 检验上传文件的头信息，用来判断是否是非法文件
     *
     * @param file
     */
    private void checkContent(MultipartFile file) throws IOException {
        InputStream in = null;
        try {
            in = file.getInputStream();
            byte[] b = new byte[4];
            if (in == null)
                ExceptionFactory.create("无法识别的文件");
            if (in.read(b, 0, b.length) < 4)
                ExceptionFactory.create("文件太小");
            StringBuilder sb = new StringBuilder();
            String hv;
            for (byte b1 : b) {
                hv = Integer.toHexString(b1 & 0xFF).toLowerCase();
                if (hv.length() < 2) sb.append(0);
                sb.append(hv);
            }
            if (properties.getContentType().contains(sb.toString()))
                return;
            ExceptionFactory.create("不能接收的文件类型");
        } finally {
            if (in != null) in.close();
        }
    }

    private String getExt(String originalName) {
        Integer index = originalName.lastIndexOf(".");
        if (index <= 0) ExceptionFactory.create("无法识别的文件");
        String ext = originalName.substring(index + 1);
        checkFile(ext);
        return ext;
    }

    private void checkFile(String ext) {
        if (properties.getFileType().contains(ext.toLowerCase())) {
            return;
        }
        ExceptionFactory.create("不能接收的文件类型");
    }

    private String getNewFileName(String ext) {
        return new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd").format(new Date()) +
                File.separator + StringUtils.getUUID() + "." + ext;
    }

    public static void main(String[] args) {
        System.out.println(new UpSDK().getNewFileName("png"));
    }

}
