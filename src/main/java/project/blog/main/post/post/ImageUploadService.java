package project.blog.main.post.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@Service
public class ImageUploadService {

    private final String uploadDir = "C:/Users/admin/IdeaProjects/blog/src/main/resources/static/postimage/";
    private static final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);

    public String saveImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String saveFileName = generateFileName(extName);

        File targetFile = new File(uploadDir, saveFileName);
        file.transferTo(targetFile);

        String imageUrl = "/images/" + saveFileName;
        logger.debug("Image saved successfully: {}", imageUrl);
        return imageUrl;
    }

    private String generateFileName(String extName) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR)
                + String.valueOf(calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + calendar.get(Calendar.HOUR_OF_DAY)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.SECOND)
                + calendar.get(Calendar.MILLISECOND)
                + extName;
    }
}
