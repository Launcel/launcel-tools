package xyz.launcel.upsdk;

import org.springframework.web.multipart.MultipartFile;
import xyz.launcel.hook.ApplicationContextHook;

public class UpLoadTool {
    
    private UpLoadTool() {
    }
    
    private static UpSDK upSDK = ApplicationContextHook.getBean("upSDK");
    
    public static String upload(MultipartFile file) {
        return upSDK.upload(file);
    }
}
