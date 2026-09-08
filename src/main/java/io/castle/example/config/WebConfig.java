package io.castle.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves the static assets and the Castle browser SDK. The browser SDK is
 * served straight from the npm install (node_modules) instead of being vendored
 * into the repo, matching the other Castle example apps.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:static/");
        registry.addResourceHandler("/vendor/castle-js/**")
                .addResourceLocations("file:node_modules/@castleio/castle-js/dist/");
    }
}
