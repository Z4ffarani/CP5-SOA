package br.com.fiap.pedidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pedidosOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pedidos")
                        .version("1.0.0")
                        .description("API REST para criação e consulta de pedidos de comércio eletrônico."))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local")));
    }
}
