package shareYourFashion.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shareYourFashion.main.constant.ImageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String REQUEST_URL = "/";
    private static final String REDIRECT_URL = "/login";
    private static final String ERROR_URL = "/error/**";
    private static final String ERROR_PAGE_CLASSPATH = "classpath:templates/error/";
    private static final String DEFAULT_IMAGE_PATH = "/images/";
    private static final String EXTERNAL_PROFILE_IMAGE_URL = "file:///Users/ahnkwanghyun/uploads/fashionCommunity/user/profileImage/";
    private static final String EXTERNAL_BACKGROUND_PROFILE_IMAGE_URL =  "file:///Users/ahnkwanghyun/uploads/fashionCommunity/user/backgroundProfileImage/";
    private static final String EXTERNAL_USER_FILE_PATH = "file:///Users/ahnkwanghyun/uploads/fashionCommunity/user/**";

    private final String uploadImagesPath;

    public WebMvcConfig(@Value("${custom.path.upload-images}")String uploadImagesPath) {
        this.uploadImagesPath = uploadImagesPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/summernoteImage/**")
                .addResourceLocations("file:///C:/seul/");

        // user image 외부 경로 접근 허용
//        List<String> imageFolders = Stream.of(ImageType.values())
//                                            .map(ImageType::name)
//                                            .collect(Collectors.toList());
//
//
//        for(String imageFolder : imageFolders) {
//            registry.addResourceHandler(DEFAULT_IMAGE_PATH + imageFolder + "/**")
//                    .addResourceLocations("file:///" + uploadImagesPath + imageFolder + "/");
//
//        }

    }
}
