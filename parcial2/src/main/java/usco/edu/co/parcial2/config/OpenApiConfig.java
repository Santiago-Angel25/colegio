package usco.edu.co.parcial2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parcialOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parcial 2 - Gestion de Asignaturas")
                        .version("1.0")
                        .description("Servicios web para gestionar asignaturas, horarios, docentes, estudiantes y permisos por rol."));
    }
}
