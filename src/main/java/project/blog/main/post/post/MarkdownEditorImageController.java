package project.blog.main.post.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MarkdownEditorImageController {

    private final ImageUploadService imageUploadService;

    @Autowired
    public MarkdownEditorImageController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/image")
    public @ResponseBody Map<String, String> writeTestPost(@RequestParam("image") MultipartFile multi) {
        Map<String, String> response = new HashMap<>();
        try {
            String imageUrl = imageUploadService.saveImage(multi);
            response.put("url", imageUrl);
        } catch (IOException e) {
            response.put("error", "Image upload failed: " + e.getMessage());
        }
        return response;
    }
}