package mvc.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${storage.max-size}")
    private String maxStorageSizeString;

    private long maxStorageSize;

    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        maxStorageSize = parseStorageSize(maxStorageSizeString);
    }

    private long parseStorageSize(String storageSize) {
        long size = 0;
        String lowerCaseStorageSize = storageSize.toLowerCase();

        if (lowerCaseStorageSize.endsWith("mb")) {
            size = Long.parseLong(lowerCaseStorageSize.replace("mb", "")) * 1024 * 1024;
        }
        if (lowerCaseStorageSize.endsWith("gb")) {
            size = Long.parseLong(lowerCaseStorageSize.replace("gb", "")) * 1024 * 1024 * 1024;
        } else {
            System.out.println("Make storage.max-size in MB or GB");
        }
        return size;
    }

    public boolean isStorageLimitReached(long fileSize) {
        long currentStorageSize = calculateStorageSize();
        return currentStorageSize + fileSize > maxStorageSize;
    }

    private long calculateStorageSize() {
        File dir = new File(uploadPath);
        long currentStorageSize = 0;
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    currentStorageSize += file.length();
                }
            }
        }
        return currentStorageSize;
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (isStorageLimitReached(file.getSize())) {
            return null;
        }

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + originalFileName;

        assert originalFileName != null;
        File targetFile = new File(dir, resultFileName);
        file.transferTo(targetFile);

        return resultFileName;
    }

}
