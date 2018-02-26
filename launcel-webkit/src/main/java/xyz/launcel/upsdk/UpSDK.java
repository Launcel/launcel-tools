package xyz.launcel.upsdk;

import org.springframework.web.multipart.MultipartFile;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UpSDK {

    private static String domain = "";

    private static String path;

    private static Integer max_size;

    private static Integer min_size = 0;

    /**
     * 多种文件类型
     */
    private static List<String> fileType;
    //                               office07    office03       pdf        jpg         jpg          png       jpeg
    private static String[] strings = {"504b0304", "d0cf11e0", "25504446", "ffd8ffe0", "ffd8ffe1", "89504e47", "ffd8ff"};

    private static List<String> contentType = new ArrayList<>(Arrays.asList(strings));

    public static String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getMax_size() {
        return max_size;
    }

    public void setMax_size(Integer max_size) {
        this.max_size = max_size;
    }

    public Integer getMin_size() {
        return min_size;
    }

    public void setMin_size(Integer min_size) {
        this.min_size = min_size;
    }

    public List<String> getFileType() {
        return fileType;
    }

    public void setFileType(List<String> fileType) {
        this.fileType = fileType;
    }

    public String upload(MultipartFile file) {
        try {
            checkContent(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkSize(file);
        String oldName = file.getOriginalFilename();
        String newName = getNewFileName(getExt(oldName));
        StringBuilder sb = new StringBuilder(domain).append(path).append(File.separator).append(newName);
        File dir = new File(sb.toString());
        if (!dir.getParentFile().exists())
            if (dir.getParentFile().mkdirs()) {
                try {
                    file.transferTo(dir);
                    return sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    private void checkSize(MultipartFile file) {
        if (file.getSize() < (min_size * 2 << 19))
            ExceptionFactory.create("文件太小");
        if (file.getSize() > max_size * 2 << 19)
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
            if (contentType.contains(sb.toString()))
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
        if (fileType.contains(ext.toLowerCase())) return;
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
