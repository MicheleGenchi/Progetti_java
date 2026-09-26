package comgenchi.geotools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GeoToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoToolsApplication.class, args);
	}

	    // Incolla questo blocco qui sotto:
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://0.0.0.0:7001",
                            "http://localhost:7001",
                            "http://localhost:8081",
                            "http://0.0.0.0:8081",
                            "http://casagenchi5.ddns.net:8081")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
