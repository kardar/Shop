package com.shop.user;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadFileUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try(InputStream inputStream = multipartFile.getInputStream()) {
             Path filePath = uploadPath.resolve(fileName);
             Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ex){
            throw  new IOException("Could not save file"+fileName, ex);
        }
    }

    public static void cleanDirectory(String dir) throws IOException {
        Path dirPath = Paths.get(dir);
        try{
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    } catch (IOException e) {
                        System.out.println("Could not delete file"+file );
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Could not list directory"+ dirPath);
        }
    }
    public static void removeDir(String dir) throws IOException {
        cleanDirectory(dir);
        Files.delete(Paths.get(dir));

    }
}