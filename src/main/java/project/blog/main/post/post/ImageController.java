package project.blog.main.post.post;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageController implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                // 이미지 파일의 요청 경로를 지정한다.
                .addResourceHandler("/images/**")
                // 이미지 파일을 불러올 로컬 저장소의 위치를 지정한다.
                .addResourceLocations("file:/C:/Users/admin/IdeaProjects/blog/src/main/resources/static/postimage/");
    }
}
