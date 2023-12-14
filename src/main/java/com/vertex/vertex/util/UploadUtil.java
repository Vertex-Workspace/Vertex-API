package com.vertex.vertex.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class UploadUtil {

    public static boolean uploadImage(MultipartFile file) {

        boolean success = false;

        if (!file.isEmpty()) {
            String filename = file.getOriginalFilename();

            try {
                String folder = "D:\\Miguel\\Teste\\Imgs";
                File dir = new File(folder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(
                        dir.getAbsolutePath()
                        + File.separator
                        + filename);

                BufferedOutputStream stream = new BufferedOutputStream((new FileOutputStream(serverFile)));

                stream.write(file.getBytes());
                stream.close();
                
            } catch (Exception e) {

            }
        }

    }

}
