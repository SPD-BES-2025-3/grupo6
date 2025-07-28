package br.com.ufg.odm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de Bibliotecas - API Pública")
                        .description("API pública para consulta de dados com MongoDB do sistema de bibliotecas")
                        .version("1.0.0")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                );
    }
}
