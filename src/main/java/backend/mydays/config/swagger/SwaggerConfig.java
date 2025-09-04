package backend.mydays.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Mydays API")
                .version("1.0")
                .description("하루 챌린지 Mydays의 API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
