package ua.com.owu.feb_2023_springboot.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathToFolder = "file:///" + System.getProperty("user.home") + File.separator + "images" + File.separator;
        registry
                .addResourceHandler("/photo/**")
                .addResourceLocations(pathToFolder);
    }
}

//<img src="localhost:8080/photo/model s.jpg">
//<img src="localhost:8080/photo/jerry.png">
//<img src="localhost:8080/photo/merry.png">
