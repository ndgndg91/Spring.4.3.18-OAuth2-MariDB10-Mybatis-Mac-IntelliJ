package com.ndgndg91.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void uploadImageOfMember(String path, MultipartFile file) {
        File conFile = new File(path, file.getOriginalFilename());
        try {
            conFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(conFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void makeDirectory(String filePath){
        File folder = new File(filePath);
        folder.mkdirs();
    }
}
