package br.com.fiap.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "ErroResponse", description = "Resposta padrão de erro")
public record ErroResponse(
        @Schema(description = "Data e hora do erro", example = "2026-10-21T19:30:00.000-03:00")
        OffsetDateTime timestamp,

        @Schema(description = "Código HTTP", example = "404")
        int status,

        @Schema(description = "Descrição do erro", example = "Pedido não encontrado")
        String erro,

        @Schema(description = "Detalhes do erro", example = "[\"Pedido 3f2b1c9e-8a4d-4e2b-9c1a-7d5e6f8a9b0c não encontrado\"]")
        List<String> detalhes,

        @Schema(description = "Caminho da requisição", example = "/pedidos/3f2b1c9e-8a4d-4e2b-9c1a-7d5e6f8a9b0c")
        String caminho,

        @Schema(description = "Identificador da requisição", example = "b7e4a2d1-5c3f-4a8e-9b6d-1e2f3a4b5c6d")
        String requestId
) {
}
