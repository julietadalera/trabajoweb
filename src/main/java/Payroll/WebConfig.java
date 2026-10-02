package Payroll;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String pastaUploads = UploadController.PASTA
            .toAbsolutePath()
            .toUri()
            .toString();

        if (!pastaUploads.endsWith("/")) {
            pastaUploads += "/";
        }

        // /img/arquivo.jpg busca primeiro em uploads/ e depois em static/img/
        registry.addResourceHandler("/img/**")
            .addResourceLocations(pastaUploads, "classpath:/static/img/");
    }
}
